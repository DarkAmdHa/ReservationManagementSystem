import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import com.google.gson.JsonObject;
import com.reservationsystem.utils.LogUtils;
import com.reservationsystem.utils.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/EditProfileServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2 MB
                 maxFileSize = 1024 * 1024 * 10,       // 10 MB
                 maxRequestSize = 1024 * 1024 * 50)    // 50 MB
public class EditProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet?notLoggedIn=true");
            return;
        }

        // Set user information as request attributes
        request.setAttribute("userName", user.getName());
        request.setAttribute("userEmail", user.getEmail());
        request.setAttribute("userId", user.getId());
        request.setAttribute("userAvatarUrl", user.getAvatarUrl());

        // Forward to the editProfile.jsp page
        request.getRequestDispatcher("/WEB-INF/editProfile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
        	sendJsonResponse(response, "USER_NOT_LOGGED_IN", "Not Logged In");
            return;
        }

        // Update user information based on the form data
        String newName = request.getParameter("name");
        String newEmail = request.getParameter("email");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        Boolean isEditted = false;

        // Validate the entered current password
        if (currentPassword != null && !currentPassword.isEmpty() && !currentPassword.equals(user.getPassword())) {
            sendJsonResponse(response, "ERROR", "Please provide the correct current password.");
            return;
        }

        // Check if a new name is provided
        if (newName != null && !newName.isEmpty()) {
            user.setName(newName);
            isEditted = true;
        }

        // Check if a new email is provided and it's different from the current one
        if (newEmail != null && !newEmail.isEmpty() && !newEmail.equals(user.getEmail())) {
            // Check if the new email already exists in the system
            if (isExistingEmail(newEmail)) {
                sendJsonResponse(response, "ERROR", "This email already exists.");
                return;
            }
            user.setEmail(newEmail);
            isEditted = true;
        }

        // Check if a new password is provided and matches the confirmation
        if (newPassword != null && !newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                sendJsonResponse(response, "ERROR", "The new password does not match the confirmation password");
                return;
            }
            // Update the user's password
            user.setPassword(newPassword);
            isEditted= true;
        }

        // Check if a new avatar file is uploaded
        Part avatarPart = request.getPart("avatar");
        if (avatarPart != null && avatarPart.getSize() > 0) {
        	  // Get the uploaded file name
            String fileName = System.currentTimeMillis() + "_" + avatarPart.getSubmittedFileName();

            System.out.println(getServletContext().getRealPath("/static"));
            String uploadDir = getServletContext().getRealPath("/static/images/avatars");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdir();
            }

            // Save the file to the specified directory
            avatarPart.write(uploadDir + File.separator + fileName);

            // Delete the current avatar file (if the user has one)
            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                File currentAvatarFile = new File(uploadDir + File.separator + user.getAvatarUrl());
                if (currentAvatarFile.exists()) {
                    currentAvatarFile.delete();
                }
            }

            // Update the user's avatar URL
            user.setAvatarUrl(fileName);
            isEditted = true;
        }
        
        if(isEditted) {
	    	// Log profile update
	        LogUtils.logProfileUpdate(user);
        }

        // Redirect to the reservations page after updating the profile
        sendJsonResponse(response, "SUCCESS", "Profile updated successfully");
    }

    private boolean isExistingEmail(String email) {
        return User.isEmailExists(email);
    }

    // Method to send JSON response
    private void sendJsonResponse(HttpServletResponse response, String status, String message) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", status);
        jsonResponse.addProperty("message", message);

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }
}

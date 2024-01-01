import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import com.reservationsystem.utils.User;

import java.io.File;
import java.io.IOException;

@WebServlet("/EditProfileServlet")
@MultipartConfig
public class EditProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Check if the user is already logged in
        if (session.getAttribute("user") == null) {
       	 //If so,redirect
       	 response.sendRedirect(request.getContextPath() + "/LoginServlet?notLoggedIn=true");
       	 return;
        }
        
        
        User user = (User) session.getAttribute("user");

        // Set user information as request attributes
        request.setAttribute("userName", user.getName());
        request.setAttribute("userEmail", user.getEmail());

        // Forward to the editProfile.jsp page
        request.getRequestDispatcher("/WEB-INF/editProfile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Update user information based on the form data
        String newName = request.getParameter("name");
        String newEmail = request.getParameter("email");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Check if the entered current password matches the user's actual password
        if (!currentPassword.equals(user.getPassword())) {
            // Handle incorrect password scenario
            response.sendRedirect(request.getContextPath() + "/WEB-INF/editProfile.jsp?error=IncorrectPassword");
            return;
        }

        // Update user information (name and email)
        user.setName(newName);
        user.setEmail(newEmail);

        // Check if a new password is provided and matches the confirmation
        if (!newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                // Handle password mismatch scenario
                response.sendRedirect(request.getContextPath() + "/WEB-INF/editProfile.jsp?error=PasswordMismatch");
                return;
            }
            // Update the user's password
            user.setPassword(newPassword);
        }

        // Check if a new avatar file is uploaded
        if (request.getPart("avatar") != null && !request.getPart("avatar").getSubmittedFileName().isEmpty()) {
            // Get the uploaded file
            Part avatarPart = request.getPart("avatar");
            String fileName = System.currentTimeMillis() + "_" + avatarPart.getSubmittedFileName();

            // Specify the directory to save the uploaded file
            String uploadDir = getServletContext().getRealPath("/uploads");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdir();
            }

            // Save the file to the specified directory
            avatarPart.write(uploadDir + File.separator + fileName);

            // Update the user's avatar URL
            user.setAvatarUrl(fileName);
        }

        // Redirect to the reservations page after updating the profile
        response.sendRedirect(request.getContextPath() + "/ReservationsServlet");
    }
}

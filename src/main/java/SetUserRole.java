import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.reservationsystem.utils.DatabaseUtils;
import com.reservationsystem.utils.LogUtils;
import com.reservationsystem.utils.User;

@WebServlet("/SetUserRole")
public class SetUserRole extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SetUserRole() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if the user is logged in and is an admin
        if (user == null || !"ADMIN".equals(user.getRole())) {
            sendJsonResponse(response, "USER_NOT_AUTHORIZED", "User not authorized to make this request.");
            return;
        }

        // Read request body
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String requestData = buffer.toString();

        // Parse JSON request
        Gson gson = new Gson();
        UserRoleUpdate requestObj = gson.fromJson(requestData, UserRoleUpdate.class);

        // Update user role in database
        if (updateUserRole(requestObj.getUserId(), requestObj.getRole())) {
            sendJsonResponse(response, "success", "User role updated successfully.");
            
            // Log update
            if(requestObj.getRole() == "MANAGER") {
            	LogUtils.logRoleUpdate(user, "Set as manager, user of id " + requestObj.getUserId());
            }else {
            	LogUtils.logRoleUpdate(user, "Set as normal user, user of id " + requestObj.getUserId());
            }
        } else {
            sendJsonResponse(response, "error", "Failed to update user role.");
        }
    }

    private boolean updateUserRole(int userId, String role) {
        String query = "UPDATE user SET role = ? WHERE id = ?";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, role);
            preparedStatement.setInt(2, userId);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
            return false;
        }
    }

    private void sendJsonResponse(HttpServletResponse response, String status, String message) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", status);
        jsonResponse.addProperty("message", message);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(jsonResponse));
    }

    private static class UserRoleUpdate {
        private int userId;
        private String role;

        // Getters and setters
        public int getUserId() {
            return userId;
        }

        public String getRole() {
            return role;
        }
    }
}

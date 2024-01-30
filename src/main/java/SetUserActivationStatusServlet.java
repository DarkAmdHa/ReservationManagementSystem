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

@WebServlet("/SetUserActivationStatusServlet")
public class SetUserActivationStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SetUserActivationStatusServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if the user is logged in and is an admin
        if (user == null || "NORMAL".equals(user.getRole())) {
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
        UserActivationUpdate requestObj = gson.fromJson(requestData, UserActivationUpdate.class);

        // Update user activation status in database
        if (updateUserActivationStatus(requestObj.getUserId(), requestObj.getIsActive())) {
            sendJsonResponse(response, "success", "User activation status updated successfully.");
            
            // Log update
            if(requestObj.getIsActive() == 1) {
            	LogUtils.logActivationStatusUpdate(user, "Activated User of id " + requestObj.getUserId());
            }else {
            	LogUtils.logActivationStatusUpdate(user, "Deactivated User of id " + requestObj.getUserId());
            }
            
        } else {
            sendJsonResponse(response, "error", "Failed to update user activation status.");
        }
    }

    private boolean updateUserActivationStatus(int userId, int isActive) {
        String query = "UPDATE user SET isActive = ?, isDeactivated = ? WHERE id = ?";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    		
            preparedStatement.setInt(1, isActive);
            preparedStatement.setInt(2, isActive == 1 ? 0 : 1);
            preparedStatement.setInt(3, userId);

            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println(userId + "sssssssssssadddad" +  isActive);
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

    private static class UserActivationUpdate {
        private int userId;
        private int isActive;

        // Getters and setters
        public int getUserId() {
            return userId;
        }


        public int getIsActive() {
            return isActive;
        }

    }
}

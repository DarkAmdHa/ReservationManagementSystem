

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

/**
 * Servlet implementation class SetReservationStatus
 */
public class SetReservationStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetReservationStatusServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if the user is logged in
        if (user == null || user.getRole().equals("NORMAL")) {
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
        ReservationStatusUpdate requestObj = gson.fromJson(requestData, ReservationStatusUpdate.class);
        
        if(requestObj.getStatusUpdate() != null) {
            // Update reservation status in database
            if (updateReservationStatus(requestObj.getId(), requestObj.getStatus())) {
                sendJsonResponse(response, "success", "Reservation status updated successfully.");
             // Log update
        	LogUtils.logReservationStatusUpdate(user, "Reservation of id status changed to " + requestObj.getStatus());
                
            } else {
                sendJsonResponse(response, "error", "Failed to update reservation status.");
            }
        }else {
            // Update reservation status in database
            if (updateReservationApprovalStatus(requestObj.getId(), requestObj.getStatus())) {
                sendJsonResponse(response, "success", "Reservation approval status updated successfully.");
             // Log update
                if(requestObj.getStatus() == "APPROVED") {
                	LogUtils.logReservationAcceptance(user, "Accepted Reservation of id " + requestObj.getId());
                }else {
                	LogUtils.logReservationRejection(user, "Rejected Reservation of id " + requestObj.getId());
                }
                
            } else {
                sendJsonResponse(response, "error", "Failed to update reservation approval status.");
            }
        }


    }

    private boolean updateReservationStatus(int reservationId, String status) {
        String query = "UPDATE reservation SET status = ? WHERE id = ?";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, reservationId);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
            return false;
        }
    }
    
    
    private boolean updateReservationApprovalStatus(int reservationId, String status) {
        String query = "UPDATE reservation SET approvalStatus = ? WHERE id = ?";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, reservationId);

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

    private static class ReservationStatusUpdate {
        private int id;
        private String status;
        private Boolean statusUpdate;

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Boolean getStatusUpdate() {
            return statusUpdate;
        }

        public void setStatusUpdate(Boolean statusUpdate) {
            this.statusUpdate = statusUpdate;
        }
        
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}

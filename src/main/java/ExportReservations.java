

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import com.reservationsystem.utils.Reservation;
import com.reservationsystem.utils.User;


/**
 * Servlet implementation class ExportServlet
 */
public class ExportReservations extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExportReservations() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 HttpSession session = request.getSession();
         // Check if the user is already logged in
         if (session.getAttribute("user") == null) {
        	 //If so,redirect
        	 response.sendRedirect(request.getContextPath() + "/LoginServlet?notLoggedIn=true");
        	 return;
         }
         
         
         
      // Retrieve user information from the session
         User user = (User) session.getAttribute("user");
         int userId = user.getId();
         
         String userRole =  user.getRole();
         
         if (userRole.equals("NORMAL") || userRole.equals("MANAGER")) {
        	 response.sendRedirect(request.getContextPath() + "/ReservationsServlet");
        	 return;
         }
		         
         StringBuilder buffer = new StringBuilder();
         BufferedReader reader = request.getReader();
         String line;
         while ((line = reader.readLine()) != null) {
             buffer.append(line);
         }
         String requestData = buffer.toString();

         Gson gson = new Gson();
         ExportRequest exportRequest = gson.fromJson(requestData, ExportRequest.class);
         
         
         if ("on".equals(exportRequest.getSearchedReservations()) && (exportRequest.getFrom() == null || exportRequest.getTo() == null)) {
             // Both start date and end date are required for export
             response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Start date and end date are required.");
             return;
         }
		         
         response.setContentType("text/csv");
         response.setHeader("Content-Disposition", "attachment; filename=\"reservations.csv\"");

	        
        try (PrintWriter writer = response.getWriter()) {
            
            // Write CSV header
        	 writer.println("Reservation ID,User Id,User Name,User Email,Start Time,End Time,Date,Room ID,Room Name,Table ID,Table Name,Seats Capacity,Approval Status, Status");
            
            // Fetch all tables from the database
        	 
        	 
            List<Reservation> allReservations;
            
            if ("on".equals(exportRequest.getSearchedReservations())) {
                // Perform search
                allReservations = user.searchReservations( exportRequest.getSearchBy(),exportRequest.getSearchTerm());
            } else {
                // Filter by date range
                allReservations = user.reservationsByDate(exportRequest.getFrom(), exportRequest.getTo());
            }
            
            
            
            // Write table data to CSV
            for (Reservation reservation : allReservations) {
                writer.println(String.format("%d,%d,%s,%s,%s,%s,%s,%d,%s,%d,%s,%s,%s,%s",
	                reservation.getId(),
	                reservation.getUserId(),
	                reservation.getUserName(),
	                reservation.getUserEmail(),
	                reservation.getStartTime(),
	                reservation.getEndTime(),
	                reservation.getDate(),
	                reservation.getRoomId(),
	                reservation.getRoomName(),
	                reservation.getTableId(),
	                reservation.getTableName(),
	                reservation.getTableCapacity(),
	                reservation.getApprovalStatus(),
	                reservation.getStatus()
            		));

            }
        }
	}
	
	
    private static class ExportRequest {
        private String from;
        private String to;
        private String searchTerm;
        private String searchBy;
        private String searchedReservations;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getSearchTerm() {
            return searchTerm;
        }

        public void setSearchTerm(String searchTerm) {
            this.searchTerm = searchTerm;
        }
        
        public String getSearchBy() {
            return searchBy;
        }

        public void setSearchBy(String searchBy) {
            this.searchBy = searchBy;
        }
        
        public String getSearchedReservations() {
            return searchedReservations;
        }

        public void setSearchedReservations(String searchedReservations) {
            this.searchedReservations = searchedReservations;
        }
    }


}

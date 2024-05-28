

import jakarta.servlet.RequestDispatcher;
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
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.reservationsystem.utils.DatabaseUtils;
import com.reservationsystem.utils.User;

/**
 * Servlet implementation class Analytics
 */
public class Analytics extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Analytics() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
         
         if (userRole.equals("NORMAL")) {
        	 response.sendRedirect(request.getContextPath() + "/ReservationsServlet");
        	 return;
         }
         
         // Get the selected timeframe parameter
         String timeframe = "today";
         
         // Initialize a map to hold the analytics data
         Map<String, Object> analyticsData = new HashMap<>();
         
         
         
         List<Map<String, Integer>> peakHoursData = getPeakHours(timeframe);
         analyticsData.put("peakHours", peakHoursData);
         
         // Retrieve cancellation rate
         double cancellationRate = getCancellationRate(timeframe);
         analyticsData.put("cancellationRate", cancellationRate);

         // Retrieve attendance rate
         double attendanceRate = getAttendanceRate(timeframe);
         analyticsData.put("attendanceRate", attendanceRate);

         // Retrieve most reserved seats by capacity
         Map<String, Integer> mostReservedSeats = getMostReservedSeatsByTimeFrame(timeframe);
         analyticsData.put("mostReservedSeats", mostReservedSeats);
         
         int reservationCount = getReservationCountByTimeFrame(timeframe);
         request.setAttribute("reservationCount", reservationCount);

         
         // Set user information as request attributes
         request.setAttribute("userName", user.getName());
         request.setAttribute("userEmail", user.getEmail());
         request.setAttribute("userRole", user.getRole());
         request.setAttribute("userId", userId);
         request.setAttribute("userAvatarUrl", user.getAvatarUrl());
         request.setAttribute("cancellationRate", cancellationRate);
         request.setAttribute("attendanceRate", attendanceRate);
         request.setAttribute("mostReservedSeats", mostReservedSeats);
         request.setAttribute("peakHoursData", peakHoursData);
         request.setAttribute("reservationCount", reservationCount);
         
         RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/analytics.jsp");
         dispatcher.forward(request, response);
         
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 HttpSession session = request.getSession();
	        User user = (User) session.getAttribute("user");

	        // Check if the user is logged in
	        if (user == null || user.getRole().equals("NORMAL")) {
	            sendJsonResponse(response, "USER_NOT_AUTHORIZED", "User not authorized to make this request.", null);
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
	        Map<String, String> requestObj = gson.fromJson(requestData, new TypeToken<Map<String, String>>(){}.getType());

	        // Extract timeFrame and stat parameters
	        String timeFrame = requestObj.get("timeFrame");
	        String stat = requestObj.get("stat");

	        Object data = null;
	        switch (stat) {
	            case "peak-hours":
	                data = getPeakHours(timeFrame);
	                break;
	            case "cancellation-rate":
	                data = getCancellationRate(timeFrame);
	                break;
	            case "attendance-rate":
	                data = getAttendanceRate(timeFrame);
	                break;
	            case "seats-booked":
	                data = getMostReservedSeatsByTimeFrame(timeFrame);
	                break;
	            case "reservations":
	                data = getReservationCountByTimeFrame(timeFrame);
	                break;
	            default:
	                sendJsonResponse(response, "INVALID_STAT", "Invalid stat parameter.", null);
	                return;
	        }

	        // Package the retrieved data into a JSON response
	        Map<String, Object> responseData = new HashMap<>();

	        // Send JSON response
	        sendJsonResponse(response,"success", "New data fetched", data);
	    }

	    // Helper method to send JSON response
	    private void sendJsonResponse(HttpServletResponse response, String status, String message, Object data) throws IOException {
	        JsonObject jsonResponse = new JsonObject();
	        jsonResponse.addProperty("status", status);
	        jsonResponse.addProperty("message", message);
	        if(data != null) {
		        jsonResponse.addProperty("data", new Gson().toJson(data));
	        }

	
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write(new Gson().toJson(jsonResponse));
	    }
	
	
	public int getReservationCountByTimeFrame(String timeframe) {
	    String query = "";
	    switch (timeframe) {
	        case "today":
	            query = "SELECT COUNT(*) as count FROM reservation WHERE date = CURDATE()";
	            break;
	        case "week":
	            query = "SELECT COUNT(*) as count FROM reservation WHERE YEARWEEK(date, 1) = YEARWEEK(CURDATE(), 1)";
	            break;
	        case "month":
	            query = "SELECT COUNT(*) as count FROM reservation WHERE YEAR(date) = YEAR(CURDATE()) AND MONTH(date) = MONTH(CURDATE())";
	            break;
	        case "year":
	            query = "SELECT COUNT(*) as count FROM reservation WHERE YEAR(date) = YEAR(CURDATE())";
	            break;
	    }

	    int reservationCount = 0;
	    try (Connection connection = DatabaseUtils.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        if (resultSet.next()) {
	            reservationCount = resultSet.getInt("count");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Handle exception, log error, etc.
	    }
	    return reservationCount;
	}
	
	
	public List<Map<String, Integer>> getPeakHours(String timeframe) {
	    String query = "";
	    switch (timeframe) {
	        case "today":
	            query = "SELECT HOUR(startTime) as hour, COUNT(*) as count FROM reservation WHERE date = CURDATE() AND status IN ('Reserved', 'Attended', 'Missed') GROUP BY HOUR(startTime) ORDER BY hour";
	            break;
	        case "week":
	            query = "SELECT HOUR(startTime) as hour, COUNT(*) as count FROM reservation WHERE YEARWEEK(date, 1) = YEARWEEK(CURDATE(), 1) AND status IN ('Reserved', 'Attended', 'Missed') GROUP BY HOUR(startTime) ORDER BY hour";
	            break;
	        case "month":
	            query = "SELECT HOUR(startTime) as hour, COUNT(*) as count FROM reservation WHERE YEAR(date) = YEAR(CURDATE()) AND MONTH(date) = MONTH(CURDATE()) AND status IN ('Reserved', 'Attended', 'Missed') GROUP BY HOUR(startTime) ORDER BY hour";
	            break;
	        case "year":
	            query = "SELECT HOUR(startTime) as hour, COUNT(*) as count FROM reservation WHERE YEAR(date) = YEAR(CURDATE()) AND status IN ('Reserved', 'Attended', 'Missed') GROUP BY HOUR(startTime) ORDER BY hour";
	            break;
	    }

	    List<Map<String, Integer>> peakHoursData = new ArrayList<>();
	    try (Connection connection = DatabaseUtils.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        // Initialize an array to hold count for each hour of the day
	        int[] hourCounts = new int[24];
	        while (resultSet.next()) {
	            int hour = resultSet.getInt("hour");
	            int count = resultSet.getInt("count");
	            hourCounts[hour] = count;
	        }

	        // Convert the hour counts array into a list of maps
	        for (int hour = 0; hour < 24; hour++) {
	            Map<String, Integer> hourData = new HashMap<>();
	            hourData.put("hour", hour);
	            hourData.put("count", hourCounts[hour]);
	            peakHoursData.add(hourData);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Handle exception, log error, etc.
	    }
	    return peakHoursData;
	}
	
	
    public double getCancellationRate(String timeframe) {
        String query = "";
        switch (timeframe) {
            case "today":
                query = "SELECT COUNT(*) as total_reservations, SUM(CASE WHEN status = 'Cancelled' THEN 1 ELSE 0 END) as cancelled_reservations FROM reservation WHERE date = CURDATE()";
                break;
            case "week":
                query = "SELECT COUNT(*) as total_reservations, SUM(CASE WHEN status = 'Cancelled' THEN 1 ELSE 0 END) as cancelled_reservations FROM reservation WHERE YEARWEEK(date, 1) = YEARWEEK(CURDATE(), 1)";
                break;
            case "month":
                query = "SELECT COUNT(*) as total_reservations, SUM(CASE WHEN status = 'Cancelled' THEN 1 ELSE 0 END) as cancelled_reservations FROM reservation WHERE YEAR(date) = YEAR(CURDATE()) AND MONTH(date) = MONTH(CURDATE())";
                break;
            case "year":
                query = "SELECT COUNT(*) as total_reservations, SUM(CASE WHEN status = 'Cancelled' THEN 1 ELSE 0 END) as cancelled_reservations FROM reservation WHERE YEAR(date) = YEAR(CURDATE())";
                break;
        }

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                int totalReservations = resultSet.getInt("total_reservations");
                int cancelledReservations = resultSet.getInt("cancelled_reservations");
                if (totalReservations > 0) {
                	// Your calculation
                	double percentage = (double) cancelledReservations / totalReservations * 100;

                	// Create a DecimalFormat object with desired formatting
                	DecimalFormat df = new DecimalFormat("#.##");

                	// Format the percentage using DecimalFormat
                	String formattedPercentage = df.format(percentage);

                	// Convert the formatted string back to double if needed
                	double formattedValue = Double.parseDouble(formattedPercentage);
                	
                    return (double) formattedValue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception, log error, etc.
        }
        return 0.0;
    }
    
    public double getAttendanceRate(String timeframe) {
        String query = "";
        switch (timeframe) {
            case "today":
                query = "SELECT COUNT(*) as total_reservations, SUM(CASE WHEN status = 'Attended' THEN 1 ELSE 0 END) as attended_reservations FROM reservation WHERE date = CURDATE()";
                break;
            case "week":
                query = "SELECT COUNT(*) as total_reservations, SUM(CASE WHEN status = 'Attended' THEN 1 ELSE 0 END) as attended_reservations FROM reservation WHERE YEARWEEK(date, 1) = YEARWEEK(CURDATE(), 1)";
                break;
            case "month":
                query = "SELECT COUNT(*) as total_reservations, SUM(CASE WHEN status = 'Attended' THEN 1 ELSE 0 END) as attended_reservations FROM reservation WHERE YEAR(date) = YEAR(CURDATE()) AND MONTH(date) = MONTH(CURDATE())";
                break;
            case "year":
                query = "SELECT COUNT(*) as total_reservations, SUM(CASE WHEN status = 'Attended' THEN 1 ELSE 0 END) as attended_reservations FROM reservation WHERE YEAR(date) = YEAR(CURDATE())";
                break;
        }

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                int totalReservations = resultSet.getInt("total_reservations");
                int attendedReservations = resultSet.getInt("attended_reservations");
                if (totalReservations > 0) {
                  	double percentage = (double) attendedReservations / totalReservations * 100;

                	// Create a DecimalFormat object with desired formatting
                	DecimalFormat df = new DecimalFormat("#.##");

                	// Format the percentage using DecimalFormat
                	String formattedPercentage = df.format(percentage);

                	// Convert the formatted string back to double if needed
                	double formattedValue = Double.parseDouble(formattedPercentage);
                	
                    return (double) formattedValue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception, log error, etc.
        }
        return 0.0;
    }
    
    public Map<String, Integer> getMostReservedSeatsByTimeFrame(String timeframe) {
        String query = "";
        switch (timeframe) {
            case "today":
                query = "SELECT t.seatsCapacity, COUNT(*) as reservation_count FROM reservation r JOIN restauranttable t ON r.tableId = t.id WHERE r.date = CURDATE() GROUP BY t.seatsCapacity";
                break;
            case "week":
                query = "SELECT t.seatsCapacity, COUNT(*) as reservation_count FROM reservation r JOIN restauranttable t ON r.tableId = t.id WHERE YEARWEEK(r.date, 1) = YEARWEEK(CURDATE(), 1) GROUP BY t.seatsCapacity";
                break;
            case "month":
                query = "SELECT t.seatsCapacity, COUNT(*) as reservation_count FROM reservation r JOIN restauranttable t ON r.tableId = t.id WHERE YEAR(r.date) = YEAR(CURDATE()) AND MONTH(r.date) = MONTH(CURDATE()) GROUP BY t.seatsCapacity";
                break;
            case "year":
                query = "SELECT t.seatsCapacity, COUNT(*) as reservation_count FROM reservation r JOIN restauranttable t ON r.tableId = t.id WHERE YEAR(r.date) = YEAR(CURDATE()) GROUP BY t.seatsCapacity";
                break;
        }

        Map<String, Integer> reservationsByCapacity = new HashMap<>();
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int seatsCapacity = resultSet.getInt("seatsCapacity");
                int reservationCount = resultSet.getInt("reservation_count");
                reservationsByCapacity.put(seatsCapacity + " seats", reservationCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception, log error, etc.
        }
        return reservationsByCapacity;
    }
	

}

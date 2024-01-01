import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.reservationsystem.utils.DatabaseUtils;
import com.reservationsystem.utils.Reservation;
import com.reservationsystem.utils.Room;
import com.reservationsystem.utils.Table;
import com.reservationsystem.utils.User;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/MakeReservationServlet")
public class MakeReservationServlet extends HttpServlet {
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
        
        
        // Retrieve user information from the session
        User user = (User) session.getAttribute("user");
        int userId = user.getId();

        // Set user information as request attributes
        request.setAttribute("userName", user.getName());
        request.setAttribute("userEmail", user.getEmail());
        request.setAttribute("userId", userId);
        request.setAttribute("userAvatarUrl", user.getAvatarUrl());

        
        
        // Fetch rooms from the database
        List<Room> rooms = getRoomsFromDatabase();

        // Fetch tables for the first room from the database
        List<Table> tablesForFirstRoom = getTablesForRoomFromDatabase(rooms.get(0).getId());

        // put retrieved data as attributes in the request
        request.setAttribute("rooms", rooms);
        request.setAttribute("tables", tablesForFirstRoom);

        request.getRequestDispatcher("/WEB-INF/makeReservation.jsp").forward(request, response);
    }

    private List<Table> getTablesForRoomFromDatabase(int roomId) {
        // Perform database query to fetch table data for the specified room
        List<Table> tables = new ArrayList<>();

        // Use a prepared statement with a parameterized query
        String query = "SELECT * FROM restauranttable WHERE RoomId = ?";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameter (roomId) for the prepared statement
            preparedStatement.setString(1, Integer.toString(roomId));

            // Execute the query and process the results
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("tableId");
                    String name = resultSet.getString("tableName");
                    String capacity = resultSet.getString("seatsCapacity");
                    Table table = new Table(id, name, capacity, roomId, "");
                    tables.add(table);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        }

        return tables;
    }

    // Example usage in the servlet method
    private List<Room> getRoomsFromDatabase() {
        List<Room> rooms = new ArrayList<>();

        String query = "SELECT roomId, roomName FROM room";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("roomId");
                String name = resultSet.getString("roomName");
                Room room = new Room(id, name);
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        }

        return rooms;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve user and reservation information from the request
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if the user is logged in
        if (session.getAttribute("user") == null) {
	        JsonObject jsonResponse = new JsonObject();
	        jsonResponse.addProperty("status", "error");
	        jsonResponse.addProperty("message", "User session expired.");
	
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write(new Gson().toJson(jsonResponse));
       	 	return;
        }

        String date = request.getParameter("date");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String tableId = request.getParameter("tableId");
        String notes = request.getParameter("notes");

        // Check table availability for the given date and time
        if (isTableAvailable(tableId, date, startTime, endTime)) {
            // Table is available, create reservation
            createReservation(user.getId(), tableId, date, startTime, endTime, notes);

            // Provide success response
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("status", "success");
            jsonResponse.addProperty("message", "Reservation created successfully");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(jsonResponse));
        } else {
            // Table is not available, provide error response
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Selected table is not available for the specified date and time");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(jsonResponse));
        }
    }

    private boolean isTableAvailable(String tableId, String date, String startTime, String endTime) {
        // Perform a database query to check table availability
        String query = "SELECT * FROM reservation WHERE tableId = ? AND date = ? AND ((startTime >= ? AND startTime < ?) OR (endTime > ? AND endTime <= ?))";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters for the prepared statement
            preparedStatement.setString(1, tableId);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, startTime);
            preparedStatement.setString(4, endTime);
            preparedStatement.setString(5, startTime);
            preparedStatement.setString(6, endTime);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If there are no overlapping reservations, the table is available
                return !resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
            return false;
        }
    }

    private void createReservation(int userId, String tableId, String date, String startTime, String endTime, String notes) {
        // Perform a database query to insert the new reservation
        String query = "INSERT INTO reservation (userId, tableId, date, startTime, endTime, notes, approvalStatus) VALUES (?, ?, ?, ?, ?, ?, 'PENDING')";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters for the prepared statement
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, tableId);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, startTime);
            preparedStatement.setString(5, endTime);
            preparedStatement.setString(6, notes);

            // Execute the query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        }
    }
}

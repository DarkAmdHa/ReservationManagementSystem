

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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.reservationsystem.utils.DatabaseUtils;
import com.reservationsystem.utils.LogUtils;
import com.reservationsystem.utils.Reservation;
import com.reservationsystem.utils.Room;
import com.reservationsystem.utils.Table;
import com.reservationsystem.utils.User;

/**
 * Servlet implementation class TableManagementServlet
 */
public class TableManagementServlet extends HttpServlet {
    int PAGE_SIZE = 10;
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TableManagementServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Check if the user is already logged in
        if (session.getAttribute("user") == null) {
            // If not, redirect
            response.sendRedirect(request.getContextPath() + "/LoginServlet?notLoggedIn=true");
            return;
        }

        // Retrieve user information from the session
        User user = (User) session.getAttribute("user");
        int userId = user.getId();
        String userRole = user.getRole();

        if (userRole.equals("NORMAL") || userRole.equals("MANAGER")) {
            response.sendRedirect(request.getContextPath() + "/ReservationsServlet");
            return;
        }
        
        String searchBy = request.getParameter("searchBy");
        String searchTerm = request.getParameter("searchTerm");
        
        // Retrieve tables and rooms for user:
        List<Table> allTables;
        if (searchBy != null && searchTerm != null) {
        	allTables = searchTables(searchBy, searchTerm);
        } else {
        	allTables = getAllTables();
        }

        // Retrieve all possible rooms for when the user creates tables:
        List<Room> allRooms = getAllRooms();

        // Pagination logic
        String pageStr = request.getParameter("page");
        int currentPage = pageStr != null ? Integer.parseInt(pageStr) : 1;
        int startIndex = (currentPage - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, allTables.size());

        // Extract the sublist for the current page
        List<Table> paginatedTables = allTables.subList(startIndex, endIndex);

        // Set user information as request attributes
        request.setAttribute("userName", user.getName());
        request.setAttribute("userEmail", user.getEmail());
        request.setAttribute("userRole", user.getRole());
        request.setAttribute("userId", userId);
        request.setAttribute("userAvatarUrl", user.getAvatarUrl());
        request.setAttribute("allTables", paginatedTables);
        request.setAttribute("allRooms", allRooms);

        // Pagination information
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", (int) Math.ceil((double) allTables.size() / PAGE_SIZE));

        request.setAttribute("searchBy", searchBy);
        request.setAttribute("searchTerm", searchTerm);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/tablesManagement.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handlePostRequest(request, response);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handlePutRequest(request, response);
    }
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	handleDeleteRequest(request, response);
    }
    
    private void handlePostRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.getRole().equals("ADMIN")) {
            sendJsonResponse(response, "USER_NOT_AUTHORIZED", "User not authorized to make this request.");
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
        TableCreateRequest tableRequest = gson.fromJson(requestData, TableCreateRequest.class);

        if (tableRequest.getSeatsCapacity() > 10) {
            sendJsonResponse(response, "error", "Number of seats cannot be more than 10.");
            return;
        }

        if (doesTableNameExist(tableRequest.getName())) {
            sendJsonResponse(response, "error", "Table name already exists.");
            return;
        }

        if (createTable(tableRequest.getName(), tableRequest.getRoomId(), tableRequest.getSeatsCapacity())) {
            sendJsonResponse(response, "success", "Table created successfully.");
            // Log update
        	LogUtils.logTableCreated(user, "Table of name " + tableRequest.getName() + " created.");
        } else {
            sendJsonResponse(response, "error", "Failed to create table.");
        }
    }

    private void handlePutRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.getRole().equals("ADMIN")) {
            sendJsonResponse(response, "USER_NOT_AUTHORIZED", "User not authorized to make this request.");
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
        TableUpdateRequest tableRequest = gson.fromJson(requestData, TableUpdateRequest.class);
        
        Table tableToUpdate = getTableToUpdate(tableRequest.getId());


        if (tableRequest.getSeatsCapacity() > 10) {
            sendJsonResponse(response, "error", "Number of seats cannot be more than 10.");
            return;
        }
        

        
        if (!tableToUpdate.getName().equals(tableRequest.getName()) && doesTableNameExist(tableRequest.getName())) {
            sendJsonResponse(response, "error", "Table name already exists.");
            return;
        }
        if(tableRequest.getName() == "") {
            sendJsonResponse(response, "error", "Table name is required.");
            return;
        }
        
        if (updateTable(tableRequest.getId(), tableRequest.getName(), tableRequest.getRoomId(), tableRequest.getSeatsCapacity())) {
            sendJsonResponse(response, "success", "Table updated successfully.");
            // Log update
            LogUtils.logTableUpdated(user, "Table of name " + tableRequest.getName() + " updated.");
        } else {
            sendJsonResponse(response, "error", "Failed to update table.");
        }
    }

    
    private void handleDeleteRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.getRole().equals("ADMIN")) {
            sendJsonResponse(response, "USER_NOT_AUTHORIZED", "User not authorized to make this request.");
            return;
        }

        int tableId = Integer.parseInt(request.getParameter("id"));

        if (deleteTable(tableId)) {
            sendJsonResponse(response, "success", "Table deleted successfully.");
            // Log deletion
            LogUtils.logTableDeletion(user, "Table with ID " + tableId + " deleted.");
        } else {
            sendJsonResponse(response, "error", "Failed to delete table.");
        }
    }
    
    private boolean createTable(String tableName, int roomId, int numberOfSeats) {
        String query = "INSERT INTO restauranttable (name, roomId, seatsCapacity) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, tableName);
            preparedStatement.setInt(2, roomId);
            preparedStatement.setInt(3, numberOfSeats);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    private boolean updateTable(int tableId, String tableName, int roomId, int numberOfSeats) {
        String query = "UPDATE restauranttable SET name = ?, roomId = ?, seatsCapacity = ? WHERE id = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, tableName);
            preparedStatement.setInt(2, roomId);
            preparedStatement.setInt(3, numberOfSeats);
            preparedStatement.setInt(4, tableId);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean deleteTable(int tableId) {
        String query = "DELETE FROM restauranttable WHERE id = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, tableId);

            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static class TableCreateRequest {
        private String name;
        private int roomId;
        private int seatsCapacity;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public int getSeatsCapacity() {
            return seatsCapacity;
        }

        public void setSeatsCapacity(int seatsCapacity) {
            this.seatsCapacity = seatsCapacity;
        }
    }

    
    private static class TableUpdateRequest {
    private int id;
    private String name;
    private int roomId;
    private int seatsCapacity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getSeatsCapacity() {
        return seatsCapacity;
    }

    public void setSeatsCapacity(int seatsCapacity) {
        this.seatsCapacity = seatsCapacity;
    }
}


    private boolean doesTableNameExist(String tableName) {
        String query = "SELECT COUNT(*) FROM restauranttable WHERE name = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, tableName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Table getTableToUpdate(int id) {
        String query = "SELECT * FROM restauranttable WHERE id = ?";
        Table table = null;
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int tableId = resultSet.getInt("id");
                String tableName = resultSet.getString("name");
                String seatsCapacity = resultSet.getString("seatsCapacity");
                table = new Table(tableId,tableName,seatsCapacity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return table;
    }
    
    private void sendJsonResponse(HttpServletResponse response, String status, String message) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", status);
        jsonResponse.addProperty("message", message);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(jsonResponse));
    }
    
	public List<Room> getAllRooms() {
		  String query = "SELECT * FROM room";
		  List<Room> allRooms = new ArrayList<>();
		  try (Connection connection = DatabaseUtils.getConnection();
		       PreparedStatement preparedStatement = connection.prepareStatement(query);
		       ResultSet resultSet = preparedStatement.executeQuery()) {
		    while (resultSet.next()) {
		      int roomId = resultSet.getInt("id");
		      String roomName = resultSet.getString("name");
		      Room room = new Room(roomId, roomName);
		      allRooms.add(room);
		    }
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		  return allRooms;
		}

    public List<Table> searchTables(String searchBy, String searchTerm) {
    	String query = "SELECT t.*, r.id AS roomId, r.name AS roomName "
        		+ "FROM restauranttable t "
        		+ "LEFT JOIN room r ON t.roomId = r.id ";
        
        List<Table> tables = new ArrayList<>();
        
        query += "WHERE ";
        
	     	  try {
	              Connection connection = DatabaseUtils.getConnection();
	              
	    	    int parameterIndex = 1;
	    	 
	    	    switch (searchBy) {
	    	      case "all":
	    	    	query += "t.name LIKE ? OR t.seatsCapacity LIKE ? OR r.name LIKE ?";
	    	        break;
	    	      case "table_name":
	    	        query += "t.name LIKE ?";
	    	        break;
	    	      case "seat_numbers":
	      	        query += "t.seatsCapacity LIKE ?";
	    	        break;
	    	      case "room_name":
	    	        query += "r.name LIKE ?";
	    	        break;
	    	      default:
	    	        return null;
	    	    }
	    	    query += "ORDER BY t.id DESC";
	    	    PreparedStatement preparedStatement = connection.prepareStatement(query);
	    	    
	            if ("all".equals(searchBy)) {
	                preparedStatement.setString(parameterIndex++, "%" + searchTerm + "%");
	                preparedStatement.setString(parameterIndex++, "%" + searchTerm + "%");
	                preparedStatement.setString(parameterIndex++, "%" + searchTerm + "%");
	            } else {
	                preparedStatement.setString(parameterIndex, "%" + searchTerm + "%");
	            }
	            
	            
	    	    System.out.println("Final SQL Query: " + query);
    
    	    ResultSet resultSet = preparedStatement.executeQuery();

    	     while (resultSet.next()) {
                 int tableId = resultSet.getInt("id");
                 String tableName = resultSet.getString("name");
                 String seatsCapacity = resultSet.getString("seatsCapacity");
                 int roomId = resultSet.getInt("roomId");
                 String roomName = resultSet.getString("roomName");
                 Table table = new Table(tableId,tableName,seatsCapacity, roomId, roomName);
                 tables.add(table);
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tables;
    }
    
    public List<Table> getAllTables() {
        String query = "SELECT t.*, r.id AS roomId, r.name AS roomName "
        		+ "FROM restauranttable t "
        		+ "LEFT JOIN room r ON t.roomId = r.id ORDER BY t.id DESC";
        
        List<Table> allTables = new ArrayList<>();
                
        try {	
	              Connection connection = DatabaseUtils.getConnection();
	              PreparedStatement preparedStatement = connection.prepareStatement(query);
	              ResultSet resultSet = preparedStatement.executeQuery();

	    	     while (resultSet.next()) {
	                 int tableId = resultSet.getInt("id");
	                 String tableName = resultSet.getString("name");
	                 String seatsCapacity = resultSet.getString("seatsCapacity");
	                 int roomId = resultSet.getInt("roomId");
	                 String roomName = resultSet.getString("roomName");
	                 Table table = new Table(tableId,tableName,seatsCapacity, roomId, roomName);
	                 allTables.add(table);
	             }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allTables;
    }

}



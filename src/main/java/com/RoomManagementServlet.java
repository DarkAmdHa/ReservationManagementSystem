package com;

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
import com.reservationsystem.utils.User;

/**
 * Servlet implementation class RoomManagementServlet
 */
public class RoomManagementServlet extends HttpServlet {
	int PAGE_SIZE = 6;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RoomManagementServlet() {
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
         
         if (userRole.equals("NORMAL") || userRole.equals("MANAGER")) {
        	 response.sendRedirect(request.getContextPath() + "/ReservationsServlet");
        	 return;
         }
         
         
         String searchBy = request.getParameter("searchBy");
         String searchTerm = request.getParameter("searchTerm");
         
         List<Room> allRooms;
         if (searchBy != null && searchTerm != null) {
        	 allRooms = searchRooms(searchBy, searchTerm);
         } else {
        	 allRooms = getAllRooms();
         }
         
         // Pagination logic
         String pageStr = request.getParameter("page");
         int currentPage = pageStr != null ? Integer.parseInt(pageStr) : 1;
         int startIndex = (currentPage - 1) * PAGE_SIZE;
         int endIndex = Math.min(startIndex + PAGE_SIZE, allRooms.size());
         
      // Extract the sublist for the current page
         List<Room> paginatedRooms = allRooms.subList(startIndex, endIndex);

         // Set user information as request attributes
         request.setAttribute("userName", user.getName());
         request.setAttribute("userEmail", user.getEmail());
         request.setAttribute("userRole", user.getRole());
         request.setAttribute("userId", userId);
         request.setAttribute("userAvatarUrl", user.getAvatarUrl());
         request.setAttribute("allRooms", paginatedRooms);
         
      // Pagination information
         request.setAttribute("currentPage", currentPage);
         request.setAttribute("totalPages", (int) Math.ceil((double) allRooms.size() / PAGE_SIZE));
         

         request.setAttribute("searchBy", searchBy);
         request.setAttribute("searchTerm", searchTerm);

         RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/roomsManagement.jsp");
         dispatcher.forward(request, response);
	}

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
        RoomCreateRequest roomRequest = gson.fromJson(requestData, RoomCreateRequest.class);


        if (doesRoomNameExist(roomRequest.getName())) {
            sendJsonResponse(response, "error", "Room name already exists.");
            return;
        }

        if (createRoom(roomRequest.getName())) {
            sendJsonResponse(response, "success", "Room created successfully.");
            // Log update
        	LogUtils.logRoomCreated(user, "Room of name " + roomRequest.getName() + " created.");
        } else {
            sendJsonResponse(response, "error", "Failed to create room.");
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
        RoomUpdateRequest roomRequest = gson.fromJson(requestData, RoomUpdateRequest.class);
        
        Room roomToUpdate = getRoomToUpdate(roomRequest.getId());

        
        if (!roomToUpdate.getName().equals(roomRequest.getName()) && doesRoomNameExist(roomRequest.getName())) {
            sendJsonResponse(response, "error", "Room name already exists.");
            return;
        }
        
        if(roomRequest.getName() == "") {
            sendJsonResponse(response, "error", "Room name is required.");
            return;
        }
        
        if (updateRoom(roomRequest.getId(), roomRequest.getName())) {
            sendJsonResponse(response, "success", "Room updated successfully.");
            // Log update
            LogUtils.logRoomUpdated(user, "Room of name " + roomRequest.getName() + " updated.");
        } else {
            sendJsonResponse(response, "error", "Failed to update room.");
        }
    }

    
    private void handleDeleteRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.getRole().equals("ADMIN")) {
            sendJsonResponse(response, "USER_NOT_AUTHORIZED", "User not authorized to make this request.");
            return;
        }

        int roomId = Integer.parseInt(request.getParameter("id"));

        if (deleteRoom(roomId)) {
            sendJsonResponse(response, "success", "Room deleted successfully.");
            // Log deletion
            LogUtils.logRoomDeletion(user, "Room with ID " + roomId + " deleted.");
        } else {
            sendJsonResponse(response, "error", "Failed to delete room.");
        }
    }
    
	
    private Room getRoomToUpdate(int id) {
        String query = "SELECT * FROM room WHERE id = ?";
        Room room = null;
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int roomId = resultSet.getInt("id");
                String roomName = resultSet.getString("name");
                room = new Room(roomId,roomName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }
    
    
    private void sendJsonResponse(HttpServletResponse response, String status, String message) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", status);
        jsonResponse.addProperty("message", message);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(jsonResponse));
    }
    
    private static class RoomCreateRequest {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    
    private static class RoomUpdateRequest {
	    private int id;
	    private String name;
	
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
}
    
    private boolean createRoom(String roomName) {
        String query = "INSERT INTO room (name) VALUES (?)";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, roomName);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    private boolean updateRoom(int roomId, String roomName) {
        String query = "UPDATE room SET name = ? WHERE id = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, roomName);
            preparedStatement.setInt(2, roomId);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean deleteRoom(int roomId) {
        String query = "DELETE FROM room WHERE id = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, roomId);

            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean doesRoomNameExist(String roomName) {
        String query = "SELECT COUNT(*) FROM room WHERE name = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, roomName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
	
	
    public List<Room> searchRooms(String searchBy, String searchTerm) {
    	String query = "SELECT * FROM room r ";
        
        List<Room> rooms = new ArrayList<>();
        
        query += "WHERE ";
        
	     	  try {
	              Connection connection = DatabaseUtils.getConnection();
	              
	    	    int parameterIndex = 1;
	    	 
	    	    switch (searchBy) {
	    	      case "room_name":
	    	    	query += "r.name LIKE ?";
	    	        break;
	    	      default:
	    	        return null;
	    	    }
	    	    query += "ORDER BY r.id DESC";
	    	    PreparedStatement preparedStatement = connection.prepareStatement(query);
	    	    
                preparedStatement.setString(parameterIndex, "%" + searchTerm + "%");
	            
	    	    System.out.println("Final SQL Query: " + query);
    
    	    ResultSet resultSet = preparedStatement.executeQuery();

    	     while (resultSet.next()) {
	   		      int roomId = resultSet.getInt("id");
	   		      String roomName = resultSet.getString("name");
	   		      Room room = new Room(roomId, roomName);
	   		      rooms.add(room);
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }
    

}

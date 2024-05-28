package com.reservationsystem.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.reservationsystem.utils.DatabaseUtils;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;
    private Boolean isActive;
    private Boolean isDeactivated;
    private String avatarUrl;
    
    // Constructors
    public User() {
        // Default constructor
    }

    // Constructor without isActive and id (optional)
    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email.toLowerCase();
        this.password = password;
        this.role = role;
        this.isActive = false; // Set a default value
        this.avatarUrl = "";
    }
    
    // Constructor with isActive (optional)
    public User(String name, String email, String password, String role, Boolean isActive) {
        this(name, email, password, role); // Call the constructor without isActive and id
        this.isActive = isActive;
    }
    
    // Constructor with isActive and id and avatarUrl
    public User(String name, String email, String password, String role, Boolean isActive, int id, String avatarUrl) {
        this(name, email, password, role, isActive); // Call the constructor with isActive
        this.id = id;
        this.avatarUrl = avatarUrl;
    }

    

    
    
    // Getters and setters
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
        updateFieldInDatabase("name", name);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        updateFieldInDatabase("email", email);

    }
    
    public void setEmailNormal(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;


    }

    public void setPassword(String password) {
        this.password = password;
        updateFieldInDatabase("password", password);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive == 1;
    }
    
    public boolean getIsDeactivated() {
        return isDeactivated;
    }
    
    public void setIsDeactivated(int isDeactivated) {
        this.isDeactivated = isDeactivated == 1;
    }
    
    
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        updateFieldInDatabase("avatarUrl", avatarUrl);

    }
    
    private void updateFieldInDatabase(String fieldName, String value) {
        String query = "UPDATE user SET " + fieldName + " = ? WHERE id = ?";
        try {
            Connection connection = DatabaseUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, this.id);

            // Execute the update query
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception, log error, etc.
        }
    }

    
    
    public static RegistrationResult registerUser(String name, String email, String password) {
            // Check if the email is already registered
            String query = "SELECT * FROM user WHERE email = ?";
            try {
        	Connection connection = DatabaseUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Email already exists, registration failed
            	return RegistrationResult.USER_ALREADY_EXISTS;
            }

            // If email is not registered, create a new user and add to the database
            query = "INSERT INTO user (name, email, password, role) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, "NORMAL");
            preparedStatement.executeUpdate();

            // Registration successful
            return RegistrationResult.SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exception, log error, etc.
                return RegistrationResult.ERROR;
            }
    }

 // Method to retrieve a user by email
    public static User getUserByEmail(String email) {
        String query = "SELECT * FROM reservations.user WHERE email=?";
        
        try {
            Connection connection = DatabaseUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, email);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // User found, create a User object and return
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("role"),
                        resultSet.getBoolean("isActive"),
                        resultSet.getInt("id"),
                        resultSet.getString("avatarUrl")
                );
                return user;
            } else {
                // User not found
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception, log error, etc.
            return null;
        }
    }
    
    public static String getUserRole(String email) {
    	String query = "SELECT role FROM reservations.user WHERE email=?";
    	
        try {
            Connection connection = DatabaseUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, email);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // User found, create a User object and return
                return resultSet.getString("role");
            } else {
                // User not found
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception, log error, etc.
            return null;
        }
    }
    
 // Method to authenticate a user during login
    public static LoginResult loginUser(String email, String password) {
        User user = getUserByEmail(email);

        if (user != null) {
            // User found 
        	//check password
            if (password.equals(user.getPassword())) {
                // Passwords match, 
            	if(!user.isActive) {
            		return LoginResult.NOT_ACTIVE_USER;
            	}
            	
            	//Acitve and password matches, login successful
                return LoginResult.SUCCESS;
            } else {
                // Passwords do not match
                return LoginResult.INVALID_CREDENTIALS;
            }
        } else {
            // User not found or account is not active
            return LoginResult.INVALID_CREDENTIALS;
        }
    }
    
    
    public List<Reservation> getUserReservations() {
        String query = "SELECT reservation.id, reservation.date, reservation.approvalStatus, reservation.status, reservation.cancelled_at, reservation.cancellation_reason, "
                + "reservation.startTime, reservation.endTime, room.name as roomName, restauranttable.name as tableName, restauranttable.seatsCapacity as tableCapacity "
                + "FROM reservation "
                + "JOIN restauranttable ON reservation.tableId = restauranttable.id "
                + "JOIN room ON restauranttable.roomId = room.id "
                + "WHERE reservation.userId = ? "
                + "ORDER BY reservation.date ASC, reservation.startTime ASC;";

        try {
            Connection connection = DatabaseUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, this.id);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Reservation> reservations = new ArrayList<>();

            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(resultSet.getInt("id"));
                reservation.setDate(resultSet.getDate("date"));
                reservation.setStartTime(resultSet.getString("startTime"));
                reservation.setEndTime(resultSet.getString("endTime"));
                reservation.setStatus(resultSet.getString("status"));
                reservation.setCancelledAt(resultSet.getTimestamp("cancelled_at"));
                reservation.setCancellationReason(resultSet.getString("cancellation_reason"));
                reservation.setTableName(resultSet.getString("tableName"));
                reservation.setTableCapacity(resultSet.getString("tableCapacity"));
                reservation.setRoomName(resultSet.getString("roomName"));
                reservation.setApprovalStatus(resultSet.getString("approvalStatus"));

                reservations.add(reservation);
            }

            return reservations;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception, log error, etc.
            return null;
        }
    }
    
    public List<Reservation> getPendingUserReservations() {
        String query = "SELECT reservation.id, reservation.date, reservation.approvalStatus, "
                + "reservation.startTime, reservation.endTime, room.name as roomName, restauranttable.name as tableName, restauranttable.seatsCapacity as tableCapacity, user.name as userName, user.avatarUrl as avatarUrl "
                + "FROM reservation "
                + "JOIN user ON reservation.userId = user.id "
                + "JOIN restauranttable ON reservation.tableId = restauranttable.id "
                + "JOIN room ON restauranttable.roomId = room.id "
                + "WHERE reservation.approvalStatus = \"PENDING\" "
                + "ORDER BY reservation.date ASC, reservation.startTime ASC;";

        try {
            Connection connection = DatabaseUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Reservation> reservations = new ArrayList<>();

            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(resultSet.getInt("id"));
                reservation.setDate(resultSet.getDate("date"));
                reservation.setStartTime(resultSet.getString("startTime"));
                reservation.setEndTime(resultSet.getString("endTime"));
                reservation.setTableName(resultSet.getString("tableName"));
                reservation.setTableCapacity(resultSet.getString("tableCapacity"));
                reservation.setRoomName(resultSet.getString("roomName"));
                reservation.setApprovalStatus(resultSet.getString("approvalStatus"));
                reservation.setUserName(resultSet.getString("userName"));
                reservation.setAvatarUrl(resultSet.getString("avatarUrl"));

                reservations.add(reservation);
            }

            return reservations;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception, log error, etc.
            return null;
        }
    }
    
    
    public List<Reservation> searchReservations(String searchBy, String searchTerm) {
        String query = "SELECT reservation.id, reservation.date, reservation.approvalStatus,reservation.status, "
                + "reservation.startTime, reservation.endTime, room.id as roomId, room.name as roomName, restauranttable.id as tableId, restauranttable.name as tableName, restauranttable.seatsCapacity as tableCapacity, user.id as userId, user.name as userName, user.email as userEmail, user.avatarUrl as avatarUrl "
                + "FROM reservation "
                + "JOIN user ON reservation.userId = user.id "
                + "JOIN restauranttable ON reservation.tableId = restauranttable.id "
                + "JOIN room ON restauranttable.roomId = room.id ";

    	  
          List<Reservation> reservations = new ArrayList<>();

    	  // Append WHERE clause based on searchBy (improved with prepared statement)


    	  try {
              Connection connection = DatabaseUtils.getConnection();
              
    	    int parameterIndex = 1;
    	    
    	    if(searchBy.equals("all") && searchTerm.equals("")) {
        	    query += " ORDER BY reservation.date ASC, reservation.startTime ASC";
    	    }else {
	    	  query += "WHERE ";
	    	  
	     	 
	    	    switch (searchBy) {
	    	      case "all":
	    	    	  query += "user.name LIKE ? OR user.email LIKE ? OR restauranttable.name LIKE ? OR restauranttable.seatsCapacity LIKE ? OR room.name LIKE ?";
	      	        break;
	    	      case "user_name":
	    	    	query += "user.name LIKE ?";
	    	        
	    	        break;
	    	      case "user_email":
	    	        query += "user.email LIKE ?";
	    	        break;
	    	      case "table_number":
	    	        query += "restauranttable.name LIKE ?";
	    	        break;
	    	      case "seat_numbers":
	      	        query += "restauranttable.seatsCapacity LIKE ?";
	    	        break;
	    	      case "room_name":
	    	        query += "room.name LIKE ?";
	    	        break;
	    	      default:
	    	        return null;
	    	    }
	    	    query += " ORDER BY reservation.date ASC, reservation.startTime ASC";
    	    }
    	    
    	    PreparedStatement preparedStatement = connection.prepareStatement(query);
    	    
    	    if(!searchBy.equals("all") || !searchTerm.equals("")) {
                if ("all".equals(searchBy)) {
                    preparedStatement.setString(parameterIndex++, "%" + searchTerm + "%");
                    preparedStatement.setString(parameterIndex++, "%" + searchTerm + "%");
                    preparedStatement.setString(parameterIndex++, "%" + searchTerm + "%");
                    preparedStatement.setString(parameterIndex++, "%" + searchTerm + "%");
                    preparedStatement.setString(parameterIndex++, "%" + searchTerm + "%");
                } else {
                    preparedStatement.setString(parameterIndex, "%" + searchTerm + "%");
                }
    	    }


    	    System.out.println("Final SQL Query: " + query); // Print the final query with placeholders
    
    	    ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(resultSet.getInt("id"));
                reservation.setDate(resultSet.getDate("date"));
                reservation.setStartTime(resultSet.getString("startTime"));
                reservation.setEndTime(resultSet.getString("endTime"));
                reservation.setTableId(resultSet.getInt("tableId"));
                reservation.setTableName(resultSet.getString("tableName"));
                reservation.setTableCapacity(resultSet.getString("tableCapacity"));
                reservation.setRoomId(resultSet.getInt("roomId"));
                reservation.setRoomName(resultSet.getString("roomName"));
                reservation.setApprovalStatus(resultSet.getString("approvalStatus"));
                reservation.setStatus(resultSet.getString("status"));
                reservation.setUserId(resultSet.getInt("userId"));
                reservation.setUserName(resultSet.getString("userName"));
                reservation.setUserEmail(resultSet.getString("userEmail"));
                reservation.setAvatarUrl(resultSet.getString("avatarUrl"));

                reservations.add(reservation);
            }
    	  } catch (Exception e) {
    	    e.printStackTrace();
    	    return null;
    	  }

    	  return reservations; // Assuming reservations list is populated within the try block
    	}

    
    
    public List<Reservation> getAllUserReservations() {
        String query = "SELECT reservation.id, reservation.date, reservation.approvalStatus, reservation.status, "
                + "reservation.startTime, reservation.endTime, room.id as roomId, room.name as roomName, restauranttable.id as tableId, restauranttable.name as tableName, restauranttable.seatsCapacity as tableCapacity, user.id as userId, user.name as userName, user.email as userEmail, user.avatarUrl as avatarUrl "
                + "FROM reservation "
                + "JOIN user ON reservation.userId = user.id "
                + "JOIN restauranttable ON reservation.tableId = restauranttable.id "
                + "JOIN room ON restauranttable.roomId = room.id "
                + "ORDER BY reservation.date ASC, reservation.startTime ASC;";

        try {
            Connection connection = DatabaseUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Reservation> reservations = new ArrayList<>();

            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(resultSet.getInt("id"));
                reservation.setDate(resultSet.getDate("date"));
                reservation.setStartTime(resultSet.getString("startTime"));
                reservation.setEndTime(resultSet.getString("endTime"));
                reservation.setTableId(resultSet.getInt("tableId"));
                reservation.setTableName(resultSet.getString("tableName"));
                reservation.setTableCapacity(resultSet.getString("tableCapacity"));
                reservation.setRoomId(resultSet.getInt("roomId"));
                reservation.setRoomName(resultSet.getString("roomName"));
                reservation.setApprovalStatus(resultSet.getString("approvalStatus"));
                reservation.setStatus(resultSet.getString("status"));
                reservation.setUserId(resultSet.getInt("userId"));
                reservation.setUserName(resultSet.getString("userName"));
                reservation.setUserEmail(resultSet.getString("userEmail"));
                reservation.setAvatarUrl(resultSet.getString("avatarUrl"));

                reservations.add(reservation);
            }

            return reservations;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception, log error, etc.
            return null;
        }
    }
    
    
    public List<Reservation> searchPendingReservations(String searchBy, String searchTerm) {
        String query = "SELECT reservation.id, reservation.date, reservation.approvalStatus, reservation.status, "
                + "reservation.startTime, reservation.endTime, room.id as roomId, room.name as roomName, restauranttable.id as tableId, restauranttable.name as tableName, restauranttable.seatsCapacity as tableCapacity, user.id as userId, user.name as userName, user.email as userEmail, user.avatarUrl as avatarUrl "
                + "FROM reservation "
                + "JOIN user ON reservation.userId = user.id "
                + "JOIN restauranttable ON reservation.tableId = restauranttable.id "
                + "JOIN room ON restauranttable.roomId = room.id "
                + "WHERE reservation.approvalStatus = \"PENDING\" ";

    	  
          List<Reservation> reservations = new ArrayList<>();

    	  // Append WHERE clause based on searchBy (improved with prepared statement)


    	  try {
              Connection connection = DatabaseUtils.getConnection();
              
    	    int parameterIndex = 1;
    	    
    	    if(searchBy.equals("all") && searchTerm.equals("")) {
        	    query += " ORDER BY reservation.date ASC, reservation.startTime ASC";
    	    }else {	     	 
	    	    switch (searchBy) {
	    	      case "all":
	    	    	  query += "AND (user.name LIKE ? OR user.email LIKE ? OR restauranttable.name LIKE ? OR restauranttable.seatsCapacity LIKE ? OR room.name LIKE ?)";
	      	        break;
	    	      case "user_name":
	    	    	query += "AND user.name LIKE ?";
	    	        
	    	        break;
	    	      case "user_email":
	    	        query += "AND user.email LIKE ?";
	    	        break;
	    	      case "table_number":
	    	        query += "AND restauranttable.name LIKE ?";
	    	        break;
	    	      case "seat_numbers":
	      	        query += "AND restauranttable.seatsCapacity LIKE ?";
	    	        break;
	    	      case "room_name":
	    	        query += "AND room.name LIKE ?";
	    	        break;
	    	      default:
	    	        return null;
	    	    }
	    	    query += " ORDER BY reservation.date ASC, reservation.startTime ASC";
    	    }
    	    
    	    PreparedStatement preparedStatement = connection.prepareStatement(query);
    	    
    	    if(!searchBy.equals("all") || !searchTerm.equals("")) {
                if ("all".equals(searchBy)) {
                    preparedStatement.setString(parameterIndex++, "%" + searchTerm + "%");
                    preparedStatement.setString(parameterIndex++, "%" + searchTerm + "%");
                    preparedStatement.setString(parameterIndex++, "%" + searchTerm + "%");
                    preparedStatement.setString(parameterIndex++, "%" + searchTerm + "%");
                    preparedStatement.setString(parameterIndex++, "%" + searchTerm + "%");
                } else {
                    preparedStatement.setString(parameterIndex, "%" + searchTerm + "%");
                }
    	    }


    	    System.out.println("Final SQL Query: " + query); // Print the final query with placeholders
    
    	    ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(resultSet.getInt("id"));
                reservation.setDate(resultSet.getDate("date"));
                reservation.setStartTime(resultSet.getString("startTime"));
                reservation.setEndTime(resultSet.getString("endTime"));
                reservation.setTableId(resultSet.getInt("tableId"));
                reservation.setTableName(resultSet.getString("tableName"));
                reservation.setTableCapacity(resultSet.getString("tableCapacity"));
                reservation.setRoomId(resultSet.getInt("roomId"));
                reservation.setRoomName(resultSet.getString("roomName"));
                reservation.setApprovalStatus(resultSet.getString("approvalStatus"));
                reservation.setStatus(resultSet.getString("status"));
                reservation.setUserId(resultSet.getInt("userId"));
                reservation.setUserName(resultSet.getString("userName"));
                reservation.setUserEmail(resultSet.getString("userEmail"));
                reservation.setAvatarUrl(resultSet.getString("avatarUrl"));

                reservations.add(reservation);
            }
    	  } catch (Exception e) {
    	    e.printStackTrace();
    	    return null;
    	  }

    	  return reservations; // Assuming reservations list is populated within the try block
    	}

    public List<Reservation> reservationsByDate(String startDate, String endDate) {
        String query = "SELECT reservation.id, reservation.date, reservation.approvalStatus, reservation.status, "
                + "reservation.startTime, reservation.endTime, room.id as roomId, room.name as roomName, restauranttable.id as tableId, restauranttable.name as tableName, restauranttable.seatsCapacity as tableCapacity, user.id as userId, user.name as userName, user.email as userEmail, user.avatarUrl as avatarUrl "
                + "FROM reservation "
                + "JOIN user ON reservation.userId = user.id "
                + "JOIN restauranttable ON reservation.tableId = restauranttable.id "
                + "JOIN room ON restauranttable.roomId = room.id ";

    	  
          List<Reservation> reservations = new ArrayList<>();

    	  // Append WHERE clause based on searchBy (improved with prepared statement)
    	  

    	  try {
              Connection connection = DatabaseUtils.getConnection();

      	    
    	    int parameterIndex = 1;
    	 
    	       if (!startDate.equals("") && !endDate.equals("")) {
    	            query += "WHERE reservation.date >= ? AND reservation.date <= ?";
    	        }
    	    query += " ORDER BY reservation.date ASC, reservation.startTime ASC";
    	    
      	    PreparedStatement preparedStatement = connection.prepareStatement(query);
      	    
 	       if (!startDate.equals("") && !endDate.equals("")) {
 	            preparedStatement.setString(parameterIndex++, startDate);
 	            preparedStatement.setString(parameterIndex++, endDate);
	        }
 	       


    	    System.out.println("Final SQL Query: " + query); 
    	    
    	    ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(resultSet.getInt("id"));
                reservation.setDate(resultSet.getDate("date"));
                reservation.setStartTime(resultSet.getString("startTime"));
                reservation.setEndTime(resultSet.getString("endTime"));
                reservation.setTableId(resultSet.getInt("tableId"));
                reservation.setTableName(resultSet.getString("tableName"));
                reservation.setTableCapacity(resultSet.getString("tableCapacity"));
                reservation.setRoomId(resultSet.getInt("roomId"));
                reservation.setRoomName(resultSet.getString("roomName"));
                reservation.setApprovalStatus(resultSet.getString("approvalStatus"));
                reservation.setStatus(resultSet.getString("status"));
                reservation.setUserId(resultSet.getInt("userId"));
                reservation.setUserName(resultSet.getString("userName"));
                reservation.setUserEmail(resultSet.getString("userEmail"));
                reservation.setAvatarUrl(resultSet.getString("avatarUrl"));

                reservations.add(reservation);
            }
    	  } catch (Exception e) {
    	    e.printStackTrace();
    	    return null;
    	  }

    	  return reservations; // Assuming reservations list is populated within the try block
    	}
    
    // Method to check if an email already exists in the system
    public static boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";

        try {
            Connection connection = DatabaseUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if there is at least one user with the given email
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true;  // Email exists
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception, log error, etc.
        }
        return false; // Email does not exist or an error occurred
    }
}



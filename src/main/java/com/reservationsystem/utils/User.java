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
    private String avatarUrl;

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
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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
        String query = "SELECT reservation.reservationId, reservation.date, reservation.approvalStatus, "
                + "reservation.startTime, reservation.endTime, room.roomName, restauranttable.tableName "
                + "FROM reservation "
                + "JOIN restauranttable ON reservation.tableId = restauranttable.tableId "
                + "JOIN room ON restauranttable.RoomId = room.roomId "
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
                reservation.setId(resultSet.getInt("reservationId"));
                reservation.setDate(resultSet.getDate("date"));
                reservation.setStartTime(resultSet.getString("startTime"));
                reservation.setEndTime(resultSet.getString("endTime"));
                reservation.setTableName(resultSet.getString("tableName"));
                reservation.setRoom(resultSet.getString("roomName"));
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
}



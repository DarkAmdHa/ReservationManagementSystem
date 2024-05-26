package com.reservationsystem.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Reservation {
    private int id;
    private Date date;
    private String startTime;
    private String endTime;
    private String status;
    private Timestamp cancelledAt;
    private String cancellationReason;
    private int tableId;
    private String tableName;
    private String tableCapacity;
    private int roomId;
    private String roomName;
    private String approvalStatus;
    private String notes;
    private int userId;
    private String userName;
    private String userEmail;
    private String avatarUrl;

    // Constructors
    public Reservation() {
        // Default constructor
    }

    public Reservation(int id, Date date, String startTime, String endTime, String tableName, String tableCapacity, String roomName, String approvalStatus, String notes) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tableName = tableName;
        this.tableCapacity = tableCapacity;
        this.roomName = roomName;
        this.approvalStatus = approvalStatus;
        this.notes = notes;
    }

    public Reservation(int id, Date date, String startTime, String endTime,String status,Timestamp cancelledAt,String cancellationReason,  String tableName, String tableCapacity, String roomName, String approvalStatus, String notes) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.cancelledAt = cancelledAt;
        this.cancellationReason = cancellationReason;
        this.tableName = tableName;
        this.tableCapacity = tableCapacity;
        this.roomName = roomName;
        this.approvalStatus = approvalStatus;
        this.notes = notes;
    }

    

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
    

    public Timestamp getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Timestamp cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
    
    public int getTableId() {
    	return tableId;
    }
    
    
    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getTableCapacity() {
    	return tableCapacity;
    }
    
    
    public void setTableCapacity(String tableCapacity) {
        this.tableCapacity = tableCapacity;
    }
    
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    
    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserEmail() {
return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public String getAvatarUrl() {
return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
 // Fetch reservation details by ID
    public static Reservation getReservationById(int reservationId) {
        Reservation reservation = null;

        String query = "SELECT r.*, rm.name as roomName, rt.name as tableName, rt.seatsCapacity as tableCapacity " +
        		"FROM reservation r " +
        		"JOIN restauranttable rt ON r.tableId = rt.id " +
        		"JOIN room rm ON rt.roomId = rm.id " + 
        		"WHERE r.id = ? ";
        
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, reservationId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    Date date = resultSet.getDate("date");
                    String startTime = resultSet.getString("startTime");
                    String endTime = resultSet.getString("endTime");
                    String status = resultSet.getString("status");
                    Timestamp cancelledAt = resultSet.getTimestamp("cancelled_at");
                    String cancellationReason = resultSet.getString("cancellation_reason");
                    String tableName = resultSet.getString("tableName");
                    String tableCapacity = resultSet.getString("tableCapacity");
                    String roomName = resultSet.getString("roomName");
                    String approvalStatus = resultSet.getString("approvalStatus");
                    String notes = resultSet.getString("notes");

                    reservation = new Reservation(id, date, startTime, endTime,status,cancelledAt,cancellationReason, tableName,tableCapacity, roomName, approvalStatus, notes);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        }

        return reservation;
    }
    
}

package com.reservationsystem.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Reservation {
    private int id;
    private Date date;
    private String startTime;
    private String endTime;
    private String tableName;
    private String room;
    private String approvalStatus;
    private String notes;

    // Constructors
    public Reservation() {
        // Default constructor
    }

    public Reservation(int id, Date date, String startTime, String endTime, String tableName, String room, String approvalStatus, String notes) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tableName = tableName;
        this.room = room;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
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
    
 // Fetch reservation details by ID
    public static Reservation getReservationById(int reservationId) {
        Reservation reservation = null;

        String query = "SELECT r.*, rt.name as roomName, rt.name as tableName" +
        		"FROM reservation r" +
        		"JOIN restauranttable rt ON r.tableId = rt.id" +
        		"JOIN room rm ON rt.roomId = rm.id" + 
        		"WHERE r.id = ?";
        
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, reservationId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    Date date = resultSet.getDate("date");
                    String startTime = resultSet.getString("startTime");
                    String endTime = resultSet.getString("endTime");
                    String tableName = resultSet.getString("tableName");
                    String room = resultSet.getString("room");
                    String approvalStatus = resultSet.getString("approvalStatus");
                    String notes = resultSet.getString("notes");

                    reservation = new Reservation(id, date, startTime, endTime, tableName, room, approvalStatus, notes);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        }

        return reservation;
    }
    
}

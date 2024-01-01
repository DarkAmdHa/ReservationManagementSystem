package com.reservationsystem.utils;

import java.sql.Date;

public class Reservation {
    private int id;
    private Date date;
    private String startTime;
    private String endTime;
    private String tableName;
    private String room;
    private String approvalStatus;

    // Constructors
    public Reservation() {
        // Default constructor
    }

    public Reservation(int id, Date date, String startTime, String endTime, String tableName, String room, String approvalStatus) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tableName = tableName;
        this.room = room;
        this.approvalStatus = approvalStatus;
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
    
}

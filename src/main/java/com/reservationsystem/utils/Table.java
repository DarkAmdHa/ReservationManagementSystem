package com.reservationsystem.utils;

public class Table {
    private int id;
    private String name;
    private String capacity;
    private int roomId;
    private String roomName;
    
    public Table(int id, String name, String capacity, int roomId, String roomName) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.roomId = roomId;
        this.roomName = roomName;
    }
    
    // Getter for id
    public int getId() {
        return id;
    }

    // Getter for name
    public String getName() {
        return name;
    }
    
    // Getter for capacity
    public String getCapacity() {
        return capacity;
    }
    
    // Getter for roomId
    public int getRoomId() {
        return roomId;
    }
    
    // Getter for roomName
    public String getRoomName() {
        return roomName;
    }
}


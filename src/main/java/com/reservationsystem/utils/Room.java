package com.reservationsystem.utils;


public class Room {
  
    private int id;
    private String name;
    
    public Room(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Getter for id
    public int getId() {
        return id;
    }

    // Getter for roomName
    public String getName() {
        return name;
    }
}

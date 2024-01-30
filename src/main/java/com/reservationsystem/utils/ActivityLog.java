package com.reservationsystem.utils;

import java.sql.Timestamp;

public class ActivityLog {

    private Timestamp date;
    private String action;
    private String email;
    private String role;
    private String avatarUrl;
    private String name;
    private String actionComment;

    public ActivityLog(Timestamp date, String action) {
        this.date = date;
        this.action = action;
    }
    
    public ActivityLog(Timestamp date, String action, String email, String role, String avatarUrl, String name, String actionComment) {
        this.date = date;
        this.action = action;
        this.email = email;
        this.role = role;
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.actionComment = actionComment;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getAction() {
        return action;
    }
    
    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getName() {
        return name;
    }
    public String getActionComment() {
        return actionComment;
    }
}

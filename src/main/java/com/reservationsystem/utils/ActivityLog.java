package com.reservationsystem.utils;

import java.sql.Timestamp;

public class ActivityLog {

    private Timestamp date;
    private String action;

    public ActivityLog(Timestamp date, String action) {
        this.date = date;
        this.action = action;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getAction() {
        return action;
    }
}

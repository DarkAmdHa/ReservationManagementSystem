package com.reservationsystem.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.reservationsystem.utils.DatabaseUtils;
import com.reservationsystem.utils.User;

public class LogUtils {

    public static void logUserLogin(User user) {
        log(user.getId(), ActionType.LOGGED_IN);
    }

    public static void logUserLogout(User user) {
        log(user.getId(), ActionType.LOGGED_OUT);
    }

    public static void logReservationCreation(User user) {
        log(user.getId(), ActionType.MADE_RESERVATION);
    }

    public static void logReservationEdit(User user) {
        log(user.getId(), ActionType.EDITED_RESERVATION);
    }
    
    public static void logReservationCancellation(User user) {
        log(user.getId(), ActionType.CANCELLED_RESERVATION);
    }
    

    public static void logReservationDeletion(User user) {
        log(user.getId(), ActionType.DELETED_RESERVATION);
    }

    public static void logProfileUpdate(User user) {
        log(user.getId(), ActionType.UPDATED_PROFILE);
    }
    
    public static void logTableCreated(User user, String detail) {
        log(user.getId(), ActionType.CREATED_TABLE,detail);
    }
    
    public static void logTableUpdated(User user, String detail) {
        log(user.getId(), ActionType.UPDATED_TABLE, detail);
    }
    
    public static void logTableDeletion(User user, String detail) {
        log(user.getId(), ActionType.DELETED_TABLE, detail);
    }
    
    public static void logRoomCreated(User user, String detail) {
        log(user.getId(), ActionType.CREATED_ROOM,detail);
    }
    
    public static void logRoomUpdated(User user, String detail) {
        log(user.getId(), ActionType.UPDATED_ROOM, detail);
    }
    
    public static void logRoomDeletion(User user, String detail) {
        log(user.getId(), ActionType.DELETED_ROOM, detail);
    }
    
    
    public static void Room(User user) {
        log(user.getId(), ActionType.UPDATED_ROOM);
    }
    
    private static void log(int userId, ActionType actionType) {
        log(userId, actionType, "");
    }

    private static void log(int userId, ActionType actionType,String detail) {
        String query = "INSERT INTO log (userId, actionTime, actionDescription, actionComment) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setTimestamp(2, getCurrentTimestamp());
            preparedStatement.setString(3, actionType.name());
            preparedStatement.setString(4, detail);
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        }
    }

    private static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    public static void logRoleUpdate(User user, String detail) {
        log(user.getId(), ActionType.UPDATED_ROLE,detail);
    }

    public static void logActivationStatusUpdate(User user, String detail) {
        log(user.getId(), ActionType.UPDATED_ACTIVATION_STATUS, detail);
    }

    public static void logReservationAcceptance(User user, String detail) {
        log(user.getId(), ActionType.ACCEPTED_RESERVATION, detail);
    }

    public static void logReservationRejection(User user, String detail) {
        log(user.getId(), ActionType.REJECTED_RESERVATION, detail);
    }

    private enum ActionType {
        LOGGED_IN,
        LOGGED_OUT,
        MADE_RESERVATION,
        EDITED_RESERVATION,
        CANCELLED_RESERVATION,
        DELETED_RESERVATION,
        UPDATED_PROFILE,
        UPDATED_ROLE,
        UPDATED_ACTIVATION_STATUS,
        ACCEPTED_RESERVATION,
        REJECTED_RESERVATION,
        CREATED_TABLE,
        UPDATED_TABLE,
        DELETED_TABLE,
        CREATED_ROOM,
        UPDATED_ROOM,
        DELETED_ROOM
    }

   

    public static List<ActivityLog> getUserActivityLog(int userId) {
        List<ActivityLog> activityLogList = new ArrayList<>();

        String query = "SELECT actionTime, actionDescription FROM log WHERE userId = ? ORDER BY actionTime DESC";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Timestamp date = resultSet.getTimestamp("actionTime");
                    String action = resultSet.getString("actionDescription");

                    ActivityLog activityLog = new ActivityLog(date, action);
                    activityLogList.add(activityLog);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        }

        return activityLogList;
    }
    
    
    public static List<ActivityLog> getAllActivityLogs() {
        List<ActivityLog> activityLogList = new ArrayList<>();

        String query = "SELECT actionTime, actionDescription, actionComment, user.email as email, user.name as name, user.avatarUrl as avatarUrl, user.role as role FROM log LEFT JOIN user ON log.userId = user.id ORDER BY actionTime DESC";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Timestamp date = resultSet.getTimestamp("actionTime");
                String action = resultSet.getString("actionDescription");
                String actionComment = resultSet.getString("actionComment");
                String email = resultSet.getString("email");
                String avatarUrl = resultSet.getString("avatarUrl");
                String role = resultSet.getString("role");
                String name = resultSet.getString("name");
                
                ActivityLog activityLog = new ActivityLog(date, action,email,avatarUrl,role, name,actionComment);
                activityLogList.add(activityLog);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        }

        return activityLogList;
    }
}

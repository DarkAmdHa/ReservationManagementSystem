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

    public static void logReservationDeletion(User user) {
        log(user.getId(), ActionType.DELETED_RESERVATION);
    }

    public static void logProfileUpdate(User user) {
        log(user.getId(), ActionType.UPDATED_PROFILE);
    }

    private static void log(int userId, ActionType actionType) {
        String query = "INSERT INTO log (userId, actionTime, actionDescription) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        	System.out.println("asdasdasd" + actionType.name());
            preparedStatement.setInt(1, userId);
            preparedStatement.setTimestamp(2, getCurrentTimestamp());
            preparedStatement.setString(3, actionType.name());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        }
    }

    private static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    private enum ActionType {
        LOGGED_IN,
        LOGGED_OUT,
        MADE_RESERVATION,
        EDITED_RESERVATION,
        DELETED_RESERVATION,
        UPDATED_PROFILE
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
}

import com.google.gson.Gson;
import com.reservationsystem.utils.DatabaseUtils;
import com.reservationsystem.utils.Table;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/GetTablesServlet")
public class GetTablesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the selected date, start time, and end time from the request parameters
        String selectedDate = request.getParameter("date");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        // Check if any of the parameters is null or empty
        if (selectedDate == null || selectedDate.isEmpty() ||
            startTime == null || startTime.isEmpty() ||
            endTime == null || endTime.isEmpty()) {
            // Send an error response with HTTP status code 400 (Bad Request)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Date, startTime, and endTime parameters are required");
            return;
        }

        // Fetch tables based on the selected date, start time, and end time from the database
        List<Table> tables = getTablesForDateTimeFromDatabase(selectedDate, startTime, endTime);

        // Convert the list of tables to JSON
        String jsonTables = new Gson().toJson(tables);

        // Set content type and character encoding
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Send the JSON response
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonTables);
            out.flush();
        }
    }

    private List<Table> getTablesForDateTimeFromDatabase(String selectedDate, String startTime, String endTime) {
        // Perform database query to fetch table data for the specified date, start time, and end time
        List<Table> tables = new ArrayList<>();

        // Use a prepared statement with a parameterized query
        String query = "SELECT rt.*, ro.roomName " +
                "FROM restauranttable rt " +
                "LEFT JOIN reservation r ON rt.tableId = r.tableId " +
                "LEFT JOIN room ro ON rt.RoomId = ro.roomId " +
                "WHERE (" +
                "    (r.startTime IS NULL OR r.endTime IS NULL) " +
                "    OR (" +
                "        r.date = ? " +
                "        AND (" +
                "            r.approvalStatus IS NULL " +
                "            OR r.approvalStatus != 'APPROVED' " +
                "            OR NOT (" +
                "                (r.startTime > ? AND r.startTime < ?) OR " +
                "                (r.endTime > ? AND r.endTime < ?) OR " +
                "                (? > r.startTime AND ? < r.endTime) OR " +
                "                (? > r.startTime AND ? < r.endTime) " +
                "            )" +
                "        )" +
                "    )" +
                "    OR r.date != ? " +
                "    OR r.date IS NULL " +
                "    OR r.approvalStatus IS NULL " +
                "    OR r.approvalStatus != 'APPROVED'" +
                ");";

        
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters for the prepared statement
        	preparedStatement.setString(1, selectedDate);
        	preparedStatement.setString(2, startTime);
        	preparedStatement.setString(3, endTime);
        	preparedStatement.setString(4, startTime);
        	preparedStatement.setString(5, endTime);
            preparedStatement.setString(6, startTime);
            preparedStatement.setString(7, endTime);
            preparedStatement.setString(8, startTime);
            preparedStatement.setString(9, endTime);
            preparedStatement.setString(10, selectedDate);
            

            // Execute the query and process the results
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("tableId");
                    String name = resultSet.getString("tableName");
                    String capacity = resultSet.getString("seatsCapacity");
                    int roomId = resultSet.getInt("RoomId");
                    String roomName = resultSet.getString("roomName");
                    Table table = new Table(id, name, capacity, roomId, roomName);
                    tables.add(table);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        }

        return tables;
    }
}


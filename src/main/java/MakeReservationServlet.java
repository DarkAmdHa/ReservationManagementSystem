

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.reservationsystem.utils.DatabaseUtils;
import com.reservationsystem.utils.Room;
import com.reservationsystem.utils.Table;
/**
 * Servlet implementation class MakeReservationServlet
 */
public class MakeReservationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MakeReservationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch rooms from the database
        List<Room> rooms = getRoomsFromDatabase();
        
        

        // Fetch tables for the first room from the database
        List<Table> tablesForFirstRoom = getTablesForRoomFromDatabase(rooms.get(0).getId());

        // put retrieved data as attributes in the request
        request.setAttribute("rooms", rooms);
        request.setAttribute("tables", tablesForFirstRoom);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/makeReservation.jsp");
        dispatcher.forward(request, response);
    }
    
    private List<Table> getTablesForRoomFromDatabase(int roomId) {
        // Perform database query to fetch table data for the specified room
        List<Table> tables = new ArrayList<>();

        // Use a prepared statement with a parameterized query
        String query = "SELECT * FROM restauranttable WHERE RoomId = ?";
        
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameter (roomId) for the prepared statement
            preparedStatement.setString(1, Integer.toString(roomId));

            // Execute the query and process the results
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("tableId");
                    String name = resultSet.getString("tableName");
                    String capacity = resultSet.getString("seatsCapacity");
                    Table table = new Table(id,name,capacity,roomId, "");
                    tables.add(table);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        }

        return tables;
    }
    
    // Example usage in the servlet method
    private List<Room> getRoomsFromDatabase() {
        List<Room> rooms = new ArrayList<>();

        String query = "SELECT roomId, roomName FROM room";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("roomId");
                String name = resultSet.getString("roomName");
                Room room = new Room(id, name);
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        }

        return rooms;
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

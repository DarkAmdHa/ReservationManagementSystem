

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.reservationsystem.utils.Reservation;

@WebServlet("/ViewReservationDetails")
public class ViewReservationDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the reservation ID from the request parameters
        String reservationIdStr = request.getParameter("reservationId");

        // Check if the reservationId parameter is present
        if (reservationIdStr != null && !reservationIdStr.isEmpty()) {
            try {
                // Convert the reservationId parameter to an integer
                int reservationId = Integer.parseInt(reservationIdStr);

                // Fetch the reservation details using the reservationId
                Reservation reservation = fetchReservationDetails(reservationId);

                // Set the reservation details as a request attribute
                request.setAttribute("reservation", reservation);

                // Forward to the JSP for displaying reservation details
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/viewReservationDetails.jsp");
                dispatcher.forward(request, response);
            } catch (NumberFormatException e) {
                // Handle invalid reservationId parameter
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid reservationId parameter");
            }
        } else {
            // Handle missing reservationId parameter
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing reservationId parameter");
        }
    }

    private Reservation fetchReservationDetails(int reservationId) {
        // Implement the logic to fetch reservation details from the database
        // You can use your existing methods or create a new one based on your database structure
        // Return the fetched Reservation object
        // Example: return ReservationDAO.getReservationById(reservationId);
        return null;
    }
}
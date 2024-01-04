

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.reservationsystem.utils.Reservation;
import com.reservationsystem.utils.User;

@WebServlet("/ViewReservationDetailsServlet")
public class ViewReservationDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


	   	 HttpSession session = request.getSession();
	     // Check if the user is already logged in
	     if (session.getAttribute("user") == null) {
	    	 //If so,redirect
	    	 response.sendRedirect(request.getContextPath() + "/LoginServlet?notLoggedIn=true");
	    	 return;
	     }
	      // Retrieve user information from the session
         User user = (User) session.getAttribute("user");
     
	     
	     // Set user information as request attributes
	     request.setAttribute("userName", user.getName());
	     request.setAttribute("userEmail", user.getEmail());
	     request.setAttribute("userId", user.getId());
	     request.setAttribute("userAvatarUrl", user.getAvatarUrl());
     
        // Retrieve the reservation ID from the request parameters
        String reservationIdStr = request.getParameter("id");

        // Check if the reservationId parameter is present
        if (reservationIdStr != null && !reservationIdStr.isEmpty()) {
            try {
                // Convert the reservationId parameter to an integer
                int reservationId = Integer.parseInt(reservationIdStr);

                // Fetch the reservation details using the reservationId
                Reservation reservation = Reservation.getReservationById(reservationId);

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


}
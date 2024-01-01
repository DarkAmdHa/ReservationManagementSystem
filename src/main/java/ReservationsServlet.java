

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import com.reservationsystem.utils.Reservation;
import com.reservationsystem.utils.User;

/**
 * Servlet implementation class ReservationsServlet
 */
public class ReservationsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReservationsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 HttpSession session = request.getSession();
         // Check if the user is already logged in
         if (session.getAttribute("user") == null) {
        	 //If so,redirect
        	 response.sendRedirect(request.getContextPath() + "/LoginServlet?notLoggedIn=true");
        	 return;
         }
         
         
         
      // Retrieve user information from the session
         User user = (User) session.getAttribute("user");
         int userId = user.getId();
         
         // Retrieve reservations for the user
         List<Reservation> userReservations = user.getUserReservations();

         // Set user information as request attributes
         request.setAttribute("userName", user.getName());
         request.setAttribute("userEmail", user.getEmail());
         request.setAttribute("userId", userId);
         request.setAttribute("userAvatarUrl", user.getAvatarUrl());
         request.setAttribute("userReservations", userReservations);

         RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/reservations.jsp");
         dispatcher.forward(request, response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.reservationsystem.utils.Reservation;
import com.reservationsystem.utils.User;

@WebServlet("/PendingReservationsServlet")
public class PendingReservationsServlet extends HttpServlet {
    int PAGE_SIZE = 6;
    private static final long serialVersionUID = 1L;

    public PendingReservationsServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Check if the user is logged in
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet?notLoggedIn=true");
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getId();
        String userRole = user.getRole();

        if (userRole.equals("NORMAL")) {
            response.sendRedirect(request.getContextPath() + "/ReservationsServlet");
            return;
        }

        String searchBy = request.getParameter("searchBy");
        String searchTerm = request.getParameter("searchTerm");
        
        
        // Retrieve reservations for the user
        List<Reservation> pendingReservations;
        if (searchBy != null && searchTerm != null) {
        	pendingReservations = user.searchPendingReservations(searchBy, searchTerm);
        } else {
        	pendingReservations = user.getPendingUserReservations();
        }


        // Handle case with no pending reservations
        if (pendingReservations == null ) {
            request.setAttribute("noPendingReservationsMessage", "No pending reservations at the moment.");
        } else {
            // Pagination logic
            String pageStr = request.getParameter("page");
            int currentPage = pageStr != null ? Integer.parseInt(pageStr) : 1;
            int startIndex = (currentPage - 1) * PAGE_SIZE;
            int endIndex = Math.min(startIndex + PAGE_SIZE, pendingReservations.size());

            // Extract the sublist for the current page
            List<Reservation> reservationsPage = pendingReservations.subList(startIndex, endIndex);

            request.setAttribute("pendingReservations", reservationsPage);
            // Pagination information
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", (int) Math.ceil((double) pendingReservations.size() / PAGE_SIZE));
        }

        // Set user information as request attributes
        request.setAttribute("userName", user.getName());
        request.setAttribute("userEmail", user.getEmail());
        request.setAttribute("userRole", user.getRole());
        request.setAttribute("userId", userId);
        request.setAttribute("userAvatarUrl", user.getAvatarUrl());
        
        request.setAttribute("searchBy", searchBy);
        request.setAttribute("searchTerm", searchTerm);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pendingReservations.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

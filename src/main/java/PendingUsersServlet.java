import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.reservationsystem.utils.DatabaseUtils;
import com.reservationsystem.utils.Reservation;
import com.reservationsystem.utils.User;

@WebServlet("/PendingUsersServlet")
public class PendingUsersServlet extends HttpServlet {
    int PAGE_SIZE = 6;
    private static final long serialVersionUID = 1L;

    public PendingUsersServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet?notLoggedIn=true");
            return;
        }

        User user = (User) session.getAttribute("user");
        String userRole = user.getRole();

        if (userRole.equals("NORMAL")) {
            response.sendRedirect(request.getContextPath() + "/ReservationsServlet");
            return;
        }

        List<User> pendingUsers = getPendingUsers();
        
        // Handle case with no pending reservations
        if (pendingUsers.isEmpty()) {
            request.setAttribute("noPendingUsersMessage", "No pending users at the moment.");
        } else {
            // Pagination logic
            String pageStr = request.getParameter("page");
            int currentPage = pageStr != null ? Integer.parseInt(pageStr) : 1;
            int startIndex = (currentPage - 1) * PAGE_SIZE;
            int endIndex = Math.min(startIndex + PAGE_SIZE, pendingUsers.size());

            // Extract the sublist for the current page
            List<User> usersPage = pendingUsers.subList(startIndex, endIndex);

            request.setAttribute("pendingUsers", usersPage);
            // Pagination information
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", (int) Math.ceil((double) pendingUsers.size() / PAGE_SIZE));
        }

        // Set user information as request attributes
        request.setAttribute("userName", user.getName());
        request.setAttribute("userEmail", user.getEmail());
        request.setAttribute("userRole", user.getRole());
        request.setAttribute("userId", user.getId());
        request.setAttribute("userAvatarUrl", user.getAvatarUrl());

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pendingUsers.jsp");
        dispatcher.forward(request, response);
    }

    private List<User> getPendingUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user WHERE isActive = 0 ORDER BY id DESC"; 

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                int isActive = resultSet.getInt("isActive");
                int isDeactivated = resultSet.getInt("isDeactivated");
                String avatarUrl = resultSet.getString("avatarUrl");

                User user = new User();
                user.setId(id);
                user.setEmail(email);
                user.setName(name);
                user.setIsActive(isActive);
                user.setIsDeactivated(isDeactivated);
                user.setAvatarUrl(avatarUrl);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
        return users;
    }


}

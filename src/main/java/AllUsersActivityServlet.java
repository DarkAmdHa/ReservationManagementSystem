import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import com.reservationsystem.utils.ActivityLog;
import com.reservationsystem.utils.LogUtils;
import com.reservationsystem.utils.User;

/**
 * Servlet implementation class AllActivityLogServlet
 */
@WebServlet("/AllUsersActivityServlet")
public class AllUsersActivityServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    int PAGE_SIZE = 10;

    public AllUsersActivityServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet?notLoggedIn=true");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Check if the user has admin privileges
        if ("NORMAL".equals(user.getRole())) {
            // Redirect or show an unauthorized access message
            response.sendRedirect(request.getContextPath() + "/ReservationsServlet");
            return;
        }

        // Set user information as request attributes
        request.setAttribute("userName", user.getName());
        request.setAttribute("userEmail", user.getEmail());
        request.setAttribute("userId", user.getId());
        request.setAttribute("userRole", user.getRole());
        request.setAttribute("userAvatarUrl", user.getAvatarUrl());

        // Assuming that LogUtils has a method to get all activity logs
        List<ActivityLog> allActivityLog = LogUtils.getAllActivityLogs();

        // Pagination logic
        String pageStr = request.getParameter("page");
        int currentPage = pageStr != null ? Integer.parseInt(pageStr) : 1;
        int startIndex = (currentPage - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, allActivityLog.size());

        // Extract the sublist for the current page
        List<ActivityLog> paginatedActivityLog = allActivityLog.subList(startIndex, endIndex);

        request.setAttribute("activityLog", paginatedActivityLog);
        // Pagination information
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", (int) Math.ceil((double) allActivityLog.size() / PAGE_SIZE));

        request.getRequestDispatcher("/WEB-INF/allActivityLog.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}



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
import com.reservationsystem.utils.Reservation;
import com.reservationsystem.utils.User;

/**
 * Servlet implementation class ActivityLogServlet
 */
public class ActivityLogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int PAGE_SIZE = 10;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActivityLogServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the user's session
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

        // Set user information as request attributes
        request.setAttribute("userName", user.getName());
        request.setAttribute("userEmail", user.getEmail());
        request.setAttribute("userId", userId);
        request.setAttribute("userAvatarUrl", user.getAvatarUrl());

        // Assuming that LogUtils has a method to get the user's activity log
        List<ActivityLog> activityLog = LogUtils.getUserActivityLog(user.getId());
        
        // Pagination logic
        String pageStr = request.getParameter("page");
        int currentPage = pageStr != null ? Integer.parseInt(pageStr) : 1;
        int startIndex = (currentPage - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, activityLog.size());


        // Extract the sublist for the current page
        List<ActivityLog> paginatedActivityLog = activityLog.subList(startIndex, endIndex);


        

        request.setAttribute("activityLog", paginatedActivityLog);
        // Pagination information
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", (int) Math.ceil((double) activityLog.size() / PAGE_SIZE));
    	
        request.getRequestDispatcher("/WEB-INF/activityLog.jsp").forward(request, response);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

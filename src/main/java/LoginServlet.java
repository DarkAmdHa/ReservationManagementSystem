import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.reservationsystem.utils.User;
import com.reservationsystem.utils.LoginResult;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	 HttpSession session = request.getSession();
         // Check if the user is already logged in
         if (session.getAttribute("user") != null) {
        	 //If so,redirect
        	 response.sendRedirect(request.getContextPath() + "/ReservationsServlet");
        	 return;
         }
         
         
    	// Forward to the login page
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        HttpSession session = request.getSession();
        // Check if the user is already logged in
        if (session.getAttribute("user") != null) {
            JsonObject alreadyLoggedInResponse = new JsonObject();
            alreadyLoggedInResponse.addProperty("loginResult", "ALREADY_LOGGED_IN");
            alreadyLoggedInResponse.addProperty("message", "User is already logged in");

            response.setContentType("application/json");
            response.getWriter().write(alreadyLoggedInResponse.toString());
            return;
        }
        
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        // Validate the inputs
        if (isValidInput(email, password)) {
            // Check additional server-side validations
            if (isEmailValid(email) && isPasswordValid(password)) {
                
                LoginResult loginResult = User.loginUser(email, password);
                switch (loginResult) {
                    case SUCCESS:
                        // Set a session attribute to indicate the user is logged in
                        session.setAttribute("user", User.getUserByEmail(email));

                        JsonObject successResponse = new JsonObject();
                        successResponse.addProperty("loginResult", "SUCCESS");
                        successResponse.addProperty("message", "Login successful");

                        response.setContentType("application/json");
                        response.getWriter().write(successResponse.toString());
                        break;
                    case INVALID_CREDENTIALS:
                        // Login failed, invalid credentials
                        JsonObject userNotFoundResponse = new JsonObject();
                        userNotFoundResponse.addProperty("loginResult", "INVALID_CREDENTIALS");
                        userNotFoundResponse.addProperty("message", "Invalid email or password");

                        response.setContentType("application/json");
                        response.getWriter().write(userNotFoundResponse.toString());
                        break;
                    case NOT_ACTIVE_USER:
                        // Login failed, invalid credentials
                        JsonObject notActiveResponse = new JsonObject();
                        notActiveResponse.addProperty("loginResult", "NOT_ACTIVE_USER");
                        notActiveResponse.addProperty("message", "Your account has not been activated by our admins yet. You will be notified once the account is activated. Thank you");

                        response.setContentType("application/json");
                        response.getWriter().write(notActiveResponse.toString());
                        break;
                    case ERROR:
                        // Login failed due to an error
                        JsonObject errorResponse = new JsonObject();
                        errorResponse.addProperty("loginResult", "ERROR");
                        errorResponse.addProperty("message", "Login error");

                        response.setContentType("application/json");
                        response.getWriter().write(errorResponse.toString());
                        break;
                }
            } else {
                // Invalid inputs based on additional server-side validations
                JsonObject invalidInputResponse = new JsonObject();
                invalidInputResponse.addProperty("loginResult", "INVALID_INPUTS");
                invalidInputResponse.addProperty("message", "Invalid inputs based on server-side validations");

                response.setContentType("application/json");
                response.getWriter().write(invalidInputResponse.toString());
            }
        } else {
            JsonObject invalidInputResponse = new JsonObject();
            invalidInputResponse.addProperty("loginResult", "INVALID_INPUTS");
            invalidInputResponse.addProperty("message", "Invalid inputs");

            response.setContentType("application/json");
            response.getWriter().write(invalidInputResponse.toString());
        }
    }

    private boolean isValidInput(String email, String password) {
        return email != null && !email.isEmpty() && password != null && !password.isEmpty();
    }

    private boolean isEmailValid(String email) {
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    private boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$");
    }
}

import com.google.gson.JsonObject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.reservationsystem.utils.User;
import com.reservationsystem.utils.RegistrationResult;
import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 HttpSession session = request.getSession();
         // Check if the user is already logged in
         if (session.getAttribute("user") != null) {
        	 //If so,redirect
        	 response.sendRedirect(request.getContextPath() + "/ReservationsServlet");
        	 return;
         }
         
         
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/register.jsp");
        dispatcher.forward(request, response);
	}
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 String name = request.getParameter("name");
         String email = request.getParameter("email");
         String password = request.getParameter("password");

         System.out.println("Hello " + name + email + password);
        // Validate the inputs
         if (isValidInput(name, email, password)) {
        	 // Check additional server-side validations
             if (isNameValid(name) && isEmailValid(email) && isPasswordValid(password)) {
            	 User user = new User(name, email, password, "NORMAL", false);

                 
                 RegistrationResult registrationResult = User.registerUser(name,email,password);
                 switch (registrationResult) {
                 case SUCCESS:
                     // Set a session attribute to indicate the user is logged in
                     // User needs to be activated first, so commenting this out:
                	 //HttpSession session = request.getSession();
                	 //session.setAttribute("user", user);

                     JsonObject successResponse = new JsonObject();
                     successResponse.addProperty("registrationResult", "SUCCESS");
                     successResponse.addProperty("message", "Registration successful");


                     response.setContentType("application/json");
                     response.getWriter().write(successResponse.toString());
                     break;
                 case USER_ALREADY_EXISTS:
                     // Registration failed, user already exists
                     JsonObject userExistsResponse = new JsonObject();
                     userExistsResponse.addProperty("registrationResult", "USER_ALREADY_EXISTS");
                     userExistsResponse.addProperty("message", "User with this email already exists");

                     response.setContentType("application/json");
                     response.getWriter().write(userExistsResponse.toString());
                     break;
                 case ERROR:
                     // Registration failed due to an error
                	 JsonObject errorResponse = new JsonObject();
                	 errorResponse.addProperty("registrationResult", "ERROR");
                     errorResponse.addProperty("message", "Registration error");

                     response.setContentType("application/json");
                     response.getWriter().write(errorResponse.toString());
                     break;
                 }
             }else {
            	// Invalid inputs based on additional server-side validations
                 JsonObject invalidInputResponse = new JsonObject();
                 invalidInputResponse.addProperty("status", "error");
                 invalidInputResponse.addProperty("message", "Invalid inputs based on server-side validations");

                 response.setContentType("application/json");
                 response.getWriter().write(invalidInputResponse.toString());
             }
         } else {
             JsonObject invalidInputResponse = new JsonObject();
             invalidInputResponse.addProperty("status", "error");
             invalidInputResponse.addProperty("message", "Invalid inputs");

             response.setContentType("application/json");
             response.getWriter().write(invalidInputResponse.toString());
         }
    }
    
    private boolean isValidInput(String name, String email, String password) {
        return name != null && !name.isEmpty() && email != null && !email.isEmpty() && password != null && !password.isEmpty();
    }

    private boolean isNameValid(String name) {
        // Add your name validation logic here
        return name.length() > 1; // Example: Name should be more than 1 character
    }

    
    private boolean isEmailValid(String email) {
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    private boolean isPasswordValid(String password) {
        // Add your password validation logic here
        // Example: Password should have one uppercase character, one lowercase character, one digit, and one special character
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }
}


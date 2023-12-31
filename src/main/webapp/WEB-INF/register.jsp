<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/functions"
    prefix="fn" %>

    <h:header title="Register" bodyClasses="flex items-center justify-center min-h-screen bg-gray-200 min-w-screen gradient-overlay"/>
   <style>
      .gradient-overlay {
        background: linear-gradient(rgb(0 0 0 / 0%), rgb(0 102 68 / 29%)),
          url(https://picsum.photos/1200/768) center/cover no-repeat;
      }
    </style>
    <div class="flex w-full h-full">
      <div class="w-9/12 hidden md:flex gradient-overlay"></div>
      <div class="w-3/12 bg-white p-8 shadow-lg min-h-screen">
        <div class="flex justify-center">
          <!-- Replace the src attribute with your logo image URL -->
          <img
            src="https://via.placeholder.com/80"
            alt="Logo"
            class="mb-4"
            data-ilt="1703634975311"
          />
        </div>
        <h2 class="text-2xl font-semibold mb-4">Register</h2>
        <form id="registerForm">
          <div class="mb-4">
            <label for="name" class="block text-gray-600 text-sm font-medium"
              >Name:</label
            >
            <input
              type="text"
              id="name"
              name="name" required
              class="mt-1 p-2 w-full border rounded-md"
            />
          </div>
          <div class="mb-4">
            <label for="email" class="block text-gray-600 text-sm font-medium"
              >Email:</label
            >
            <input
              type="email"
              id="email"
              name="email" required
              class="mt-1 p-2 w-full border rounded-md"
            />
          </div>
          <div class="mb-4">
            <label
              for="password"
              class="block text-gray-600 text-sm font-medium"
              >Password:</label
            >
            <input
              type="password"
              id="password"
              name="password" required
              class="mt-1 p-2 w-full border rounded-md"
            />
          </div>
          <div class="mb-4">
            <label
              for="confirmPassword"
              class="block text-gray-600 text-sm font-medium"
              >Confirm Password:</label
            >
            <input
              type="password"
              id="confirmPassword" required
              name="confirmPassword"
              class="mt-1 p-2 w-full border rounded-md"
            />
          </div>
          <div id="errorContainer" class="text-red-500"></div>
          
          <button
            type="submit"
            class="bg-green-500 text-white px-4 py-2 rounded-md transition hover:bg-green-600"
          >
            Register
          </button>
        </form>
        <p class="mt-4 text-sm">
          Already have an account?
          <a
            href="/reservation_system/LoginServlet"
            class="text-green-500 transition hover:text-green-600 hover:underline"
            >Login here</a
          >.
        </p>
      </div>
    </div>

    <script>
    
    document.getElementById("registerForm").addEventListener("submit", function (event) {
        event.preventDefault();

        const form = event.target;

        // Client-side validation
        const name = form.querySelector("#name").value;
        const email = form.querySelector("#email").value;
        const password = form.querySelector("#password").value;
        const confirmPassword = form.querySelector("#confirmPassword").value;

        const errorMessages = [];

        // Validate name
        if (name.length < 2) {
            errorMessages.push("Name must be at least 2 characters");
        }

        // Validate email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            errorMessages.push("Invalid email address");
        }

        // Validate password: Must be 8 character, one uppercase, one lowercase and one digit
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        if (!passwordRegex.test(password)) {
            errorMessages.push("Password must have at least 8 characters, one uppercase letter, one lowercase letter, one digit, and one special character");
        }

        // Validate password match
        if (password !== confirmPassword) {
            errorMessages.push("Passwords do not match");
        }

        if (errorMessages.length > 0) {
            // Display error messages and prevent form submission
            displayErrorMessages(errorMessages);
            return;
        }else{
        	document.getElementById("errorContainer").innerHTML = '';
        }

        // If client-side validation passed, submit the form to the server
        fetch("/reservation_system/RegisterServlet", {
            method: "POST",
            body: new URLSearchParams({
                name,
                email,
                password,
              })
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error("Registration failed");
                }
            })
            .then(data => {
                // Check the response data for registration result
                handleServerResponse(data);
            })
            .catch(error => {
                // Handle registration failure, show a generic error message
                console.error("Registration error:", error);
            });
    });

    function handleServerResponse(data) {
        const errorContainer = document.getElementById("errorContainer");

        switch (data.registrationResult) {
            case "SUCCESS":
                console.log("Registration successful");
                window.location.href = "/reservation_system/ReservationsServlet";
                break;
            case "USER_ALREADY_EXISTS":
                console.error("User already exists");
                displayErrorMessages(["User with this email already exists"]);
                break;
            case "ERROR":
                console.error("Registration error");
                displayErrorMessages(["Registration error"]);
                break;
            case "INVALID_INPUTS":
                // Update error container with server-side validation errors
                displayErrorMessages(data.errorMessages);
                break;
            default:
                console.error("Unexpected registration result");
                displayErrorMessages(["Unexpected registration result"]);
                break;
        }
    }

    function displayErrorMessages(errorMessages) {
        const errorContainer = document.getElementById("errorContainer");
        errorContainer.innerHTML = ""; // Clear previous error messages

        if (errorMessages && errorMessages.length > 0) {
            const errorList = document.createElement("ul");
            errorList.classList.add('mb-4', 'list-disc')
            errorMessages.forEach(message => {
                const listItem = document.createElement("li");
                listItem.className = 'opacity-0 transition transform translate-y-2 duration-100';
                listItem.textContent = message;
                errorList.appendChild(listItem);
            });
            errorContainer.appendChild(errorList);
            setTimeout(() => {
                errorContainer.querySelectorAll('li').forEach(listItem => listItem.className = 'opacity-1 transition transform translate-y-0 duration-100')

            }, 100)
        }
    }

    </script>
   <h:footer/>
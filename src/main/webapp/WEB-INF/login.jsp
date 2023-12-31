<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<h:header title="Login" bodyClasses="flex items-center justify-center h-screen bg-gray-200"/>

<div class="bg-white p-8 rounded shadow-md w-96">
  <h2 class="text-2xl font-semibold mb-6">Login</h2>
  <form id="loginForm">
    <div class="mb-4">
      <label for="email" class="block text-gray-600 text-sm font-medium">Email:</label>
      <input type="email" id="email" name="email" class="mt-1 p-2 w-full border rounded-md"/>
    </div>
    <div class="mb-4">
      <label for="password" class="block text-gray-600 text-sm font-medium">Password:</label>
      <input type="password" id="password" name="password" class="mt-1 p-2 w-full border rounded-md"/>
    </div>
    <div id="errorContainer" class="text-red-500"></div>
    <button type="submit" class="bg-green-500 text-white px-4 py-2 rounded-md">Login</button>
  </form>
  <p class="mt-4 text-sm">
    Don't have an account?
    <a href="/reservation_system/RegisterServlet" class="text-green-500">Register here</a>.
  </p>
</div>

<script>
document.getElementById("loginForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const form = event.target;

    const email = form.querySelector("#email").value;
    const password = form.querySelector("#password").value;

    const errorMessages = [];

    // Validate email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        errorMessages.push("Invalid email address");
    }

    // Validate password: Must be 8 characters, one uppercase, one lowercase, and one digit
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,}$/;
    if (!passwordRegex.test(password)) {
        errorMessages.push("Password must have at least 8 characters, one uppercase letter, one lowercase letter, one digit, and one special character");
    }

    if (errorMessages.length > 0) {
        displayErrorMessages(errorMessages);
        return;
    } else {
        document.getElementById("errorContainer").innerHTML = '';
    }

    // If client-side validation passed, submit the form to the server
    fetch("/reservation_system/LoginServlet", {
        method: "POST",
        body: new URLSearchParams({
            email,
            password,
        })
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Login failed");
        }
    })
    .then(data => {
        // Check the response data for login result
        handleServerResponse(data);
    })
    .catch(error => {
        // Handle login failure, show a generic error message
        console.error("Login error:", error);
    });
});

function handleServerResponse(data) {
    const errorContainer = document.getElementById("errorContainer");

    switch (data.loginResult) {
        case "SUCCESS":
            console.log("Login successful");
            window.location.href = "/reservation_system/ReservationsServlet";
            break;
        case "INVALID_CREDENTIALS":
            console.error(data.message);
            displayErrorMessages([data.message]);
            break;
        case "NOT_ACTIVE_USER":
            console.error(data.message);
            displayErrorMessages([data.message]);
            break;
        case "ERROR":
            console.error("Login error");
            displayErrorMessages(["Login error"]);
            break;

        default:
            console.error("Unexpected login result");
            displayErrorMessages(["Unexpected login result"]);
            break;
    }
}

function displayErrorMessages(errorMessages) {
    const errorContainer = document.getElementById("errorContainer");
    errorContainer.innerHTML = "";

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

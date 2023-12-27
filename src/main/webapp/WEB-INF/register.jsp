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
              name="name"
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
              name="email"
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
              name="password"
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
              id="confirmPassword"
              name="confirmPassword"
              class="mt-1 p-2 w-full border rounded-md"
            />
          </div>
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
            href="login.html"
            class="text-green-500 transition hover:text-green-600 hover:underline"
            >Login here</a
          >.
        </p>
      </div>
    </div>

    <script src="register.js"></script>
   <h:footer/>
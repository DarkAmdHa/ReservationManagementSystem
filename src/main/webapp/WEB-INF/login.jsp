<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/functions"
    prefix="fn" %>

    <h:header title="Login" bodyClasses="flex items-center justify-center h-screen bg-gray-200"/>
        
        
    <div class="bg-white p-8 rounded shadow-md w-96">
      <h2 class="text-2xl font-semibold mb-6">Login</h2>
      <form id="loginForm">
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
          <label for="password" class="block text-gray-600 text-sm font-medium"
            >Password:</label
          >
          <input
            type="password"
            id="password"
            name="password"
            class="mt-1 p-2 w-full border rounded-md"
          />
        </div>
        <button
          type="submit"
          class="bg-green-500 text-white px-4 py-2 rounded-md"
        >
          Login
        </button>
      </form>
      <p class="mt-4 text-sm">
        Don't have an account?
        <a href="register.html" class="text-green-500">Register here</a>.
      </p>
    </div>

   <h:footer/>

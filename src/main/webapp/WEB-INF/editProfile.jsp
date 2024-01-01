<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<h:header title="Edit Profile" bodyClasses="font-sans bg-gray-100 flex min-h-screen"/>

<h:sidebar from='EditProfileServlet'/>

<main class="flex-1 p-10">
  <h2 class="text-xl font-semibold mb-4">Edit Profile</h2>

  <form action="EditProfileServlet" method="post" enctype="multipart/form-data" class="max-w-md mx-auto bg-white p-8 rounded-md shadow-md">
    <!-- Avatar Upload -->
    <div class="mb-4">
      <label for="avatar" class="block text-gray-700 font-bold mb-2">Avatar:</label>
      <input type="file" name="avatar" accept="image/*" class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500">
    </div>

    <!-- Name Update -->
    <div class="mb-4">
      <label for="name" class="block text-gray-700 font-bold mb-2">Name:</label>
      <input type="text" name="name" value="${userName}" class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500">
    </div>

    <!-- Email Update -->
    <div class="mb-4">
      <label for="email" class="block text-gray-700 font-bold mb-2">Email:</label>
      <input type="text" name="email" value="${userEmail}" class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500">
    </div>

    <!-- Password Update -->
    <div class="mb-4">
      <label for="currentPassword" class="block text-gray-700 font-bold mb-2">Current Password:</label>
      <input type="password" name="currentPassword" class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500">
    </div>

    <div class="mb-4">
      <label for="newPassword" class="block text-gray-700 font-bold mb-2">New Password:</label>
      <input type="password" name="newPassword" class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500">
    </div>

    <div class="mb-4">
      <label for="confirmPassword" class="block text-gray-700 font-bold mb-2">Confirm Password:</label>
      <input type="password" name="confirmPassword" class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500">
    </div>

    <!-- Submit Button -->
    <button type="submit" class="bg-green-500 transition text-white px-4 py-2 rounded-md hover:bg-green-600 focus:outline-none">Update Profile</button>
  </form>
</main>


<h:footer/>

  <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/functions"
    prefix="fn" %>

    <h:header title="Reservations" bodyClasses="font-sans bg-gray-100 flex min-h-screen"/>



      <h:sidebar from='N/A' 
      
      />
  <main class="flex-1 p-10">
  
  
<div class="max-w-md mx-auto my-8 bg-white rounded-md shadow-md p-6">

    <h2 class="text-2xl font-semibold mb-4">Reservation Details</h2>

    <div class="mb-4">
        <p class="text-gray-500">Reservation ID: <span class="text-gray-800 font-semibold">${reservation.id}</span></p>
        <p class="text-gray-500">Date: <span class="text-gray-800 font-semibold">${reservation.date}</span></p>
        <p class="text-gray-500">Time: <span class="text-gray-800 font-semibold">${reservation.startTime} - ${reservation.endTime}</span></p>
        <p class="text-gray-500">Table: <span class="text-gray-800 font-semibold">${reservation.tableName}</span></p>
        <p class="text-gray-500">Room: <span class="text-gray-800 font-semibold">${reservation.room}</span></p>
        <p class="text-gray-500">Status: <span class="text-gray-800 font-semibold">${reservation.approvalStatus}</span></p>
        <p class="text-gray-500">Notes: <span class="text-gray-800 font-semibold">${reservation.notes}</span></p>
    </div>

    <!-- Buttons -->
    <div class="flex justify-between">
        <a href="#" class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition">Edit</a>
        <a href="#" class="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 transition">Delete</a>
    </div>

</div>
</main>
      <h:footer/>
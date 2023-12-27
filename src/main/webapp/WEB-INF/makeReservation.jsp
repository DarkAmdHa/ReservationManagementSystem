  <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/functions"
    prefix="fn" %>

    <h:header title="Make a Reservation" bodyClasses="font-sans bg-gray-100 flex min-h-screen"/>

      <h:sidebar from='MakeReservationServlet'/>
      
          <main class="flex-1 p-10">
      <h2 class="text-xl font-semibold mb-4">Make Reservation</h2>

      <!-- Your reservation form goes here -->
      <form
        action="ReservationServlet"
        method="post"
        class="max-w-md mx-auto bg-white p-8 rounded-md shadow-md"
      >
        <div class="mb-4">
          <label for="date" class="block text-gray-700 font-bold mb-2"
            >Reservation Date:</label
          >
          <input
            type="date"
            id="date"
            name="date"
            class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500"
          />
        </div>

        <div class="mb-4">
          <label for="time" class="block text-gray-700 font-bold mb-2"
            >Reservation Time:</label
          >
          <input
            type="time"
            id="time"
            name="time"
            class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500"
          />
        </div>

        <div class="mb-4">
          <label for="room" class="block text-gray-700 font-bold mb-2"
            >Select Room:</label
          >
          <select
            id="room"
            name="room"
            class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500"
          >
            <option value="room1">Room 1</option>
            <option value="room2">Room 2</option>
            <!-- Add more room options as needed -->
          </select>
        </div>

        <div class="mb-4">
          <label for="numSeats" class="block text-gray-700 font-bold mb-2"
            >Number of Seats:</label
          >
          <select
            id="numSeats"
            name="numSeats"
            class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500"
          >
            <option value="2">2 Seats</option>
            <option value="4">4 Seats</option>
            <option value="8">8 Seats</option>
            <option value="10+">10+ Seats</option>
          </select>
        </div>

        <div class="mb-4">
          <label
            for="specialRequests"
            class="block text-gray-700 font-bold mb-2"
            >Special Requests:</label
          >
          <textarea
            id="specialRequests"
            name="specialRequests"
            class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500"
          ></textarea>
        </div>

        <div class="flex items-center justify-between">
          <button
            type="submit"
            class="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 focus:outline-none"
          >
            Submit Reservation
          </button>
          <a href="DashboardServlet" class="text-gray-500 hover:underline"
            >Cancel</a
          >
        </div>
      </form>
    </main>
    
  <h:footer/>
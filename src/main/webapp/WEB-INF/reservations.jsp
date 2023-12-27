  <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/functions"
    prefix="fn" %>

    <h:header title="Reservations" bodyClasses="font-sans bg-gray-100 flex min-h-screen"/>

      <h:sidebar from='ReservationsServlet'/>

    <main class="flex-1 p-10">
      <h2 class="text-xl font-semibold mb-4">Reservations</h2>
      <div class="mb-4 hidden">
        <p class="text-gray-500 mb-2">
          No reservations yet. Click below to add a reservation.
        </p>
        <!-- Add Reservation Button -->
        <a
          href="ReservationServlet"
          class="bg-green-500 text-white px-4 py-2 rounded-md inline-block hover:bg-green-600 transition"
          >Add Reservation</a
        >
      </div>

      <div class="">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div
            class="bg-gradient-to-r from-gray-100 to-green-100 p-4 rounded-md shadow-md transition transform hover:scale-105 text-gray-700"
          >
            <h3 class="text-lg font-semibold mb-2">Reservation 1</h3>

            <div class="flex items-center text-sm pb-3 gap-4">
              <div class="border-r border-gray-300 pr-4">
                <p class="font-bold">Room 1</p>
                <p class="">6 Seats</p>
              </div>

              <p class="pr-4">December 20, 2022</p>
            </div>

            <p class="">
              Status: <span class="text-green-500 font-semibold">Accepted</span>
            </p>

            <a href="ViewReservationDetails" class="underline text-green-500"
              >View Details</a
            >
          </div>

          <div
            class="bg-gradient-to-r from-gray-100 to-red-100 p-4 rounded-md shadow-md transition transform hover:scale-105 text-gray-700"
          >
            <h3 class="text-lg font-semibold mb-2">Reservation 2</h3>

            <div class="flex items-center text-sm pb-3 gap-4">
              <div class="border-r border-gray-300 pr-4">
                <p class="font-bold">Room 2</p>
                <p class="">4 Seats</p>
              </div>

              <p class="pr-4">December 20, 2022</p>
            </div>

            <p class="text-gray-600">
              Status: <span class="text-red-500 font-semibold">Rejected</span>
            </p>

            <a href="ViewReservationDetails" class="underline text-green-500"
              >View Details</a
            >
          </div>

          <div
            class="p-4 rounded-md shadow-md transition transform hover:scale-105 text-gray-700"
          >
            <h3 class="text-lg font-semibold mb-2">Reservation 3</h3>

            <div class="flex items-center text-sm pb-3 gap-4">
              <div class="border-r border-gray-300 pr-4">
                <p class="font-bold">Room 3</p>
                <p class="">2 Seats</p>
              </div>

              <p class="pr-4">December 20, 2022</p>
            </div>

            <p class="text-gray-600">
              Status: <span class="text-gray-500 font-semibold">Pending</span>
            </p>

            <a href="ViewReservationDetails" class="underline text-green-500"
              >View Details</a
            >
          </div>

          <div
            class="bg-gray-200 p-4 rounded-md shadow-md transition transform hover:scale-105 text-gray-700"
          >
            <h3 class="text-lg font-semibold mb-2">Reservation 4</h3>

            <div class="flex items-center text-sm pb-3 gap-4">
              <div class="border-r border-gray-300 pr-4">
                <p class="font-bold">Room 4</p>
                <p class="">10+ Seats</p>
              </div>

              <p class="pr-4">December 23, 2022</p>
            </div>

            <p class="text-gray-600">
              Status: <span class="text-gray-500 font-semibold">Passed</span>
            </p>

            <a href="ViewReservationDetails" class="underline text-green-500"
              >View Details</a
            >
          </div>
        </div>
      </div>
    </main>
      <h:footer/>

  <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/functions"
    prefix="fn" %>

    <h:header title="Reservations" bodyClasses="font-sans bg-gray-100 flex min-h-screen"/>



      <h:sidebar from='ReservationsServlet' 
      
      />
  <main class="flex-1 p-10">
    <h2 class="text-xl font-semibold mb-4">Reservations</h2>

    <c:choose>
        <c:when test="${empty userReservations}">
            <div class="text-gray-500 mb-2">
                No reservations yet. Click below to add a reservation.
            </div>
            <!-- Add Reservation Button -->
            <a href="ReservationServlet"
               class="bg-green-500 text-white px-4 py-2 rounded-md inline-block hover:bg-green-600 transition"
            >Add Reservation</a>
        </c:when>
        <c:otherwise>



		<div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
		    <c:forEach var="reservation" items="${userReservations}" varStatus="loop">
    <div class="<c:choose>
                   <c:when test='${reservation.date.time lt System.currentTimeMillis()}'>bg-gray-200</c:when>
                   <c:when test='${reservation.approvalStatus eq "APPROVED"}'>bg-gradient-to-r from-gray-100 to-green-100</c:when>
                   <c:when test='${reservation.approvalStatus eq "DISAPPROVED"}'>bg-gradient-to-r from-gray-100 to-red-100</c:when>
                   <c:when test='${reservation.approvalStatus eq "PENDING"}'></c:when>
                   <c:otherwise>bg-gray-200</c:otherwise>
               </c:choose>
               p-4 rounded-md shadow-md transition transform hover:scale-105 text-gray-700">

        <div class="flex items-center text-sm pb-3 gap-4">
            <div class="border-r border-gray-300 pr-4">
                <p class="font-bold">${reservation.room}</p>
                <p class="">${reservation.tableName}</p>
            </div>
            <p class="pr-4">${reservation.date}</p>
            <p class="pr-4">${reservation.startTime} - ${reservation.endTime}</p>
        </div>
        <p class="">Status: 
            <span class="<c:choose>
                            <c:when test='${reservation.date.time lt System.currentTimeMillis()}'>text-gray-500 font-semibold">Passed</c:when>
                            <c:when test='${reservation.approvalStatus eq "APPROVED"}'>text-green-500 font-semibold">Approved</c:when>
                            <c:when test='${reservation.approvalStatus eq "DISAPPROVED"}'>text-red-500 font-semibold">Rejected</c:when>
                            <c:when test='${reservation.approvalStatus eq "PENDING"}'>text-gray-500 font-semibold">Pending</c:when>
                       </c:choose>
            </span>
        </p>
        <a href="ViewReservationDetails" class="underline text-green-500">View Details</a>
    </div>
</c:forEach>

		</div>
        </c:otherwise>
    </c:choose>
    
    <!-- Pagination Controls -->
    
<c:if test="${totalPages > 1}">
    <div class="pagination flex items-center justify-center gap-2 pt-4">
        <c:forEach begin="1" end="${totalPages}" var="page">
            <a href="${pageContext.request.contextPath}/ReservationsServlet?page=${page}"
               class="px-4 py-2 rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300 ${currentPage == page ? 'bg-green-500 text-white pointer-events-none' : ''}">
                ${page}
            </a>
        </c:forEach>
    </div>
</c:if>

</main>
      <h:footer/>

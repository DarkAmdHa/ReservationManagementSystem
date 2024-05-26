<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>



<h:header title="Reservations" bodyClasses="font-sans bg-gray-100 flex min-h-screen" />

<h:adminSidebar from='AdminDashboard' />

<main class="flex-1 p-10">
    <h2 class="text-xl font-semibold mb-4">Reservations</h2>
    
 

<form action="${pageContext.request.contextPath}/AdminDashboard" method="get">
  <input type="hidden" name="page" value="${currentPage}">

     <div class="flex gap-2 mb-4 items-end">
 	
        <div class="flex flex-col gap-2 flex-1">
            <label for="searchTerm">Search Term</label>
            <input type="text" id="searchTerm" name="searchTerm" class="border border-gray-300 rounded-md px-2 py-1 flex-grow" value="${searchTerm}">
        </div>
        <div class="flex flex-col gap-2 flex-1">
            <label for="searchBy">Search By</label>
            <select id="searchBy" name="searchBy" class="border border-gray-300 rounded-md px-2 py-1">
                <option value="user_name" <c:if test="${searchBy eq 'user_name'}">selected</c:if>>User Name</option>
                <option value="user_email" <c:if test="${searchBy eq 'user_email'}">selected</c:if>>User Email</option>
                <option value="table_number" <c:if test="${searchBy eq 'table_number'}">selected</c:if>>Table Number</option>
                <option value="seat_numbers" <c:if test="${searchBy eq 'seat_numbers'}">selected</c:if>>Seats Capacity</option>
                <option value="room_name" <c:if test="${searchBy eq 'room_name'}">selected</c:if>>Room Name</option>
              </select>
        </div>

  <button type="submit" class="bg-red-500 text-white px-4 py-2 rounded-md inline-block hover:bg-red-600 transition flex-1 h-fit max-w-12">
    Search
  </button>
</div>
</form>


    <c:choose>
         <c:when test="${empty allReservations and not empty searchTerm }">
            <div class="text-gray-500 mb-2">
                No results found for search term: "${searchTerm}".
                </div>
        </c:when>

        <c:when test="${empty allReservations}">
            <div class="text-gray-500 mb-2">
                No reservations yet. Click below to add a reservation.
            </div>
            <!-- Add Reservation Button -->
            <a href="ReservationServlet"
               class="bg-red-500 text-white px-4 py-2 rounded-md inline-block hover:bg-red-600 transition">
                Add Reservation
            </a>
        </c:when>
        <c:otherwise>
            <table class="rounded min-w-full border-collapse border border-gray-200 mt-4" style="box-shadow: -10px 0px 20px 8px rgb(0 0 0 / 7%);">
                <thead class='bg-gray-200 '>
                    <tr class='border border-1 border-gray-300'>
                    	<th class="p-2 text-left text-gray-500 font-medium">Reserved By</th>
                        <th class="p-2 text-left text-gray-500 font-medium">Room Name</th>
                        <th class="p-2 text-left text-gray-500 font-medium">Table</th>
                        <th class="p-2 text-left text-gray-500 font-medium">Table Capacity</th>
                        <th class=" p-2 text-left text-gray-500 font-medium">Date</th>
						<th class=" p-2 text-left text-gray-500 font-medium">From - To</th>	
                        <th class="p-2 text-left text-gray-500 font-medium text-center">Status</th>
                    </tr>
                </thead>
                <tbody class='border border-1 border-gray-300'>
                    <c:forEach var="reservation" items="${allReservations}" varStatus="loop">
                        <tr class="border-b border-1 border-gray-300 transition hover:bg-gray-200">
                        <td class="p-2">
                        	<div class='flex gap-2'>
                        		<img
							     <c:choose>
							            <c:when test="${not empty reservation.avatarUrl}">
							                src="${pageContext.request.contextPath}/static/images/avatars/${reservation.avatarUrl}"
							            </c:when>
							            <c:otherwise>
							                src="${pageContext.request.contextPath}/static/images/avatar.jpg"
							            </c:otherwise>
							        </c:choose>
							
							          width="20px"
							          height="20px"
							          class="rounded-full overflow-hidden"
							        />
                        		${reservation.userName}
                        	</div>
                        </td>
                                                    <td class="p-2">${reservation.roomName}</td>
                            <td class="p-2">${reservation.tableName}</td>
                           <th class="p-2 text-left text-gray-500 font-medium">
                             <c:choose>
						        <c:when test="${reservation.tableCapacity >= 10}">
						            10+ Seats
						        </c:when>
						        <c:otherwise>
						            ${reservation.tableCapacity} Seats
						        </c:otherwise>
						    </c:choose>
                           </th>
                            <td class="p-2">${reservation.date}</td>
							<td class="p-2">${reservation.startTime} - ${reservation.endTime}</td>
                            <td class="p-2 flex gap-3 items-center justify-center">
                            
							                            
                                <c:choose>
                                    <c:when test='${reservation.approvalStatus eq "APPROVED"}'>
                                    	<p class='status text-green-500'>Approved</p>
										<input type="checkbox" class='statusToggle' id="statusToggle-${reservation.id }" checked="true" hidden onchange="handleToggleChange('${reservation.id}', this)" />
										
										<!-- Visual Toggle Div -->
										<label for="statusToggle-${reservation.id }" class="toggle-switch">
										    <div class="toggle-slider"></div>
										</label>
                                    </c:when>
                                    <c:when test='${reservation.approvalStatus eq "DISAPPROVED"}'>
                                    <p class='status text-red-500'>Disapproved</p>
										<input type="checkbox" class='statusToggle' id="statusToggle-${reservation.id }" hidden onchange="handleToggleChange('${reservation.id}', this)" />
										
										<!-- Visual Toggle Div -->
										<label for="statusToggle-${reservation.id }" class="toggle-switch disapproved">
										    <div class="toggle-slider"></div>
										</label>	
                                    </c:when>
                                    <c:otherwise>
                                    <p class='status text-gray-500'>Pending</p>
										<input type="checkbox" class='statusToggle' id="statusToggle-${reservation.id }"  hidden onchange="handleToggleChange('${reservation.id}', this)" />
										
										<!--	 Visual Toggle Div -->
										<label for="statusToggle-${reservation.id }" checked="false" class="toggle-switch">
										    <div class="toggle-slider"></div>
										</label>
									</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <!-- Pagination Controls -->
    <c:if test="${totalPages > 1}">
        <div class="pagination flex items-center justify-center gap-2 pt-4">
            <c:forEach begin="1" end="${totalPages}" var="page">
                <a href="${pageContext.request.contextPath}/AdminDashboard?page=${page}"
                   class="px-4 py-2 rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300 ${currentPage == page ? 'bg-red-500 text-white pointer-events-none' : ''}">
                    ${page}
                </a>
            </c:forEach>
        </div>
    </c:if>
</main>

<script>
    function handleToggleChange(reservationId, checkbox) {
        setLoading(true); // Assuming setLoading function is defined elsewhere

        // Prepare data to send
        const status = checkbox.checked ? 'APPROVED' : 'DISAPPROVED';
        const data = { id: reservationId, status: status };

        // AJAX call to the server
        fetch('/reservation_system/SetReservationStatusServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        })
        .then(response => response.json())
        .then(data => {
        setLoading(false);
        if (data.status === "success") {
            updateUI(reservationId, status);
        } else {
            throw new Error(data.message || 'Something went wrong');
        }
    }).catch(error => {
        console.error('Error:', error);
        setLoading(false);
        checkbox.checked = !checkbox.checked; // Revert the checkbox state
        alert(error.message);
    });
    }

    function updateUI(reservationId, status) {

        const label = document.querySelector(`label[for='statusToggle-${"${reservationId}"}']`);
        const textElement = label.parentElement.querySelector('p.status');
		
        if (status === 'APPROVED') {
            label.classList.remove('disapproved');
            textElement.textContent = 'Approved';
            textElement.className = 'status text-green-500';
        } else {
            label.classList.add('disapproved');
            textElement.textContent = 'Disapproved';
            textElement.className = 'status text-red-500';
        }
    }
</script>


<h:footer />

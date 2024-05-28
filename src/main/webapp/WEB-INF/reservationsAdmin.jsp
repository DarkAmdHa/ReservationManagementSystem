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
                <option value="all" <c:if test="${searchBy eq 'user_name'}">selected</c:if>>All</option>
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

<c:if test="${userRole eq 'ADMIN'}">
<a href='#' class='exportReservations text-red-500 underline transition hover:text-red-400'>
	Export Reservations
</a>
</c:if>

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
                        <th class="p-2 text-left text-gray-500 font-medium">Table Capacity <span class='text-xs'>(seats)</span></th>
                        <th class=" p-2 text-left text-gray-500 font-medium">Date</th>
						<th class=" p-2 text-left text-gray-500 font-medium">From - To</th>	
                        <th class="p-2 text-left text-gray-500 font-medium text-center">Approval Status</th>
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
                           <td class="p-2 text-left text-gray-500 font-medium">
                             <c:choose>
						        <c:when test="${reservation.tableCapacity >= 10}">
						            10+
						        </c:when>
						        <c:otherwise>
						            ${reservation.tableCapacity}
						        </c:otherwise>
						    </c:choose>
                           </td>
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
                                    <c:when test='${reservation.approvalStatus eq "PENDING"}'>
										<p class='status text-gray-500'>Pending</p>
										<input type="checkbox" class='statusToggle' id="statusToggle-${reservation.id }"  hidden onchange="handleToggleChange('${reservation.id}', this)" />
										
										<!--	 Visual Toggle Div -->
										<label for="statusToggle-${reservation.id }" checked="false" class="toggle-switch">
										    <div class="toggle-slider"></div>
										</label>
                                   </c:when>
                                    
                                    <c:otherwise>
    									<p class='status text-gray-500'>Passed</p>
									</c:otherwise>
                                </c:choose>
                            </td>

                             <td class="p-2 text-xs">
                                <c:choose>
                                    <c:when test='${reservation.status eq "Reserved"}'>
                                    	<p class='status text-green-500 mb-2 statusText'>Reserved</p>
                        				<c:if test="${reservation.approvalStatus != 'PASSED'}">
	                            			<div class='statusChange flex rounded overflow-hidden border border-gray-500' id="statusButtons-${reservation.id }" >
	                                    		<button class='bg-green-500 text-white opacity-70 px-2 py-1  pointer-events-none px-2 py-1 w-full statusChange' data-value="Reserved" disabled>Reserved</button>
	                                    		<button class='bg-transparent text-black px-2 py-1 hover:bg-green-500 hover:opacity-80 transition  w-full statusChange' data-value="Missed">Missed</button>
	                                    		<button class='bg-transparent text-black px-2 py-1 hover:bg-green-500 hover:opacity-80 transition w-full statusChange'  data-value="Attended">Attended</button>
	                                    	</div>
                            			</c:if>							
                                    </c:when>
                                    <c:when test='${reservation.status eq "Cancelled"}'>
                                    	<p class='status text-red-500 mb-2'>Cancelled</p>
                                    </c:when>
               	                   <c:when test='${reservation.status eq "Missed"}'>
                                    	<p class='status text-red-500 mb-2'>Missed</p>
                                		<c:if test="${reservation.approvalStatus != 'PASSED'}">
                            				<div class='statusChange flex rounded overflow-hidden border border-gray-500' id="statusButtons-${reservation.id }" >
	                                    		<button class=' w-full bg-transparent text-black px-2 py-1 hover:bg-green-500 hover:opacity-80 transition statusChange' data-value="Reserved">Reserved</button>
	                                    		<button class=' w-full bg-green-500 text-white px-2 py-1   opacity-70 pointer-events-none statusChange' data-value="Missed" disabled>Missed</button>
	                                    		<button class=' w-full bg-transparent text-black px-2 py-1 hover:bg-green-500 hover:opacity-80 transition statusChange'  data-value="Attended">Attended</button>
	                                    	</div>
                            			</c:if>
                                    </c:when>
                                     <c:when test='${reservation.status eq "Attended"}'>
                                    	<p class='status text-green-500 mb-2'>Attended</p>
                                    	<c:if test="${reservation.approvalStatus != 'PASSED'}">
            								<div class='statusChange flex rounded overflow-hidden border border-gray-500' id="statusButtons-${reservation.id }" >
	                                    		<button class=' w-full bg-transparent text-black px-2 py-1 hover:bg-green-500 hover:opacity-80 transition statusChange' data-value="Reserved">Reserved</button>
	                                    		<button class=' w-full bg-transparent text-black px-2 py-1 hover:bg-green-500 hover:opacity-80 transition statusChange' data-value="Missed">Missed</button>
	                                    		<button class='  w-full bg-green-500 text-white px-2 py-1  opacity-70 pointer-events-none statusChange'  data-value="Attended" disabled>Attended</button>
                                    		</div>	
                                    	</c:if>

                                    </c:when>
                                </c:choose>
                            </td>
                            
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
    
    <div id="exportModal" class="relative z-10 hidden"
		aria-labelledby="Export Modal" role="dialog" aria-modal="true">
		<div
			class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity modalBackdrop"></div>
		<div class="fixed inset-0 z-10 w-screen overflow-y-auto">
			<div
				class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">

				<div
					class="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95 relative transform overflow-hidden rounded-lg bg-white px-4 pb-4 pt-5 text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-sm sm:p-6 modalPanel">
					<form id="exportForm" class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">

						<div class="mb-4">
							<div class='flex gap-2 items-center justify-center'>
							
							<input type="checkbox" name="searchedReservations" id="searchedReservations" checked/>
							<label for="searchedReservations"
								class="block text-gray-700 font-bold">Only export searched reservations:</label> 
							</div>
							
							<p class='text-xs'>Checking this will result in only exporting products that were filtered using the search bar.</p>
							
						</div>
						<div class="hidden dateSelectors">
							<div class='mb-4'>
								<label for="from" class="block text-gray-700 font-bold mb-2">Export From:</label> 
								<input type="date" id="from" name="from" placeholder="Export From"  />	
							</div>
							<div class='mb-4'>
								<label for="to" class="block text-gray-700 font-bold mb-2">Export To:</label> 
								<input type="date" id="to" name="to" placeholder="Export From"  />	
							</div>

						</div>
						<ul class='submissionErrors text-sm text-red-500 py-2'>
						</ul>	
						<div class="flex items-center justify-between">

							<button id="submitButton" type="submit"
								class="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 focus:outline-none">Export Reservations
								</button>
							<button type="button" id="cancelButton"
								class="text-gray-500 hover:underline">Cancel</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

    <!-- Pagination Controls -->
    <c:if test="${totalPages > 1}">
        <div class="pagination flex items-center justify-center gap-2 pt-4">
            <c:forEach begin="1" end="${totalPages}" var="page">
                <a href="${pageContext.request.contextPath}/AdminDashboard?page=${page}<c:if test="!${searchBy eq ''}">&searchBy=${searchBy}</c:if><c:if test="!${searchTerm eq ''}">&searchTerm=${searchTerm}</c:if>"
                   class="px-4 py-2 rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300 ${currentPage == page ? 'bg-red-500 text-white pointer-events-none' : ''}">
                    ${page}
                </a>
            </c:forEach>
        </div>
    </c:if>
</main>

<script>

    const modal = document.getElementById('exportModal');
    const modalBackdrop = modal.querySelector('.modalBackdrop');
    const modalPanel = modal.querySelector('.modalPanel');
    const exportTrigger = document.querySelector('.exportReservations');
    const exportForm = document.getElementById('exportForm');
    const cancelButton = modal.querySelector('#cancelButton');
    const searchedReservationsOnly = modal.querySelector('#searchedReservations');
    const dateSelectors = modal.querySelector('.dateSelectors');

function showModal() {
    modal.classList.remove('hidden');
    setTimeout(() => {
    	modalBackdrop.classList.add('opacity-100');
    	modalBackdrop.classList.remove('opacity-0');
    	modalPanel.classList.add('opacity-100','translate-y-0','sm:scale-100')
        modalPanel.classList.remove('opacity-0','sm:translate-y-0','sm:scale-95')
    }, 10);
}

function hideModal() {
	modalBackdrop.classList.add('opacity-0');
	modalBackdrop.classList.remove('opacity-100');
    modalPanel.classList.add('opacity-0','sm:translate-y-0','sm:scale-95')
	modalPanel.classList.remove('opacity-100','translate-y-0','sm:scale-100')
    setTimeout(() => {
        modal.classList.add('hidden');
    }, 300);
}

exportTrigger.addEventListener('click', function (e) {
	e.preventDefault();
    showModal();
});

cancelButton.addEventListener('click', function () {
    hideModal();
});


searchedReservationsOnly.addEventListener('input', e=>{
	if(e.target.checked){
		hideDates();
	}else{
		showDates();
	}
})

dateSelectors.querySelector('#from').addEventListener('input', e=>{
	if(e.value != ''){
		dateSelectors.querySelector('#to').min = e.target.value;
	}
})
const hideDates = ()=>{
	dateSelectors.classList.add('hidden');
	dateSelectors.querySelectorAll('input').forEach(inp=>inp.required = false);
}

const showDates = ()=>{
	dateSelectors.classList.remove('hidden');
	dateSelectors.querySelectorAll('input').forEach(inp=>inp.required = true);
}

const pushError =(msg, success=false) => {
	  const li = document.createElement('li');
	  li.innerText = msg;
	  li.classList.add('fadeup');
	  if(!success){
		  li.classList.add('text-red-500')
	  }else{
		  li.classList.add('text-green-500')
	  }
	  document.querySelector('.submissionErrors').appendChild(li);  
	  setTimeout(()=>{
		  li.remove();
	  },2000)
  }
  
  
exportForm.addEventListener('submit', function (e) {
    e.preventDefault();
    const formData = new FormData(exportForm);
    const data = {};
    formData.forEach((value, key) => {
        data[key] = value;
    });
    if(data["searchedReservations"] == "on" ){
    	//Get the search term and search by values:
    	data["searchTerm"] = document.querySelector('#searchTerm').value;
    	data["searchBy"] = document.querySelector('#searchBy').value;
    }
    

    const url = '/reservation_system/ExportReservations';
    let method = 'POST';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            pushError("Network response was not ok");
        }
        return response.blob(); // Parse the response body as a Blob
    })
    .then(blob  => {
    	// Create a URL for the blob
        const url = window.URL.createObjectURL(blob);

        // Create a link element
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'reservations.csv'); // Specify the filename for download
        document.body.appendChild(link);

        // Trigger the click event on the link
        link.click();

        // Clean up
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
        
        hideModal()
    })
    .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    });
});

    
    function handleToggleChange(reservationId, checkbox) {
        setLoading(true);

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
    
    
     document.querySelectorAll('.statusChange button').forEach(button => {
         button.addEventListener('click', function() {
             if (!this.disabled) {
                 const reservationId = this.parentElement.id.split('-')[1];
                 const newStatus = this.getAttribute('data-value');
                 handleStatusChange(reservationId, newStatus);
             }
         });
     });
     
     
     function handleStatusChange(reservationId, newStatus) {
    	    setLoading(true);

    	    const data = { id: reservationId, status: newStatus, statusUpdate: true };

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
    	            updateStatusUI(reservationId, newStatus);
    	        } else {
    	            throw new Error(data.message || 'Something went wrong');
    	        }
    	    }).catch(error => {
    	        console.error('Error:', error);
    	        setLoading(false);
    	        alert(error.message);
    	    });
    	}
     
     function updateStatusUI(reservationId, newStatus) {
    	    const buttons = document.querySelectorAll(`#statusButtons-${'${reservationId}'} button`);
    	    
    	    const statusTextDiv = document.querySelector(`#statusButtons-${'${reservationId}'}`).parentElement.querySelector('.status')
    	    statusTextDiv.innerText = newStatus;
    	    if(newStatus === 'Missed'){
    	    	statusTextDiv.classList.add('text-red-500');
    	    	statusTextDiv.classList.remove('text-green-500');
    	    }else{
    	    	statusTextDiv.classList.remove('text-red-500');
    	    	statusTextDiv.classList.add('text-green-500');
    	    }

    	    buttons.forEach(button => {
    	        if (button.getAttribute('data-value') === newStatus) {
    	            button.disabled = true;
    	            button.classList.add('bg-green-500', 'text-white', 'opacity-70', 'pointer-events-none');
    	            button.classList.remove('bg-transparent', 'text-black', 'hover:bg-green-500', 'hover:opacity-80', 'transition');
    	        } else {
    	            button.disabled = false;
    	            button.classList.add('bg-transparent', 'text-black', 'hover:bg-green-500', 'hover:opacity-80', 'transition');
    	            button.classList.remove('bg-green-500', 'text-white', 'opacity-70', 'pointer-events-none');
    	        }
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

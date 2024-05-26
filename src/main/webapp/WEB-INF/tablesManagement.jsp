<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<h:header title="Tables Management"
	bodyClasses="font-sans bg-gray-100 flex min-h-screen" />

<h:adminSidebar from='TableManagementServlet' />

<main class="flex-1 p-10">
	<h2 class="text-xl font-semibold mb-4 flex justify-between items-center">
		Tables
		<button id="addTableButton"
			class="float-right bg-red-500 text-white px-4 py-2 rounded-md inline-block hover:bg-red-600 transition">
			Add Table</button>
	</h2>
	<form action="${pageContext.request.contextPath}/TableManagementServlet" method="get">
  		<input type="hidden" name="page" value="${currentPage}" />

     <div class="flex gap-2 mb-4 items-end">
 	
        <div class="flex flex-col gap-2 flex-1">
            <label for="searchTerm">Search Term</label>
            <input type="text" id="searchTerm" name="searchTerm" class="border border-gray-300 rounded-md px-2 py-1 flex-grow" value="${searchTerm}">
        </div>
        <div class="flex flex-col gap-2 flex-1">
            <label for="searchBy">Search By</label>
            <select id="searchBy" name="searchBy" class="border border-gray-300 rounded-md px-2 py-1">
                <option value="all" <c:if test="${searchBy eq 'all'}">selected</c:if>>All</option>
                <option value="table_name" <c:if test="${searchBy eq 'table_name'}">selected</c:if>>Table Name</option>
                <option value="seat_numbers" <c:if test="${searchBy eq 'table_capacity'}">selected</c:if>>Table Capacity</option>
                <option value="room_name" <c:if test="${searchBy eq 'room_name'}">selected</c:if>>Room Name</option>
              </select>
        </div>

  <button type="submit" class="bg-red-500 text-white px-4 py-2 rounded-md inline-block hover:bg-red-600 transition flex-1 h-fit max-w-12">
    Search
  </button>
</div>
</form>

  <%
	boolean tableCreated = request.getParameter("tableCreated") != null;
	boolean tableUpdated = request.getParameter("tableUpdated") != null;
	boolean tableDeleted = request.getParameter("tableDeleted") != null;
if (tableCreated) {
%>
   <div class='bg-green-500 w-full py-2 text-sm rounded-lg fadeUp createMsg text-white text-center font-semibold'>Table Updated</div>
   <script>setTimeout(()=>{
   	document.querySelector('.createMsg').remove();
   },4000)</script>
<%
}
else if (tableUpdated) {
%>
    <div class='bg-green-500 w-full py-2 text-sm rounded-lg fadeUp updateMsg text-white text-center font-semibold'>Table Updated</div>
    <script>setTimeout(()=>{
    	document.querySelector('.updateMsg').remove();
    },4000)</script>
<%
}
else if (tableDeleted) {
	%>
    <div class='bg-green-500 w-full py-2 text-sm rounded-lg fadeUp deleteMsg text-white text-center font-semibold'>Table Deleted</div>
    <script>setTimeout(()=>{
    	document.querySelector('.deleteMsg').remove();
    },4000)</script>
	<% 
}


%>
	<c:choose>
		<c:when test="${empty allTables}">
			<div class="text-gray-500 mb-2">No tables added.</div>
		</c:when>
		<c:otherwise>
			<table
				class="rounded min-w-full border-collapse border border-gray-200 mt-4"
				style="box-shadow: -10px 0px 20px 8px rgb(0 0 0/ 7%);">
				<thead class='bg-gray-200 '>
					<tr class='border border-1 border-gray-300'>
						<th class="p-2 text-left text-gray-500 font-medium">Table
							Name</th>
						<th class="p-2 text-left text-gray-500 font-medium">Table
							Capacity</th>
						<th class="p-2 text-left text-gray-500 font-medium">Room Name</th>
						<th class="p-2 text-left text-gray-500 font-medium">Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="table" items="${allTables}" varStatus="loop">
						<tr
							class="border-b border-1 border-gray-300 transition hover:bg-gray-200">
							<td class="p-2">${fn:escapeXml(table.name)}</td>
							<td class="p-2">${fn:escapeXml(table.capacity)} seats</td>
							<td class="p-2">${fn:escapeXml(table.roomName)}</td>
							<td class="p-2 flex gap-2">
								<button
									class="editTableButton text-blue-500 hover:text-blue-600"
									data-id="${table.id}" data-name="${table.name}"
									data-capacity="${table.capacity}"
									data-room-id="${table.roomId}">
									<i class="fas fa-edit"></i>
								</button>
								<button class="deleteTableButton text-red-500 hover:text-red-600" data-id="${table.id}">
									<i class="fas fa-trash"></i>
									
								</button>
								
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<!-- Pagination Controls -->
    <c:if test="${totalPages > 1}">
        <div class="pagination flex items-center justify-center gap-2 pt-4">
            <c:forEach begin="1" end="${totalPages}" var="page">
                <a href="${pageContext.request.contextPath}/TableManagementServlet?page=${page}"
                   class="px-4 py-2 rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300 ${currentPage == page ? 'bg-red-500 text-white pointer-events-none' : ''}">
                    ${page}
                </a>
            </c:forEach>
        </div>
    </c:if>
		</c:otherwise>
	</c:choose>


	<div id="tableModal" class="relative z-10 hidden"
		aria-labelledby="modal-title" role="dialog" aria-modal="true">
		<div
			class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity modalBackdrop"></div>
		<div class="fixed inset-0 z-10 w-screen overflow-y-auto">
			<div
				class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">

				<div
					class="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95 relative transform overflow-hidden rounded-lg bg-white px-4 pb-4 pt-5 text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-sm sm:p-6 modalPanel">
					<form id="tableForm" action="TableManagementServlet" method="post"
						class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
						<input name="id" id="editInput" value="" type="hidden"/>
						<div class="mb-4">
							<label for="name" class="block text-gray-700 font-bold mb-2">Table
								Name *:</label> <input type="text" id="name" name="name"
								class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500"
								placeholder="Table Name" required />
						</div>
						<div class="mb-4">
							<label for="seatsCapacity"
								class="block text-gray-700 font-bold mb-2">Table
								Capacity *:</label> <select id="seatsCapacity" name="seatsCapacity"
								class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500"
								required>
								<option disabled selected value="">Table Capacity</option>
								<option value="2">2</option>
								<option value="4">4</option>
								<option value="6">6</option>
								<option value="8">8</option>
								<option value="10">10</option>
							</select>
						</div>
						<div class="mb-4">
							<label for="roomId" class="block text-gray-700 font-bold mb-2">Table
								Room *:</label> <select id="roomId" name="roomId"
								class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500"
								required>
								<option disabled selected value="">Select a room</option>
								<c:forEach var="room" items="${allRooms}" varStatus="loop">
									<option value="${room.id}">${room.name}</option>
								</c:forEach>
							</select>
						</div>
						<ul class='submissionErrors text-sm text-red-500 py-2'>
						</ul>	
						<div class="flex items-center justify-between">

							<button id="submitButton" type="submit"
								class="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 focus:outline-none">Create
								Table</button>
							<button type="button" id="cancelButton"
								class="text-gray-500 hover:underline">Cancel</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</main>

<script>
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('tableModal');
    const modalBackdrop = modal.querySelector('.modalBackdrop');
    const modalPanel = modal.querySelector('.modalPanel');
    const addTableButton = document.getElementById('addTableButton');
    const cancelButton = document.getElementById('cancelButton');
    const tableForm = document.getElementById('tableForm');
    const editTableButtons = document.querySelectorAll('.editTableButton');
    const deleteTableButtons = document.querySelectorAll('.deleteTableButton');

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

    addTableButton.addEventListener('click', function () {
        tableForm.reset();
        tableForm.setAttribute('action', 'create');
        submitButton.textContent = 'Create Table';
        showModal();
    });

    cancelButton.addEventListener('click', function () {
        hideModal();
    });

    editTableButtons.forEach(button => {
        button.addEventListener('click', function () {
            const tableId = this.getAttribute('data-id');
            const tableName = this.getAttribute('data-name');
            const tableCapacity = this.getAttribute('data-capacity');
            const roomId = this.getAttribute('data-room-id');
			
            document.getElementById('editInput').value = tableId;
            document.getElementById('name').value = tableName;
            document.getElementById('seatsCapacity').value = tableCapacity;
            document.getElementById('roomId').value = roomId;
            tableForm.setAttribute('action', 'edit');
            submitButton.textContent = 'Update Table';

            showModal();
        });
    });
    
    deleteTableButtons.forEach(button => {
        button.addEventListener('click', function () {
        	if(window.confirm('Are you sure you want to delete this table? There might be reservations linked to this table.')){
       		 	const tableId = this.getAttribute('data-id');
				deleteTable(tableId);
        	}
        });
    });
    
    function deleteTable(tableId) {
        fetch(`/reservation_system/TableManagementServlet?id=`+ tableId, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
            	pushError('Failed to delete table.');
            	return;
            }
            return response.json();
        })
        .then(data => {
            if (data.status === 'success') {
           	  window.location.href='${pageContext.request.contextPath}/TableManagementServlet?tableDeleted=true';
            } else {
           	  pushError(data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error.message);
        });
    }
    
    
    tableForm.addEventListener('submit', function (e) {
        e.preventDefault();
        const formData = new FormData(tableForm);
        const data = {};
        formData.forEach((value, key) => {
            data[key] = value;
        });
        


        const action = tableForm.getAttribute('action');
        const url = '/reservation_system/TableManagementServlet';
        let method = 'POST';

        if (action === 'edit') {
            method = 'PUT';

        }

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(data => {
            if(data.status === 'success'){
            	if(action === 'edit')
              	  window.location.href='${pageContext.request.contextPath}/TableManagementServlet?tableUpdated=true';
            	else
            	  window.location.href='${pageContext.request.contextPath}/TableManagementServlet?tableCreated=true';
              }else if(data.status === 'USER_SESSION_EXPIRED'){
            	  window.location.href='${pageContext.request.contextPath}/LoginServlet?notLoggedIn=true';
              }else{
            	  pushError(data.message);
              }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    });
    
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
});
</script>

<h:footer />

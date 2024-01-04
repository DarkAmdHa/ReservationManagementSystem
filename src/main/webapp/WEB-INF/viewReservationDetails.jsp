<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<h:header title="Reservations" bodyClasses="font-sans bg-gray-100 flex min-h-screen" />

<h:sidebar from='N/A' />

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
            <a href="#" class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition" onclick="openEditModal()">Edit</a>
            <a href="#" class="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 transition" onclick="deleteReservation()">Delete</a>
        </div>
		<div class='submissionError'></div>
    </div>




        
          <div id="editReservationModal" class="modal">
          
          
            <div class="max-w-md mx-auto bg-white p-8 rounded-md shadow-md modalForm">
                <%@include file="editReservationForm.jsp" %>
            </div>
        </div>
        
        
    <script>
        function openEditModal() {
            // Show the modal
            document.getElementById('editReservationModal').classList.remove('hidden');
            document.getElementById('editReservationModal').classList.add('view');
            
            setTimeout(()=>{
            	document.querySelector('#editReservationModal .modalForm').classList.add('view');
            },500)

            
            document.getElementById('date').value = '${reservation.date}';
            document.getElementById('startTime').value = '${reservation.startTime}';
            const changeEvent = new Event('change');

            document.getElementById('date').dispatchEvent(changeEvent);
            document.getElementById('startTime').dispatchEvent(changeEvent);
            
            const endTimeFromServer = '${reservation.endTime}';
            
            document.querySelector('#endTime').value = endTimeFromServer.slice(0, 5);
            
            document.querySelector('#specialRequests').value = '${reservation.notes}';
            
            document.getElementById('endTime').dispatchEvent(changeEvent);
            document.getElementById('specialRequests').dispatchEvent(changeEvent);
            
            const awaitRooms = setInterval(()=>{
           		if(document.querySelector('.roomTab ')){
           			clearInterval(awaitRooms);
           			const changeEvent = new Event('change');
           			document.querySelector('[id="${reservation.room}"]').checked=true;
           			document.querySelector('[id="${reservation.room}"]').dispatchEvent(changeEvent);
           			
           			document.querySelector('[id="${reservation.tableName}"]').checked=true;
           			document.querySelector('[id="${reservation.tableName}"]').dispatchEvent(changeEvent);
           		}
            }, 500);
        }

        function closeEditModal() {
            // Hide the modal
			document.querySelector('#editReservationModal .modalForm').classList.remove('view');

            setTimeout(()=>{

                document.getElementById('editReservationModal').classList.remove('view');
                setTimeout(()=>{
                	document.getElementById('editReservationModal').classList.add('hidden');
                },500)
            },500)
        }
        
        document.querySelector('#editReservationModal').addEventListener('click', e=>{
        	if(!e.target.closest('.modalForm')){
        		closeEditModal();
        	}
        	
        })
        
        document.querySelector('#editReservationModal .cancel').addEventListener('click', e=>{
				e.preventDefault();
        		closeEditModal();
        	
        	
        })
        
          const pushError2 =(msg, success=false) => {
			  const li = document.createElement('li');
			  li.innerText = msg;
			  li.classList.add('fadeup');
			  if(!success){
				  li.classList.add('text-red-500')
			  }else{
				  li.classList.add('text-green-500')
			  }
			  document.querySelector('.submissionError').appendChild(li);  
			  setTimeout(()=>{
				  li.remove();
			  },2000)
		  }
		          
        const deleteReservation = ()=>{
        	if(window.confirm("Are you sure?")){
        		setLoading(true);
       	      setTimeout(async ()=>{
       	          try {
       	              const response = await fetch(
       	                "/reservation_system/MakeReservationServlet?id=${reservation.id}",
       	                {
       	                  method: "DELETE",
       	                  headers: {
       	                    "Content-Type": "application/x-www-form-urlencoded",
       	                  }
       	                }
       	              );
       	           setLoading(false);
       	              if (response.ok) {
       	                //No server crashes, check response:
       	              	      
       	                const data = await response.json();
       	                if(data.status === 'success'){
       	              	  window.location.href='${pageContext.request.contextPath}/ReservationsServlet?reservationDeleted=true';
       	                }else if(data.status === 'USER_SESSION_EXPIRED'){
       	              	  window.location.href='${pageContext.request.contextPath}/LoginServlet?notLoggedIn=true';
       	              	  
       	                }else{
       	                	pushError2(data.message);
       	                }
       	                
       	              } else {
       	                console.error("Error deleting reservation:", error);
       	             pushError2('The reservation couldn\'t be deleted.')
       	              }
       	            } catch (error) {
       	            	setLoading(false);
       	              console.error("Error deleting reservation:", error);
       	           pushError2('Something went wrong on the server. We\'re working on it!')
       	            }
       	      }, 1000);
        	}
        }
    </script>

</main>

<h:footer/>

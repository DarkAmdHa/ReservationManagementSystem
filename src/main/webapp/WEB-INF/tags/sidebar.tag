<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="from" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aside
      class="bg-white w-1/6 text-gray-600 shadow-lg max-w-screen sticky flex flex-col"
    >
      <div class="mb-4 px-6 py-3 border-b border-gray-200">
        <img
          src="https://picsum.photos/100"
          width="80px"
          height="80px"
          class="m-auto rounded-full overflow-hidden"
        />
        <a href="ProfileEditServlet">
          <p class="text-center mt-2 text-gray-500">Hi [Username]</p>
        </a>
      </div>



	        
	        
      <nav class="px-6 flex flex-col gap-2 h-full">
        <a
          href="ReservationsServlet"
 	      <c:choose>
		     <c:when test="${from eq 'ReservationsServlet'}">
				class="transition rounded flex gap-2 items-center py-1 px-3 text-white bg-green-500 pointer-events-none"
		
		     </c:when>
		     <c:otherwise>
		     class="transition rounded flex gap-2 items-center py-1 px-3 hover:text-green-500 hover:bg-gray-100"
		        
		     </c:otherwise>
		 </c:choose>
        	>
          <i class="fas fa-tachometer-alt" aria-hidden="true"></i> My Reservations
        </a>
        <a
          href="MakeReservationServlet"
          	      <c:choose>
		     <c:when test="${from eq 'MakeReservationServlet'}">
				class="transition rounded flex gap-2 items-center py-1 px-3 text-white bg-green-500 pointer-events-none"
		
		     </c:when>
		     <c:otherwise>
		     class="transition rounded flex gap-2 items-center py-1 px-3 hover:text-green-500 hover:bg-gray-100"
		        
		     </c:otherwise>
		 </c:choose>

        >
          <i class="fas fa-book" aria-hidden="true"></i> Make a Reservation
        </a>

        <a
          href="ProfileEditServlet"
                   	      <c:choose>
		     <c:when test="${from eq 'ProfileEditServlet'}">
				class="transition rounded flex gap-2 items-center py-1 px-3 text-white bg-green-500 pointer-events-none"
		
		     </c:when>
		     <c:otherwise>
		     class="transition rounded flex gap-2 items-center py-1 px-3 hover:text-green-500 hover:bg-gray-100"
		        
		     </c:otherwise>
		 </c:choose>
        >
          <i class="fas fa-user-edit" aria-hidden="true"></i> Edit Profile
        </a>
        <a
          href="ChangePasswordServlet"
                             	      <c:choose>
		     <c:when test="${from eq 'ChangePasswordServlet'}">
				class="transition rounded flex gap-2 items-center py-1 px-3 text-white bg-green-500 pointer-events-none"
		
		     </c:when>
		     <c:otherwise>
		     class="transition rounded flex gap-2 items-center py-1 px-3 hover:text-green-500 hover:bg-gray-100"
		        
		     </c:otherwise>
		 </c:choose>

        >
          <i class="fas fa-key" aria-hidden="true"></i> Change Password
        </a>
        <form action="LogoutServlet" method="post" class="px-3 mt-auto mb-10">
          <button
            type="submit"
            class="transition text-white hover:bg-red-600 bg-red-500 rounded py-1 px-3 w-full"
          >
            <i class="fas fa-sign-out-alt" aria-hidden="true"></i> Logout
          </button>
        </form>
      </nav>
    </aside>
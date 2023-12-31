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

      	<c:forEach var="link" items="${['ReservationsServlet', 'MakeReservationServlet', 'ProfileEditServlet', 'ChangePasswordServlet']}">
      	    <c:set var="linkText" />
    		<c:set var="fontAwesomeIcon" />
    		
    		<c:choose>
		      <c:when test="${link eq 'ReservationsServlet'}">
		        <c:set var="linkText" value="My Reservations" />
		        <c:set var="fontAwesomeIcon" value="fa-tachometer-alt" />
		      </c:when>
		      <c:when test="${link eq 'MakeReservationServlet'}">
		        <c:set var="linkText" value="Make a Reservation" />
		        <c:set var="fontAwesomeIcon" value="fa-book" />
		      </c:when>
		      <c:when test="${link eq 'ProfileEditServlet'}">
		        <c:set var="linkText" value="Edit Profile" />
		        <c:set var="fontAwesomeIcon" value="fa-user-edit" />
		      </c:when>
		      <c:when test="${link eq 'ChangePasswordServlet'}">
		        <c:set var="linkText" value="Change Password" />
		        <c:set var="fontAwesomeIcon" value="fa-key" />
		      </c:when>
		    </c:choose>
		    
		    <a
		      href="${link}"
		      class="transition rounded flex gap-2 items-center py-1 px-3 
		        ${from eq link ? 'text-white bg-green-500 pointer-events-none' : 'hover:text-green-500 hover:bg-gray-100'}"
		    >
		      <i class="fas ${fontAwesomeIcon}" aria-hidden="true"></i> ${linkText}
		    </a>
      	</c:forEach>
        
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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%> <%@ taglib tagdir="/WEB-INF/tags" prefix="h" %> <%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<h:header
  title="Make a Reservation"
  bodyClasses="font-sans bg-gray-100 flex min-h-screen"
/>

<h:sidebar from="MakeReservationServlet" />
<main class="flex-1 p-10">
  <h2 class="text-xl font-semibold mb-4">Make Reservation</h2>

<%@include file="makeReservationForm.jsp" %>

</main>
<h:footer />

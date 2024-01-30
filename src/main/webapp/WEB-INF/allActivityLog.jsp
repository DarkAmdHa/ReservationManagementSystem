<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%> <%@ taglib tagdir="/WEB-INF/tags" prefix="h" %> <%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<h:header
  title="Make a Reservation"
  bodyClasses="font-sans bg-gray-100 flex min-h-screen"
/>

<h:adminSidebar from="AllUsersActivityServlet" />

<main class="flex-1 p-10">

    <h2 class="text-xl font-semibold mb-4">System Activity Log</h2>

<table class="rounded min-w-full border-collapse border border-gray-200 mt-4" style="box-shadow: -10px 0px 20px 8px rgb(0 0 0 / 7%);">
        <thead class="bg-gray-200 ">
        <tr class='border border-1 border-gray-300'>
        <th class="p-2 text-left text-gray-500 font-medium">User Email</th>
            <th class="p-2 text-left text-gray-500 font-medium">Date</th>
            <th class="p-2 text-left text-gray-500 font-medium">Action</th>
            <th class="p-2 text-left text-gray-500 font-medium">Action Description</th>
        </tr>
        </thead>
        <tbody class='border border-1 border-gray-300'>
        <c:forEach var="log" items="${activityLog}">
            <tr class='border-b border-1 border-gray-300 transition hover:bg-gray-200'>
                <td class="p-2"><div class='flex gap-2'>
                        		<img
							     <c:choose>
							            <c:when test="${not empty user.avatarUrl}">
							                src="${pageContext.request.contextPath}/static/images/avatars/${user.avatarUrl}"
							            </c:when>
							            <c:otherwise>
							                src="${pageContext.request.contextPath}/static/images/avatar.jpg"
							            </c:otherwise>
							        </c:choose>
							
							          width="20px"
							          height="20px"
							          class="rounded-full overflow-hidden"
							        />
                        		${log.email}
                        	</div></td>
                <td class="p-2">${log.date}</td>
                <td class="p-2">${log.action}</td>
                <td class="p-2">${log.actionComment}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <!-- Pagination -->
    <div class="mt-4">






    <!-- Pagination Controls -->
  
<c:if test="${totalPages > 1}">
    <div class="pagination flex items-center justify-center gap-2 pt-4">
        <!-- Display the first two pages -->
        <c:forEach begin="1" end="${totalPages < 3 ? totalPages : 2}" var="page">
            <a href="${pageContext.request.contextPath}/AllUsersActivityServlet?page=${page}"
               class="px-4 py-2 rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300 ${currentPage == page ? 'bg-red-500 text-white pointer-events-none' : ''}">
                ${page}
            </a>
        </c:forEach>

        <!-- Ellipsis for skipping some pages -->
        <c:if test="${totalPages > 7 && currentPage > 4}">
            <span>...</span>
        </c:if>

        <!-- Display range around current page -->
        <c:forEach begin="${currentPage - 2 < 3 ? 3 : currentPage - 2}" end="${currentPage + 2 > totalPages - 2 ? totalPages - 2 : currentPage + 2}" var="page">
            <a href="${pageContext.request.contextPath}/AllUsersActivityServlet?page=${page}"
               class="px-4 py-2 rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300 ${currentPage == page ? 'bg-red-500 text-white pointer-events-none' : ''}">
                ${page}
            </a>
        </c:forEach>

        <!-- Another ellipsis if needed -->
        <c:if test="${totalPages > 7 && currentPage < totalPages - 3}">
            <span>...</span>
        </c:if>

        <!-- Display the last two pages -->
        <c:forEach begin="${totalPages - 1}" end="${totalPages}" var="page">
            <a href="${pageContext.request.contextPath}/AllUsersActivityServlet?page=${page}"
               class="px-4 py-2 rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300 ${currentPage == page ? 'bg-red-500 text-white pointer-events-none' : ''}">
                ${page}
            </a>
        </c:forEach>
    </div>
</c:if>







  

</div>

</main>

</body>
</html>

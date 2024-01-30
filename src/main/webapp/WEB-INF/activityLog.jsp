<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%> <%@ taglib tagdir="/WEB-INF/tags" prefix="h" %> <%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<h:header
  title="All Users Activity Log"
  bodyClasses="font-sans bg-gray-100 flex min-h-screen"
/>

<h:sidebar from="ActivityLogServlet" />

<main class="flex-1 p-10">
<div class="container mx-auto mt-10 shadow-lg bg-white rounded-lg p-16 px-8">
    <h1 class="text-2xl font-bold mb-4">Activity Log</h1>

    <table class="min-w-full border border-gray-200">
        <thead>
        <tr>
            <th class="border border-gray-200 px-4 py-2">Date</th>
            <th class="border border-gray-200 px-4 py-2">Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="log" items="${activityLog}">
            <tr>
                <td class="border border-gray-200 px-4 py-2">${log.date}</td>
                <td class="border border-gray-200 px-4 py-2">${log.action}</td>
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
            <a href="${pageContext.request.contextPath}/ActivityLogServlet?page=${page}"
               class="px-4 py-2 rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300 ${currentPage == page ? 'bg-green-500 text-white pointer-events-none' : ''}">
                ${page}
            </a>
        </c:forEach>

        <!-- Ellipsis for skipping some pages -->
        <c:if test="${totalPages > 7 && currentPage > 4}">
            <span>...</span>
        </c:if>

        <!-- Display range around current page -->
        <c:forEach begin="${currentPage - 2 < 3 ? 3 : currentPage - 2}" end="${currentPage + 2 > totalPages - 2 ? totalPages - 2 : currentPage + 2}" var="page">
            <a href="${pageContext.request.contextPath}/ActivityLogServlet?page=${page}"
               class="px-4 py-2 rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300 ${currentPage == page ? 'bg-green-500 text-white pointer-events-none' : ''}">
                ${page}
            </a>
        </c:forEach>

        <!-- Another ellipsis if needed -->
        <c:if test="${totalPages > 7 && currentPage < totalPages - 3}">
            <span>...</span>
        </c:if>

        <!-- Display the last two pages -->
        <c:forEach begin="${totalPages - 1}" end="${totalPages}" var="page">
            <a href="${pageContext.request.contextPath}/ActivityLogServlet?page=${page}"
               class="px-4 py-2 rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300 ${currentPage == page ? 'bg-green-500 text-white pointer-events-none' : ''}">
                ${page}
            </a>
        </c:forEach>
    </div>
</c:if>







  
    </div>
</div>

</main>

</body>
</html>

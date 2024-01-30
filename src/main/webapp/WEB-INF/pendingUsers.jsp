<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<h:header title="Pending Users" bodyClasses="font-sans bg-gray-100 flex min-h-screen" />

<h:adminSidebar from='PendingUsersServlet' />

<main class="flex-1 p-10">
    <h2 class="text-xl font-semibold mb-4">Pending User Activations</h2>
    <table class="rounded min-w-full border-collapse border border-gray-200 mt-4" style="box-shadow: -10px 0px 20px 8px rgb(0 0 0 / 7%);">
        <thead class='bg-gray-200 '>
            <tr class='border border-1 border-gray-300'>
                <th class="p-2 text-left text-gray-500 font-medium">Name</th>
                <th class="p-2 text-left text-gray-500 font-medium">Email</th>
                <th class="p-2 text-left text-gray-500 font-medium">Activate</th>
            </tr>
        </thead>
        <tbody class='border border-1 border-gray-300'>
            <c:choose>
                <c:when test="${empty pendingUsers}">
                    <tr class="border-b border-1 border-gray-300 transition hover:bg-gray-200">
                        <td class="text-gray-500 mb-2 p-4 text-center" colspan="3">
                            No pending user activations at the moment.
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="loopUser" items="${pendingUsers}" varStatus="loop">
                        <tr class="border-b border-1 border-gray-300 transition hover:bg-gray-200">
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
                        		${loopUser.name}
                        	</div></td>
                            <td class="p-2">${loopUser.email}</td>
                              <td class="p-2 flex gap-3 items-center">
                            <c:choose>
							            <c:when test="${loopUser.isDeactivated}">
							                <p class='status text-red-500'>Deactivated</p>
							                  <input type="checkbox" class='statusToggle' id="statusToggle-${loopUser.id}" hidden onchange="activateUser('${loopUser.id}', this)" />
                                <label for="statusToggle-${loopUser.id}" class="toggle-switch disapproved">
                                    <div class="toggle-slider"></div>
                                </label>
                                
							            </c:when>
							            <c:otherwise>
							                <p class='status text-gray-500'>Pending</p>
							                
							                  <input type="checkbox" class='statusToggle' id="statusToggle-${loopUser.id}" hidden onchange="activateUser('${loopUser.id}', this)" />
                                <label for="statusToggle-${loopUser.id}" class="toggle-switch">
                                    <div class="toggle-slider"></div>
                                </label>
							            </c:otherwise>
							        </c:choose>
							
                            	
                            
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
    
    
    <!-- Pagination Controls -->
    <c:if test="${totalPages > 1}">
        <div class="pagination flex items-center justify-center gap-2 pt-4">
            <c:forEach begin="1" end="${totalPages}" var="page">
                <a href="${pageContext.request.contextPath}/PendingUsersServlet?page=${page}"
                   class="px-4 py-2 rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300 ${currentPage == page ? 'bg-red-500 text-white pointer-events-none' : ''}">
                    ${page}
                </a>
            </c:forEach>
        </div>
    </c:if>
</main>

<script>
       function activateUser(userId, checkbox) {
        setLoading(true); // Assuming setLoading function is defined elsewhere

        // Prepare data to send
        const status = checkbox.checked ? 1 : 0;

        const data = { userId: +userId, isActive: status };

        // AJAX call to the server
        fetch('/reservation_system/SetUserActivationStatusServlet', {
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
            updateUI(userId, status);
        } else if (data.status === "USER_NOT_AUTHORIZED") {
            // Redirect to login page if user is not authorized
            alert(data.message);  // Show the unauthorized message
            window.location.href = '/reservation_system/LoginServlet'; 
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

    function updateUI(userId, status) {

        const label = document.querySelector(`label[for='statusToggle-${"${userId}"}']`);
        const textElement = label.parentElement.querySelector('p.status'); // Assuming the <p> element follows the label
		
        if (status) {
            label.classList.remove('disapproved');
            textElement.textContent = 'Activated';
            textElement.className = 'status text-green-500';
        } else {
            label.classList.add('disapproved');
            textElement.textContent = 'Deactivated';
            textElement.className = 'status text-red-500';
        }
    }
</script>

<h:footer />

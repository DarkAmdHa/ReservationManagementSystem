	<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
	<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	
	<h:header title="Edit Profile" bodyClasses="font-sans bg-gray-100 flex min-h-screen"/>
	
	<h:adminSidebar from='EditProfileAdminServlet'/>
	
	<main class="flex-1 p-10">
	  <h2 class="text-xl font-semibold mb-4">Edit Profile</h2>
	
	  <form id="editProfileForm" action="EditProfileServlet" method="post" enctype="multipart/form-data" class="max-w-md mx-auto bg-white p-8 rounded-md shadow-md">
	    <!-- Avatar Upload -->
	    <div class="mb-4">
	      <label for="avatar" class="block text-gray-700 font-bold mb-2">Avatar:</label>
	      <div id="avatarContainer" class='relative flex flex-col align-center'>
	                <img
	     <c:choose>
	            <c:when test="${not empty userAvatarUrl}">
	                src="${pageContext.request.contextPath}/static/images/avatars/${userAvatarUrl}"
	            </c:when>
	            <c:otherwise>
	                src="${pageContext.request.contextPath}/static/images/avatar.jpg"
	            </c:otherwise>
	        </c:choose>
	
	          width="150px"
	          height="80px"
	          class="m-auto rounded-full overflow-hidden"
	        />
	        <input type="file" name="avatar" accept="image/*" class="hidden" id="avatarInput">
	        <label for="avatarInput" class="m-auto cursor-pointer text-red-500 hover:underline">Change Avatar</label>
	      </div>
	    </div>
	
	    <!-- Name Update -->
	    <div class="mb-4">
	      <label for="name" class="block text-gray-700 font-bold mb-2">Name:</label>
	      <input type="text" name="name" value="${userName}" class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500">
	    </div>
	
	    <!-- Email Update -->
	    <div class="mb-4">
	<div class="mb-4">
	  <label for="email" class="block text-gray-700 font-bold mb-2">Email:</label>
	  <div id="emailContainer" class='flex justify-center items-center'>
	    <input type="text" name="email" value="${userEmail}" class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500" disabled>
	    <a href="#" onclick="toggleEmailField(); return false;" id="emailToggleLink" class="text-red-500 ml-2 hover:underline">Change</a>
	  </div>
	</div>
	
	
	    <!-- Password Update -->
	    <div class="mb-4">
	      
	      <div id="passwordForm" class=' overflow-hidden' style="display:none; transition: 0.3s ease;">
	        <label for="currentPassword" class="block text-gray-700 font-bold mb-2">Current Password:</label>
	        <input type="password" name="currentPassword" class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500">
	        
	        <label for="newPassword" class="block text-gray-700 font-bold mb-2">New Password:</label>
	        <input type="password" name="newPassword" class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500">
	        
	        <label for="confirmPassword" class="block text-gray-700 font-bold mb-2">Confirm Password:</label>
	        <input type="password" name="confirmPassword" class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500">
	      </div>
	      <a href="#" onclick="showPasswordForm(); return false;" class="text-red-500 block font-bold mb-2 password-link">Change Password</a>
	    </div>
	
	    <!-- Error Container -->
	    <div id="errorContainer" class="text-red-500 mb-4"></div>
	
	    <!-- Submit Button -->
	    <button type="submit" class="bg-red-500 transition text-white px-4 py-2 rounded-md hover:bg-red-600 focus:outline-none submitButton" disabled>Update Profile</button>
	  </form>
	</main>
	
	<script>
	document.getElementById("editProfileForm").addEventListener("submit", function (event) {
	    event.preventDefault();


	    const form = event.target;

	    // Client-side validation
	    const name = form.querySelector("[name='name']").value;
	    const emailField = form.querySelector("[name='email']");
	    const email = emailField.value;
	    const currentPassword = form.querySelector("[name='currentPassword']").value;
	    const newPassword = form.querySelector("[name='newPassword']").value;
	    const confirmPassword = form.querySelector("[name='confirmPassword']").value;

	    const errorMessages = [];

	    // Validate name
	    if (name.length < 2) {
	      errorMessages.push("Name must be at least 2 characters");
	    }

	    // Validate email if the email field is not disabled
	    if (!emailField.disabled) {
	      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	      if (!emailRegex.test(email)) {
	        errorMessages.push("Invalid email address");
	      }
	    }

	    // Validate password if the password form is visible
	    if (isPasswordFormVisible()) {
	      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
	      if (!passwordRegex.test(currentPassword)) {
	        errorMessages.push("Current Password is invalid");
	      }
	      if (!passwordRegex.test(newPassword)) {
	        errorMessages.push("New Password must have at least 8 characters, one uppercase letter, one lowercase letter, one digit, and one special character");
	      }
	      if (newPassword !== confirmPassword) {
	        errorMessages.push("New Password and Confirm Password do not match");
	      }
	    }

	    if (errorMessages.length > 0) {
	      // Display error messages and prevent form submission
	      displayErrorMessages(errorMessages);
	      return;
	    } else {
	      document.getElementById("errorContainer").innerHTML = '';
	    }

	    // If client-side validation passed, submit the form to the server
	    const formData = new FormData(form);

	    formData.append("name", form.querySelector("[name='name']").value);

	    
	    if (!emailField.disabled) {
	     formData.append("email", form.querySelector("[name='email']").value);
	    }

	    // Add password fields if the password form is visible
	    if (isPasswordFormVisible()) {
	      formData.append("currentPassword", form.querySelector("[name='currentPassword']").value);
	      formData.append("newPassword", form.querySelector("[name='newPassword']").value);
	      formData.append("confirmPassword", form.querySelector("[name='confirmPassword']").value);
	    }

	    // Add avatar file if it is selected
	const avatarInput = form.querySelector("#avatarInput");
	if (avatarInput.files.length > 0) {
	  formData.append("avatar", avatarInput.files[0]);
	}
	
	setLoading(true);
	
	setTimeout(()=>{
		fetch("/reservation_system/EditProfileServlet", {
		      method: "POST",
		      body: formData
		    })
		      .then(response => {
		    	  setLoading(false);
		        if (response.ok) {
		          return response.json();
		        } else {
		          throw new Error("Profile update failed");
		        }
		      })
		      .then(data => {
		        // Check the response data for update result
		        handleServerResponse(data);
		      })
		      .catch(error => {
		    	  setLoading(false);
		        // Handle update failure, show a generic error message
		        console.error("Profile update error:", error);
		      });
		  });
	}, 1000);
	    

  function isPasswordFormVisible() {
    const passwordForm = document.getElementById('passwordForm');
    return passwordForm.style.display !== 'none';
  }

  function handleServerResponse(data) {
    const errorContainer = document.getElementById("errorContainer");

    switch (data.status) {
      case "SUCCESS":
        console.log("Profile update successful");
        window.location.href='/reservation_system/ReservationsServlet?edited=true'
        break;
      case "USER_NOT_LOGGED_IN":
        // Update error container with server-side validation errors
        displayErrorMessages([data.message]);
        window.location.href='/reservation_system/LoginServlet?notLoggedIn=true"'
        break;
      case "ERROR":
        console.error("Profile update error");
        displayErrorMessages([data.message]);
        break;
      default:
        console.error("Unexpected profile update result");
        displayErrorMessages(["Something Went Wrong"]);
        break;
    }
  }

  function displayErrorMessages(errorMessages) {
    const errorContainer = document.getElementById("errorContainer");
    errorContainer.innerHTML = ""; // Clear previous error messages

    if (errorMessages && errorMessages.length > 0) {
      const errorList = document.createElement("ul");
      errorList.classList.add('mb-4', 'list-disc');
      errorMessages.forEach(message => {
        const listItem = document.createElement("li");
        listItem.className = 'opacity-0 transition transform translate-y-2 duration-100';
        listItem.textContent = message;
        errorList.appendChild(listItem);
      });
      errorContainer.appendChild(errorList);
      setTimeout(() => {
        errorContainer.querySelectorAll('li').forEach(listItem => listItem.className = 'opacity-1 transition transform translate-y-0 duration-100');
      }, 100);
    }
  }

  document.getElementById("avatarInput").addEventListener("change", function (event) {
    const input = event.target;
    const file = input.files[0];

    const avatarContainer = document.getElementById("avatarContainer");
    const avatarImage = avatarContainer.querySelector("img");
    if (file) {
      const reader = new FileReader();
      reader.onload = function (e) {
        const img = new Image();
        img.src = e.target.result;

        img.onload = function () {
          const aspectRatio = img.width / img.height;

          // Check if the aspect ratio is not 1:1
          if (Math.abs(aspectRatio - 1) > 0.01) {
            alert('Please upload a square image (1:1 aspect ratio).');
            // Clear the file input to prevent submission
            input.value = '';
          } else {
            // Display the selected image
            const reader = new FileReader();
            reader.onload = function (e) {
              avatarImage.src = e.target.result;
            };
            reader.readAsDataURL(file);
          }
        };
      };
      reader.readAsDataURL(file);
    } else {
      // Reset to default image when no file is selected
      avatarImage.src = "${not empty userAvatarUrl ? userAvatarUrl : pageContext.request.contextPath}/static/images/avatar.jpg";
    }
  });

  function enableField(containerId) {
    const container = document.getElementById(containerId);
    const inputField = container.querySelector('input');

    inputField.removeAttribute('disabled');
    inputField.focus();
  }

  function showPasswordForm() {
    const passwordForm = document.getElementById('passwordForm');
    const passwordLink = document.querySelector('.password-link');
    const currentPasswordField = document.querySelector("[name='currentPassword']");
    const newPasswordField = document.querySelector("[name='newPassword']");
    const confirmPasswordField = document.querySelector("[name='confirmPassword']");

    const isHidden = passwordForm.style.display === 'none' || passwordForm.style.display === '';

    if (isHidden) {
      // Change link to "Cancel" and show the password form with a transition
      passwordLink.textContent = "Cancel";
      passwordForm.style.display = 'block';
      passwordForm.style.height = '0px'; // Set initial height to 0
      const targetHeight = passwordForm.scrollHeight + 'px'; // Calculate target height
      passwordForm.style.height = targetHeight; // Set target height with transition
    } else {
      // Change link to "Change", hide the password form with a transition, and reset password fields
      passwordLink.textContent = "Change Password";
      passwordForm.style.height = '0px'; // Set height to 0 for transition
      setTimeout(() => {
        passwordForm.style.display = 'none'; // Hide the form after transition
      }, 300); // Adjust this value based on the transition duration in CSS
      currentPasswordField.value = '';
      newPasswordField.value = '';
      confirmPasswordField.value = '';
    }
  }

  let originalEmailValue = "${userEmail}";

  function toggleEmailField() {
    const emailField = document.querySelector("[name='email']");
    const emailToggleLink = document.getElementById("emailToggleLink");

    if (emailField.disabled) {
      // Change link to "Cancel" and enable the email field
      emailToggleLink.textContent = "Cancel";
      emailField.removeAttribute('disabled');
    } else {
      // Change link to "Change" and disable the email field
      emailToggleLink.textContent = "Change";
      emailField.setAttribute('disabled', 'disabled');
      emailField.value = originalEmailValue; // Reset to original value
    }
  }

  // Enable submit button when any field is changed
  document.querySelectorAll('input').forEach(input => {
    input.addEventListener('input', function () {
      document.querySelector('.submitButton').removeAttribute('disabled');
    });
  });
</script>
	
	<h:footer/>

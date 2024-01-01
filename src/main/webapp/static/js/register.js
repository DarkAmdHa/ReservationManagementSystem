document.getElementById("registerForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const form = event.target;

    fetch("/your-context-path/RegisterServlet", {
        method: "POST",
        body: new FormData(form),
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("Registration failed");
            }
        })
        .then(data => {
            // Check the response data for registration result
            switch (data.registrationResult) {
                case "SUCCESS":
                    // Handle successful registration, e.g., redirect to a new page
                    console.log("Registration successful");
                    window.location.href = "/reservation_system/ReservationsServlet";
                    break;
                case "USER_ALREADY_EXISTS":
                    // Handle user already exists error
                    console.error("User already exists");
                    displayErrorMessages(["User with this email already exists"]);
                    break;
                case "ERROR":
                    // Handle registration error
                    console.error("Registration error");
                    displayErrorMessages(["Registration error"]);
                    break;
                default:
                    // Handle unexpected response
                    console.error("Unexpected registration result");
                    displayErrorMessages(["Unexpected registration result"]);
                    break;
            }
        })
        .catch(error => {
            // Handle registration failure, show a generic error message
            console.error("Registration error:", error);
        });
});

function displayErrorMessages(errorMessages) {
    // Implement logic to display error messages in your HTML, for example:
    const errorContainer = document.getElementById("errorContainer");
    errorContainer.innerHTML = ""; // Clear previous error messages

    if (errorMessages && errorMessages.length > 0) {
        const errorList = document.createElement("ul");
        errorMessages.forEach(message => {
            const listItem = document.createElement("li");
            listItem.textContent = message;
            errorList.appendChild(listItem);
        });
        errorContainer.appendChild(errorList);
    }
}

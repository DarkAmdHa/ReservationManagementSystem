<form
  action="ReservationServlet"
  method="post"
  class="max-w-md mx-auto bg-white p-8 rounded-md shadow-md"
>
  <input type="hidden" id="editReservationId" value="${reservation.id}" />
  <div class="mb-4">
    <label for="date" class="block text-gray-700 font-bold mb-2"
      >Reservation Date:</label
    >
    <input
      type="date"
      id="date"
      name="date"
      class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500"
    />
  </div>

  <div class="mb-4">
    <label for="startTime" class="block text-gray-700 font-bold mb-2"
      >Reservation Start Time:</label
    >
    <input
      type="time"
      id="startTime"
      name="startTime"
      class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500"
    />
  </div>

  <div class="mb-4 endTimeField hidden">
    <label for="endTime" class="block text-gray-700 font-bold mb-2"
      >Reservation End Time:</label
    >

    <select
      id="endTime"
      name="endTime"
      class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500"
      required
    >
      <option disabled selected value="">Reservation End Time</option>
    </select>
  </div>

  <div class="mb-4">
    <p class="block text-gray-500 text-center mb-2 infoText">
      Select a date and time for your reservation.
    </p>

    <div class="tableSelectorContainer flex flex-col items-center"></div>
    <div class="errorsContainer flex flex-col items-center"></div>
  </div>

  <div class="mb-4">
    <label for="specialRequests" class="block text-gray-700 font-bold mb-2"
      >Special Requests:</label
    >
    <textarea
      id="specialRequests"
      name="specialRequests"
      class="w-full border rounded-md py-2 px-3 focus:outline-none focus:border-blue-500"
    ></textarea>
  </div>
  <ul class="submissionErrors text-sm text-red-500 pb-2"></ul>
  <div class="flex items-center justify-between">
    <button
      type="submit"
      class="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 focus:outline-none submitButton"
      disabled
    >
      Submit Reservation
    </button>
    <a href="ReservationsServlet" class="text-gray-500 hover:underline cancel"
      >Cancel</a
    >
  </div>
</form>

<script>
  const dateInput = document.querySelector("#date"),
    startTimeInput = document.querySelector("#startTime"),
    endTimeInput = document.querySelector("#endTime"),
    tableSelectorContainer = document.querySelector(".tableSelectorContainer"),
    submitButton = document.querySelector('button[type="submit"]'),
    errorsContainer = document.querySelector(".errorsContainer");

  let submittable = {
    dateAdded: false,
    timeStartSet: false,
    timeEndSet: false,
    TableSet: false,
  };

  checkIsSubmittable = () => {
    if (
      typeof Object.values(submittable).find((i) => i != true) != "undefined"
    ) {
      document.querySelector(".submitButton").disabled = true;
    } else {
      document.querySelector(".submitButton").disabled = false;
    }
  };

  // Set minimum date to today for date input
  const minDate = new Date();
  minDate.setDate(minDate.getDate() + 2);

  dateInput.min = minDate.toISOString().split("T")[0];

  function convertTo12HourFormat(time24) {
    // Create a Date object with a dummy date (January 1, 2000)
    const dummyDate = new Date("2000-01-01T" + time24);

    // Format the time in 12-hour format
    const time12 = dummyDate.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
      hour12: true,
    });

    return time12;
  }

  dateInput.addEventListener("change", () => {
    submittable.dateAdded = true;
  });
  startTimeInput.addEventListener("change", () => {
    // Clear the endTimeInput value
    endTimeInput.value = "";
    submittable.timeEndSet = false;

    submittable.timeStartSet = true;

    // Calculate options for end time based on start time
    const options = calculateEndTimeOptions(startTimeInput.value);

    // Set the options for the endTimeInput
    setEndTimeOptions(options);
    document.querySelector(".endTimeField").classList.remove("hidden");
  });
  endTimeInput.addEventListener("change", () => {
    submittable.timeEndSet = true;
  });

  function calculateEndTimeOptions(startTime) {
    const start = new Date(`2000-01-01T${"${startTime}"}`);

    // Calculate end times: 30 minutes, 1 hour, 1 hour 30 minutes, and 2 hours after start time
    const option1 = new Date(start.getTime() + 30 * 60 * 1000);
    const option2 = new Date(start.getTime() + 60 * 60 * 1000);
    const option3 = new Date(start.getTime() + 90 * 60 * 1000);
    const option4 = new Date(start.getTime() + 2 * 60 * 60 * 1000);

    // Format options as HH:mm
    const formattedOption1 = option1.toTimeString().slice(0, 5);
    const formattedOption2 = option2.toTimeString().slice(0, 5);
    const formattedOption3 = option3.toTimeString().slice(0, 5);
    const formattedOption4 = option4.toTimeString().slice(0, 5);

    return [
      formattedOption1,
      formattedOption2,
      formattedOption3,
      formattedOption4,
    ];
  }

  function setEndTimeOptions(options) {
    // Remove existing options
    while (endTimeInput.firstChild) {
      endTimeInput.removeChild(endTimeInput.firstChild);
    }

    //Default option:
    const optionElement = document.createElement("option");
    optionElement.value = "";
    optionElement.text = "Reservation End Time";
    optionElement.disabled = true;
    optionElement.selected = true;
    endTimeInput.add(optionElement);

    // Add new options
    options.forEach((option) => {
      const optionElement = document.createElement("option");
      optionElement.value = option;
      optionElement.text = convertTo12HourFormat(option);
      endTimeInput.add(optionElement);
    });
  }

  function calculateTimeDifference(startTime, endTime) {
    const start = new Date(`2000-01-01T${"${startTime}"}`);
    const end = new Date(`2000-01-01T${"${endTime}"}`);
    const differenceInMinutes = (end - start) / (1000 * 60);
    return differenceInMinutes;
  }

  document.querySelectorAll("#date, #startTime, #endTime").forEach((el) =>
    el.addEventListener("change", (e) => {
      submittable.TableSet = false;
      checkIsSubmittable();
      if (validateForm()) {
        fetchTables();
      } else if (startTime.value !== "" && endTime.value !== "") {
        const startTimeDate = new Date(`2000-01-01T${startTime.value}`);
        const endTimeDate = new Date(`2000-01-01T${endTime.value}`);

        if (
          startTimeDate >= endTimeDate &&
          !errorsContainer.querySelector(".wrongTime")
        ) {
          errorsContainer.innerHTML +=
            "<p class='text-red-500 font-semibold text-center wrongTime'>Start time should be before end time.</p>";
          setTimeout(() => {
            errorsContainer.querySelector(".wrongTime").remove();
          }, 2000);
        }
      }
    })
  );

  function organizeTablesByRoom(data) {
    const rooms = {};

    // Iterate through each table in the data
    data.forEach((table) => {
      const roomId = table.roomId;

      // If the room doesn't exist in the rooms object, create it
      if (!rooms[roomId]) {
        rooms[roomId] = {
          roomId: roomId,
          roomName: table.roomName,
          tables: [],
        };
      }

      // Add the table to the corresponding room
      rooms[roomId].tables.push({
        tableId: table.id,
        tableName: table.name,
        capacity: table.capacity,
      });
    });

    // Convert the rooms object into an array
    const result = Object.values(rooms);

    return result;
  }

  const fetchTables = () => {
    tableSelectorContainer.innerHTML = '<span class="loader"></span>';
    document.querySelector(".infoText").innerText = "";
    dateInput.disabled = startTimeInput.disabled = endTimeInput.disabled = true;
    setTimeout(async () => {
      try {
        // Example: Fetch available tables based on date and time
        const date = dateInput.value;
        const startTime = startTimeInput.value;
        const endTime = endTimeInput.value;
        const response = await fetch(
          `/reservation_system/GetTablesServlet?date=` +
            date +
            `&startTime=` +
            startTime +
            `&endTime=` +
            endTime
        );
        const data = await response.json();

        // Handle data and populate tableSelectorContainer
        // Example: Render tables as buttons inside tableSelectorContainer
        const organizedRooms = organizeTablesByRoom(data);

        tableSelectorContainer.innerHTML = `
            	   <div id="customSelector" class=" ">
            	    	<div class="flex max-w-full allRooms flex-wrap gap-2">
            	    		
            	    	</div>
            	    </div>
               `;

        tableSelectorContainer.querySelector("#customSelector").innerHTML += `
	 	              <div class="mt-4 allTables gap-2 border-t border-gray-200 pt-6">
	 	          	     
	 	            </div>`;

        organizedRooms.forEach((room, index) => {
          tableSelectorContainer.querySelector(".allRooms").innerHTML += `
	            	   <input type="radio" id="${"${room.roomName}"}" name="roomSelector" class="hidden" ${'${index==0 && "checked"}'}>
            	      <label for="${"${room.roomName}"}" class="roomTab cursor-pointer bg-gray-500 hover:bg-green-500 transition text-white py-2 px-4 rounded-md w-auto flex items-center justify-center">${"${room.roomName}"}</label>
	               `;

          tableSelectorContainer.querySelector(
            ".allTables"
          ).innerHTML += `<div data-room-name="${"${room.roomName}"}" class='flex gap-2 flex-wrap ${'${index!=0 && "hidden"}'}'></div>`;

          room.tables.forEach((table) => {
            tableSelectorContainer.querySelector(
              `${'.allTables [data-room-name="${room.roomName}"]'}`
            ).innerHTML += `<input type="radio" id="${"${table.tableName}"}" name="tableSelector" class="hidden" data-table-id="${"${table.tableId}"}"> <label for="${"${table.tableName}"}" class="bg-gray-500 text-white py-2 px-4 rounded-md hover:bg-green-500 transition tableTab">${"${table.tableName}"} - ${"${table.capacity} seats"}</label>`;
          });
        });

        document.querySelector(".infoText").innerText = "Select Your Table";
        //Handlers:
        // Event listener for room selection
        document
          .querySelectorAll('input[name="roomSelector"]')
          .forEach((roomInput) => {
            roomInput.addEventListener("change", function () {
              const roomId = this.id;
              updateTablesDisplay(roomId);
            });
          });

        document
          .querySelectorAll('input[name="tableSelector"]')
          .forEach((tableInput) => {
            tableInput.addEventListener("change", function () {
              submittable.TableSet = true;
              checkIsSubmittable();
            });
          });

        // Function to update the display of tables based on selected room
        function updateTablesDisplay(roomId) {
          // Hide all table containers
          document
            .querySelectorAll("[data-room-name]")
            .forEach((tableContainer) => {
              tableContainer.classList.add("hidden");
            });

          // Show the selected room's table container
          const selectedTableContainer = document.querySelector(
            `[data-room-name="${"${roomId}"}"]`
          );
          selectedTableContainer.classList.remove("hidden");
        }
      } catch (error) {
        console.error("Error fetching tables:", error);
        tableSelectorContainer.innerHTML = `<p class='text-red-500 font-semibold text-center'>Something Went Wrong!</p>`;
        document.querySelector(".infoText").innerText =
          "Select a date and time for your reservation.";
      } finally {
        dateInput.disabled =
          startTimeInput.disabled =
          endTimeInput.disabled =
            false;
      }
    }, 1000);
  };

  // Event listener for changes in selected table

  // Event listener for changes in form inputs
  document.querySelectorAll("input").forEach((input) => {
    input.addEventListener("input", () => {
      // Example: Enable submit button if all fields are filled
      const isFormValid = validateForm();
      submitButton.disabled = !isFormValid;
    });
  });

  // Function to validate form fields
  function validateForm() {
    const date = document.getElementById("date").value;
    const startTime = document.getElementById("startTime").value;
    const endTime = document.getElementById("endTime").value;
    return date && startTime && endTime && startTime < endTime;
  }

  document
    .querySelector(".submitButton")
    .addEventListener("click", async (event) => {
      event.preventDefault();

      const date = dateInput.value;
      const startTime = startTimeInput.value;
      const endTime = endTimeInput.value;

      // Get the selected table ID
      const selectedTable = document.querySelector(
        'input[name="tableSelector"]:checked'
      );
      const tableId = selectedTable
        ? selectedTable.getAttribute("data-table-id")
        : "";

      const specialRequests = document.getElementById("specialRequests").value;

      if (!date || !startTime || !endTime || !tableId) {
        alert("Fields missing.");
        return;
      }
      setLoading(true);
      setTimeout(async () => {
        try {
          const response = await fetch(
            "/reservation_system/MakeReservationServlet",
            {
              method: "POST",
              headers: {
                "Content-Type": "application/x-www-form-urlencoded",
              },
              body: new URLSearchParams({
                date,
                startTime,
                endTime,
                tableId,
                notes: specialRequests,
              }),
            }
          );
          setLoading(false);

          if (response.ok) {
            //No server crashes, check response:
            const data = await response.json();

            if (data.status === "success") {
              window.location.href =
                "${pageContext.request.contextPath}/ReservationsServlet?reservationEdited=true";
            } else if (data.status === "USER_SESSION_EXPIRED") {
              window.location.href =
                "${pageContext.request.contextPath}/LoginServlet?notLoggedIn=true";
            } else {
              pushError(data.message);
            }
          } else {
            console.error("Error submitting reservation:", error);
            pushError("The reservation couldn't be submitted.");
          }
        } catch (error) {
          console.error("Error submitting reservation:", error);
          pushError("Something went wrong on the server. We're working on it!");
          setLoading(false);
        }
      }, 1000);
    });

  const pushError = (msg, success = false) => {
    const li = document.createElement("li");
    li.innerText = msg;
    li.classList.add("fadeup");
    if (!success) {
      li.classList.add("text-red-500");
    } else {
      li.classList.add("text-green-500");
    }
    document.querySelector(".submissionError").appendChild(li);
    setTimeout(() => {
      li.remove();
    }, 4000);
  };
</script>

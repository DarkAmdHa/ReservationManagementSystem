<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%> <%@ taglib tagdir="/WEB-INF/tags" prefix="h" %> <%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<h:header
  title="Analytics"
  bodyClasses="font-sans bg-gray-100 flex min-h-screen"
/>

<h:adminSidebar from="Analytics" />


<main class="flex-1 p-10">
    <h2 class="text-xl font-semibold mb-4">Analytics</h2>
    
    
	<div class='grid grid-cols-2 gap-4'>
	    <dl class=" w-full gap-px bg-gray-900/5" data-stat="peak-hours">
	  <div class=" bg-white px-4 py-10 sm:px-6 xl:px-8">
	   <div class='flex items-baseline justify-between pb-2'>
	       <dt class="text-sm font-medium leading-6 text-gray-500">Peak Hours</dt>
		    <div class='timeFrameSelect flex gap-2'>
		    	<button class='rounded-xl bg-red-500 text-white text-sm transition p-2' data-timeframe="today">Today</button> 
		    	<button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="week">This week</button> 
		    	<button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="month">This month</button> 
		  	    <button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="year">This year</button> 
		   	</div>
	   </div>
	
        <div>
          <canvas id="peakHoursChart" class='max-h-96' width="400" height="400"></canvas>
        </div>
	  </div>
	
	</dl>
	
	
   <dl class=" w-full gap-px bg-gray-900/5" data-stat="seats-booked">
	  <div class=" bg-white px-4 py-10 sm:px-6 xl:px-8  h-full">
	   <div class='flex items-baseline justify-between pb-2'>
	       <dt class="text-sm font-medium leading-6 text-gray-500">Seats Booked</dt>
		    <div class='timeFrameSelect flex gap-2'>
		    	<button class='rounded-xl bg-red-500 text-white text-sm transition p-2' data-timeframe="today">Today</button> 
		    	<button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="week">This week</button> 
		    	<button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="month">This month</button> 
		  	    <button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="year">This year</button> 
		   	</div>
	   </div>
	
        <div>
          <canvas id="reservationsByCapacityChart" class='max-h-96' width="400" height="400"></canvas>
        </div>
	  </div>
	
	</dl>
	


    </div>
    
    <div class='grid grid-cols-3 gap-4 py-4'>
    
        <dl class="mx-auto gap-px bg-gray-900/5" data-stat="reservations">
	        <div class=" bg-white px-4 py-10 sm:px-6 xl:px-8">
	            <div class='flex flex-wrap items-baseline justify-between'>
	                <dt class="text-sm font-medium leading-6 text-gray-500">Reservations</dt>
	                <div class='timeFrameSelect flex gap-2'>
	                    <button class='rounded-xl bg-red-500 text-white text-sm transition p-2' data-timeframe="today">Today</button> 
	                    <button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="week">This week</button> 
	                    <button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="month">This month</button> 
	                    <button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="year">This year</button> 
	                </div>
	            </div>
	            <p class='text-md font-semibold reservationCountText'>${reservationCount}</p>
	        </div>
	    </dl>
	    
         <dl class="mx-auto gap-px bg-gray-900/5" data-stat="cancellation-rate">
	        <div class=" bg-white px-4 py-10 sm:px-6 xl:px-8">
	            <div class='flex flex-wrap items-baseline justify-between'>
	                <dt class="text-sm font-medium leading-6 text-gray-500">Cancellation Rate</dt>
	                <div class='timeFrameSelect flex gap-2'>
	                    <button class='rounded-xl bg-red-500 text-white text-sm transition p-2' data-timeframe="today">Today</button> 
	                    <button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="week">This week</button> 
	                    <button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="month">This month</button> 
	                    <button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="year">This year</button> 
	                </div>
	            </div>
	            <p class='text-md font-semibold cancellationRateText'>${cancellationRate}%</p>
	        </div>
	    </dl>
	    
	    
	            <dl class="mx-auto gap-px bg-gray-900/5" data-stat="attendance-rate">
	        <div class=" bg-white px-4 py-10 sm:px-6 xl:px-8">
	            <div class='flex flex-wrap items-baseline justify-between'>
	                <dt class="text-sm font-medium leading-6 text-gray-500">Attendence Rate</dt>
	                <div class='timeFrameSelect flex gap-2'>
	                    <button class='rounded-xl bg-red-500 text-white text-sm transition p-2' data-timeframe="today">Today</button> 
	                    <button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="week">This week</button> 
	                    <button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="month">This month</button> 
	                    <button class='rounded-xl bg-gray-300 text-gray-800 text-sm transition hover:bg-red-500 hover:text-white p-2'  data-timeframe="year">This year</button> 
	                </div>
	            </div>
	            <p class='text-md font-semibold attendanceRateText'>${attendanceRate}%</p>
	        </div>
	    </dl>
    </div>
        <!-- Statistics -->

</main>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
	// Peak Hours Chart
	var peakHoursData =  [
		<c:forEach var="hourData" items="${peakHoursData}" varStatus="loop">
	{
	    hour: ${hourData.hour},
	    count: ${hourData.count}
	}<c:if test="${!loop.last}">,</c:if>
	</c:forEach>
	]
	//Peak Hours Chart
	
	// Extracting Labels and Counts from Data
	var peakHoursLabels = peakHoursData.map((hourData) => hourData.hour + "");
	
	var peakHoursLabels = peakHoursData.map((hourData) => {
	  if (hourData.hour < 9) return `0${"${hourData.hour}"}:00`;
	  else return `${"${hourData.hour}"}:00`;
	});
	var peakHoursCounts = peakHoursData.map((hourData) => hourData.count);
	
	// Get the maximum count in the data
	var maxCount = Math.max(...peakHoursCounts);
	
	// Creating Line Chart
	var peakHoursChartCanvas = document
	  .getElementById("peakHoursChart")
	  .getContext("2d");
	var peakHoursChart = new Chart(peakHoursChartCanvas, {
	  type: "bar",
	  data: {
	    labels: peakHoursLabels,
	    datasets: [
	      {
	        label: "Tables Booked",
	        data: peakHoursCounts,
	        fill: false,
	        borderColor: "rgba(255, 99, 132, 1)",
	        borderWidth: 1,
	      },
	    ],
	  },
	  options: {
	    scales: {
	      x: {
	        title: {
	          display: true,
	          text: "Hour of the Day",
	        },
	      },
	      y: {
	        title: {
	          display: true,
	          text: "Tables Booked",
	        },
	        beginAtZero: true,
	        suggestedMax: maxCount + 3,
	      },
	    },
	  },
	});
	// Reservations by Seat Capacity Chart
	var reservationsByCapacityData = {};
	<c:forEach var="entry" items="${mostReservedSeats}">
    	var key = "${fn:escapeXml(entry.key)}";
	  	var value = ${entry.value};
	  	reservationsByCapacityData[key] = value;
	</c:forEach>;
	
	var seatCapacities = Object.keys(reservationsByCapacityData);
	var reservationCounts = Object.values(reservationsByCapacityData);
	
	var reservationsByCapacityChartCanvas = document
	  .getElementById("reservationsByCapacityChart")
	  .getContext("2d");
	var reservationsByCapacityChart = new Chart(reservationsByCapacityChartCanvas, {
	  type: "pie",
	  data: {
	    labels: seatCapacities,
	    datasets: [
	      {
	        label: "Reservations by Seat Capacity",
	        data: reservationCounts,
	        backgroundColor: [
	          "rgba(255, 99, 132, 0.2)",
	          "rgba(54, 162, 235, 0.2)",
	          "rgba(255, 206, 86, 0.2)",
	          "rgba(75, 192, 192, 0.2)",
	          "rgba(153, 102, 255, 0.2)",
	          "rgba(255, 159, 64, 0.2)",
	        ],
	        borderColor: [
	          "rgba(255, 99, 132, 1)",
	          "rgba(54, 162, 235, 1)",
	          "rgba(255, 206, 86, 1)",
	          "rgba(75, 192, 192, 1)",
	          "rgba(153, 102, 255, 1)",
	          "rgba(255, 159, 64, 1)",
	        ],
	        borderWidth: 1,
	      },
	    ],
	  },
	});
	
	// Function to handle the POST request
	const sendAnalyticsRequest = async (timeframe, stat) => {
	  setLoading(true);
	  try {
	    const response = await fetch("/reservation_system/Analytics", {
	      method: "POST",
	      headers: {
	        "Content-Type": "application/json",
	      },
	      body: JSON.stringify({
	        timeFrame: timeframe,
	        stat: stat,
	      }),
	    });
	
	    if (response.ok) {
	      const data = await response.json();
	      // Process response data if needed
	      if (data.status == "success") {
	  
	        if (stat === "reservations") {
	          document.querySelector(".reservationCountText").innerText = parseInt(data.data);
	        } else if (stat === "cancellation-rate") {
	          document.querySelector(".cancellationRateText").innerText = parseInt(data.data).toFixed(2) + "%";
	        } else if (stat === "attendance-rate") {
	          document.querySelector(".attendanceRateText").innerText = parseInt(data.data).toFixed(2) + "%";
	        } else if (stat === "peak-hours") {
	            const newData = JSON.parse(data.data);
	          // Extracting Labels and Counts from Data
	          const newPeakHoursLabels = newData.map((hourData) => {
	            if (hourData.hour < 9) return `0${"${hourData.hour}"}:00`;
	            else return `${"${hourData.hour}"}:00`;
	          });
	          const newPeakHoursCounts = newData.map((hourData) => hourData.count);
	
	          // Get the maximum count in the data
	          const newMaxCount = Math.max(...newPeakHoursCounts);
	
	          var peakHoursChartCanvas = document
	            .getElementById("peakHoursChart")
	            .getContext("2d");
	
	          // Creating Line Chart
	          peakHoursChart.destroy();
	          peakHoursChart = new Chart(peakHoursChartCanvas, {
	            type: "bar",
	            data: {
	              labels: newPeakHoursLabels,
	              datasets: [
	                {
	                  label: "Tables Booked",
	                  data: newPeakHoursCounts,
	                  fill: false,
	                  borderColor: "rgba(255, 99, 132, 1)",
	                  borderWidth: 1,
	                },
	              ],
	            },
	            options: {
	              scales: {
	                x: {
	                  title: {
	                    display: true,
	                    text: "Hour of the Day",
	                  },
	                },
	                y: {
	                  title: {
	                    display: true,
	                    text: "Tables Booked",
	                  },
	                  beginAtZero: true,
	                  suggestedMax: newMaxCount + 3,
	                },
	              },
	            },
	          });
	        } else if (stat === "seats-booked") {
	            const newData = JSON.parse(data.data);
	            
	        	var newSeatCapacities = Object.keys(newData);
	        	var newReservationCounts = Object.values(newData);
	        	
	        	
	        	var reservationsByCapacityChartCanvas = document
	        	  .getElementById("reservationsByCapacityChart")
	        	  .getContext("2d");
	        	
	        	console.log(newSeatCapacities);
	        	
	        	
	        	reservationsByCapacityChart.destroy();
	        	reservationsByCapacityChart = new Chart(reservationsByCapacityChartCanvas, {
	        	  type: "pie",
	        	  data: {
	        	    labels: newSeatCapacities,
	        	    datasets: [
	        	      {
	        	        label: "Reservations by Seat Capacity",
	        	        data: newReservationCounts,
	        	        backgroundColor: [
	        	          "rgba(255, 99, 132, 0.2)",
	        	          "rgba(54, 162, 235, 0.2)",
	        	          "rgba(255, 206, 86, 0.2)",
	        	          "rgba(75, 192, 192, 0.2)",
	        	          "rgba(153, 102, 255, 0.2)",
	        	          "rgba(255, 159, 64, 0.2)",
	        	        ],
	        	        borderColor: [
	        	          "rgba(255, 99, 132, 1)",
	        	          "rgba(54, 162, 235, 1)",
	        	          "rgba(255, 206, 86, 1)",
	        	          "rgba(75, 192, 192, 1)",
	        	          "rgba(153, 102, 255, 1)",
	        	          "rgba(255, 159, 64, 1)",
	        	        ],
	        	        borderWidth: 1,
	        	      },
	        	    ],
	        	  },
	        	});
	        	
	        	
	        	
	        }
	        
	        handleChangeUI(timeframe,stat);
	      } else {
	        if (data.message) {
	          alert(data.message);
	        } else {
	          alert("Something went wrong.");
	        }
	      }
	    } else {
	      throw new Error("Failed to send analytics request");
	    }
	  } catch (error) {
	    console.error("Error sending analytics request:", error);
	    // Handle error
	  } finally {
		  setTimeout(()=>{
			    setLoading(false);
		  }, 450)

	  }
	};
	
	// Function to handle time frame select button click
	const handleTimeFrameSelect = (timeframe, stat) => {
	  sendAnalyticsRequest(timeframe, stat);
	};
	
	const handleChangeUI = (timeframe,stat) => {
	  const button = document.querySelector(`[data-stat="${'${stat}'}"] button[data-timeframe="${'${timeframe}'}"]`);
	  const currentActive = button.parentElement.querySelector('.bg-red-500');
	  currentActive.classList.remove('bg-red-500', 'text-white');
	  currentActive.classList.add('bg-gray-300', 'text-gray-800', 'hover:bg-red-500', 'hover:text-white')
	  
  	  button.classList.add('bg-red-500', 'text-white');
	  button.classList.remove('bg-gray-300', 'text-gray-800', 'hover:bg-red-500', 'hover:text-white')
	};
	
	// Event listeners for time frame select buttons
	document.querySelectorAll(".timeFrameSelect button").forEach((button) => {
	  button.addEventListener("click", () => {
	    const timeframe = button.getAttribute("data-timeframe");
	    const stat = button.closest("[data-stat]").getAttribute("data-stat");
	    handleTimeFrameSelect(timeframe, stat);
	  });
	});


    
</script>






</body>
</html>

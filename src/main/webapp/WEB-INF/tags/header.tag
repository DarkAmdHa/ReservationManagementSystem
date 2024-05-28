<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="bodyClasses" required="false" %>
<%@ attribute name="additionalHeadersStuff" required="false" %>

<html lang="en" class='overflow-hidden'>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css"
      rel="stylesheet"
    />
      <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css"
      integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA=="
      crossorigin="anonymous"
      referrerpolicy="no-referrer"
    />
    <title>${title}</title>
    
    ${additionalHeadersStuff}
    
    
    <style>
    
    
body:after {
    content: '';
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: #000000b0;
    transition: 0.5s ease;
    opacity: 0;
    transition: 0.5s ease;
    pointer-events: none;
    z-index: 99;

}

body.isLoading:after {
    backdrop-filter: blur(4px);
    opacity: 1;
}

body.isLoading {
    pointer-events: none;
}

span.loader {}

body > .loader {
    transition: 0.5s ease;
    opacity: 0;
    position: absolute;
    left: 49%;
    top: 45%;
    z-index: 9;
    width: 110px;
    height: 110px;
    pointer-events: none;
    z-index: 100;
}

body.isLoading > .loader {
    opacity: 1;
}

    
    .modal {
    position: absolute;
    width: 100vw;
    height: 100vh;
    top: 0;
    left: 0;
    transition: 0.5s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    pointer-events: none;	
 	opacity: 0;	

}

.modal:before {
    content: '';
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    background: #00000091;
   	z-index: 0;
    backdrop-filter: blur(4px);
}
.modal.view{
pointer-events: visible;
     opacity: 1;
}


.modal .modalForm {
    z-index: 9;
    position: relative;
    transition: 0.5s ease;
    transform: translateY(-20px);
    opacity: 0;
    box-shadow: 1px 5px 20px rgb(0 0 0 / 9%);
    min-width: 450px;
    pointer-events: none;
}

.modal .modalForm.view {
	pointer-events: visible;	
    transform: translateY(0);
    opacity: 1;
}


    .loader {
    width: 48px;
    height: 48px;
    border: 5px solid #FFF;
    border-bottom-color: rgb(34 197 94);
    border-radius: 50%;
    display: inline-block;
    box-sizing: border-box;
    animation: rotation 1s linear infinite;
    }
    .fadeUp {
    transition: 0.5s ease;
    animation: fadeup 0.5s ease-in-out forwards;
    opacity: 0;
    transform: translateY(10px);
}

@keyframes fadeup {
    0%{
        opacity: 0;
        transform: translateY(10px);
    }
    100%{
        opacity: 1;
        transform: translateY(0);
    }
}
.resEditMsg {
    width: fit-content;

}
    
    .submitButton:disabled{
    	opacity: 0.60;
    	pointer-events: none;
    }
    .min-w-custom-100{
    min-width: 100px;}
    
      input:checked + .roomTab {
        background: rgba(16,185,129,1);
      }
               
    input:checked + .tableTab{
    	background: rgba(16,185,129,1);
    }

    @keyframes rotation {
    0% {
        transform: rotate(0deg);
    }
    100% {
        transform: rotate(360deg);
    }
    } 
    
     .toggle-switch {
    position: relative;
    display: inline-block;
    width: 32px;
    height: 18px;
    background-color: #ccc;
    border-radius: 34px;
    cursor: pointer;
    transition: background-color 0.2s;
}

.toggle-slider {
    position: absolute;
    top: 2px;
    left: 3px;
    width: 13px;
    height: 13px;
    background-color: white;
    border-radius: 50%;
    transition: transform 0.2s;
}

/* When checked, change background color and move the slider */
.statusToggle:checked + .toggle-switch .toggle-slider,
.roleToggle:checked + .toggle-switch .toggle-slider {
    transform: translateX(14px);
    background-color: #10b981;
}

.

.toggle-switch.disapproved .toggle-slider {
    background-color: #ef4444;
}

.toggle-switch {
    background-color: #dadada; /* Red when not checked */
    margin-top: 5px;
}

p.status {
    width: 90px;
    font-size: 13px;
}

main{
overflow: auto;
}
    
    </style>
  </head>

  <body
    class="${bodyClasses}"
    cz-shortcut-listen="true"
  >
  <script>const setLoading = (state)=>{
	  if(state){
		  document.querySelector('body').classList.add('isLoading')
	  }else{
		  document.querySelector('body').classList.remove('isLoading')
	  }
  }</script>
  
  
  <span class="loader"></span>
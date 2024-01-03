<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="bodyClasses" required="false" %>
<%@ attribute name="additionalHeadersStuff" required="false" %>

<html lang="en">
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
    .loader {
    width: 48px;
    height: 48px;
    border: 5px solid #FFF;
    border-bottom-color: #FF3D00;
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
    </style>
  </head>

  <body
    class="${bodyClasses}"
    cz-shortcut-listen="true"
  >
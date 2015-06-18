<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%--
  Created by
  User: Velychko A.
  Date: 05.05.2015
--%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Delete HomeTasks</title>
    <a href="/admin/toHomeTasks">Go Back</a>
    <style type="text/css">
   BODY {
    background: white;
   }
   TABLE {
    width: 800px;
    border-collapse: collapse;
    border: 2px solid white;
   }
   TD, TH {
    padding: 3px;
    border: 1px solid maroon; 
    text-align: center; 
   }
  </style>
</head>
<body>
<div class="container">
<h3>This Home Task was executed by such students:</h3>

<table class="table table-striped">
    
    <thead>
        <tr>
        	<td><b></b></td>
            <td><b>ID</b></td>
            <td><b>Name</b></td>
            <td><b>Phone</b></td>
        </tr>
        </thead>
					<%int i = 1;%>
            	
            	 <c:forEach items="${clients}" var="client">
            <tr>
            <%out.println("<td>"+i+"</td>");%>
            
            	<td>${client.id}</td>
            	<td>${client.name}</td>  
            	<td>${client.phone}</td>       	

                 </tr>
                 <% i++; %>
        </c:forEach>
    </table>
        
         
         

   
    
    
   
    
    
</div>
</body>
</html>
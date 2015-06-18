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
<h3>Selected home task(s) contain execution results inside which will be deleted too! Are you sure you want to delete?</h3>

<form class="form-group" action="/admin/deleteTasksAnyway" style="width: 140px; ">

<table class="table table-striped">
    
    <thead>
        <tr>
        	<td><b></b></td>
            <td><b>ID</b></td>
            <td><b>Task Text</b></td>
        </tr>
        </thead>
					<%int i = 1;%>
            	
            	 <c:forEach items="${homeTasks}" var="homeTask">
            <tr>
            <%out.println("<td>"+i+"</td>");%>
            
            	<td>${homeTask.id}</td>
            	<td>${homeTask.taskText}</td>        	

                 </tr>
                 <input  class="form-group" type="text" name="id" value="${homeTask.id}" hidden="true" />
                 <% i++; %>
        </c:forEach>
    </table>

        <div class="form-group"><input type="submit" value="Delete Anyway"></div>
        </form>
        
         
         

   
    
    
   
    
    
</div>
</body>
</html>
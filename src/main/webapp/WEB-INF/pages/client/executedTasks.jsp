<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%--
  Created by
  User: Velychko A.
  Date: 05.05.2015
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<a href="javascript:history.back()">Go Back</a>
<title>Executed Tasks Student</title>
<style type="text/css">
   BODY {
    background: white;
   }
   TABLE {
    width: 1000px;
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
    <h3>Execution results of "${clientName}"</h3>


    <% int i = 1; %>

    <table class="table table-striped">
    
    <thead>
        <tr>
        	<td><b></b></td>
            <td><b>Task Number</b></td>
            <td><b>Task conditions</b></td>
            <td><b>Quantity of attempts</b></td>
            <td><b>Difficulty level</b></td>
            <td><b>Executed</b></td>
        </tr>
        </thead>

            	
            	 <c:forEach items="${homeTaskResults}" var="htr">
            <tr>
            <%out.println("<td>"+i+"</td>");%>
            	<td>${htr.homeTask.id}</td>
            	<td>${htr.homeTask.taskText}</td>
            	<td>${htr.attemptQuantity}</td>
            	<td>${htr.homeTask.difficultyLevel}</td>
            	<c:if test="${htr.isExecuted == true}"><td>YES</td></c:if>
            	<c:if test="${htr.isExecuted == false}"><td>NO</td></c:if>       	
                 </tr>
                 <% i++; %>
        </c:forEach>
        
        
    </table>
</div>

</body>
</html>
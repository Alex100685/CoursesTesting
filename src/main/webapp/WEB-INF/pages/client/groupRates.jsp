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
<title>Student Rating</title>
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
    <h3>Rating</h3>


    <% int i = 1; %>

    <table class="table table-striped">
    
    <thead>
        <tr>
            <td><b>Position</b></td>
            <td><b>Student`s Name</b></td>
            <td><b>Score</b></td>
            <td><b>Executed Tasks</b></td>
        </tr>
        </thead>

            	
            	 <c:forEach items="${Ratings}" var="rating">
            <tr>
            <%out.println("<td>"+i+"</td>");%>
            	<td>${rating.clientName}</td>
            	<td>${rating.score}</td>
            	<td><a href="/client/executedTasks?id=${rating.clientId}">Executed Tasks</a></td>         	

                 </tr>
                 <% i++; %>
        </c:forEach>
    </table>
</div>

</body>
</html>
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
<title>Home Tasks</title>
</head>
<body>

<div class="container">
    <h3>Tasks List</h3>


    

    <table class="table table-striped">
    
    
    <thead>
        <tr>
        <td><b></b></td>
            <td><b><form class="form-inline" role="form" action="/admin/searchById" method="post" style="width: 90px; ">
        
        <input type="submit" value="Search"><input type="text" class="form-control" name="pattern" placeholder="by ID" style="width: 76px; ">
    </form></b></td>
            <td><b><form class="form-inline" role="form" action="/admin/searchByText" method="post" style="width: 90px; ">
        
        <input type="submit" value="Search"><input type="text" class="form-control" name="pattern" placeholder="by Text" style="width: 110px; ">
    </form></b></td>
           
            <td><b></b></td>
            
        </tr>
        </thead>
        
        	
      <thead>
        <tr>
        	<td style="width: 60px; "><b>Select</b></td>
            <td style="width: 70px; "><b>ID.</b>

<% 
if(request.getAttribute("javax.servlet.forward.servlet_path").toString().endsWith("/sortById")){
out.println("<form action=\"/admin/sortByIdRev\" style=\"width: 53px; \">");
out.println("<button type=\"submit\">sort</button>");
out.println("</form>");
}
else{
out.println("<form action=\"/admin/sortById\" style=\"width: 53px; \">");
out.println("<button type=\"submit\">sort</button>");
out.println("</form>");
}
%>
            </td>
            
            <td style="width: 210px; "><b>Task Text</b>
            
          </td>  
        
<td style="width: 196px; "><b>Correct Result Text</b>
      
            </td>
        
<td style="width: 196px; "><b>Difficulty Level</b>
      <% 
if(request.getAttribute("javax.servlet.forward.servlet_path").toString().endsWith("/sortByDifficultyLevel")){
out.println("<form action=\"/admin/sortByDifficultyLevelRev\" style=\"width: 53px; \">");
out.println("<button type=\"submit\">sort</button>");
out.println("</form>");
}
else{
out.println("<form action=\"/admin/sortByDifficultyLevel\" style=\"width: 53px; \">");
out.println("<button type=\"submit\">sort</button>");
out.println("</form>");
}
%>
            </td>
            
			<td><b>Edit</b></td>   
			<td><b>Executed By...</b></td>   
        </tr>
        </thead>
        
        
        <form method="post" action="/admin/deleteTask">
            	
            	 <c:forEach items="${homeTasks}" var="homeTask">
            <tr>
            <td><input type="checkbox" name="id []" value="${homeTask.id}"></td>
            	<td>${homeTask.id}</td>
            	<td>${homeTask.taskText}</td>
            	<td>${homeTask.taskCorrectResult}</td>
            	<td>${homeTask.difficultyLevel}</td>
            	<td><a href="/admin/edit?id=${homeTask.id}">Edit</a></td>
            	<td><a href="/admin/executedBy?id=${homeTask.id}">Students Executed...</a></td>        	

                 </tr>
        </c:forEach>
    </table>
    
<p><input type="submit" value="Delete"></p>
  </form>
  
    <form class="form-inline" role="form" action="/admin/createNewAssignment" method="post">
        <input type="submit" value="Create New Task">
    </form>
</div>

</body>
</html>
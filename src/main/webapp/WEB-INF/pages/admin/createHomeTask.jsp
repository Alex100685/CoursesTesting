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
<title>Insert title here</title>
</head>
<body>

 <form role="form" class="form-horizontal" action="/admin/saveNewAssignment" method="post">
        <div class="form-group"><h3>New Task</h3></div>
		
		Insert the text of new task:      
        <div class="form-group">
		<textarea rows="10" cols="45" input type="text" class="form-control" required="required" pattern="[A-Za-z0-9]{1,1000}" title="Must be not empty" name="taskText" placeholder="Task Text" style="width: 429px; "></textarea>
		</div>
		
		Insert the text of correct result:      
        <div class="form-group">
		<textarea rows="10" cols="45" input type="text" class="form-control" required="required" pattern="[A-Za-z0-9]{1,1000}" title="Must be not empty" name="taskResult" placeholder="Task Result" style="width: 429px; "></textarea>
		</div>
		
		Insert the difficulty level of task: 
		<div class="form-group"><input type="text" pattern = "[0-5]{1}$" title="Must contain only one number from 1 to 5" required = "required" class="form-control" name="difficultyLevel" placeholder="Level of difficulty" style="width: 429px; "></div>
		

        <div class="form-group"><input type="submit" value="Save"></div>
    </form>

</body>
</html>
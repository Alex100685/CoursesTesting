<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page language="java" pageEncoding="UTF-8"%>
    <%--
  Created by
  User: Velychko A.
  Date: 05.05.2015
--%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<a href="javascript:history.back()">Go Back</a>
    <title>Select your group</title>
</head>
<body>
<div class="container">
    <form role="form" class="form-horizontal" action="/client/groupRating" method="post">
        <div class="form-group"><h3>Select group to see rating</h3></div>
        
<div>
<select class="form-group" name="selectedGroup" style="width: 304px; height: 35px">
<c:forEach items="${Groups}" var="group">
<option>${group.name}</option>
</c:forEach>
</select>
</div>

        <div class="form-group"><input type="submit"  value="Select"></div>
    </form>
</div>
</body>
</html>
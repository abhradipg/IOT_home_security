<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="forms.css">
</head>
<body>
<%
    if(session.getAttribute("uname")==null)
    {
    	response.sendRedirect("login.jsp");
    }
%>
<fieldset>
<legend>Remove User:</legend>
<form action="delete" method ="post">
<label for="uname">Enter Username</label>
<input type ="text" name="username" id ="uname" maxlength="40" required></br>
<label for="pass">Enter Password</label>
<input type ="password" name="password" id ="pass" maxlength="40" required></br>
<input type="submit" value ="Remove">
</form>
</fieldset>
</body>
</html>
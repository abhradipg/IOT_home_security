<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="main.css">
</head>
<body>
<%
    if(session.getAttribute("uname")==null)
    {
    	response.sendRedirect("login.jsp");
    }
%>
<form action="Unlock" method="post">
<input type="submit" value="Unlock">
</form>
<hr>
<form action="New_User" method="post">
<input type="submit" value="New User">
</form>
<hr>
<form action="Remove_User" method="post">
<input type="submit" value="Remove User">
</form>
<hr>
<form action="Mobile_Notification" method="post">
<input type="submit" value="Mobile Notification">
</form>
<hr>
<form action="Generate_OTP" method="post">
<input type="submit" value="Generate OTP">
</form>
<hr>
<form action="History" method="post">
<input type="submit" value="History">
</form>
<hr>
<a href="main.jsp" target="_blank">
<input type="button" value="Live Feed">
</a>
<hr>
<form action="Logout" method="post">
<input type="submit" value="Logout">
</form>
</body>
</html>
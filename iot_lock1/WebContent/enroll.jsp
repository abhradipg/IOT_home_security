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
<legend>New User Enrollment:</legend>
<form action="Enrollment" method="post">
<label for="uname">Enter New Username</label>
<input type ="text" name="nusername" id ="uname" required></br>
<label for="pass">Enter New Password</label>
<input type ="password" name="npassword" id ="pass" required></br>
<input type="Submit" value ="submit">
</form>
</fieldset>
</body>
</html>
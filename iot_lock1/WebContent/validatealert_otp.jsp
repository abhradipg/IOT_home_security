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
<legend>Check OTP:</legend>
<form action="check_alert_otp" method="post">
<label for="otp">Enter OTP</label>
<input type ="text" name="otp" id ="otp" maxlength ="4" required></br>
<input type="submit" value="Submit">
<a href="main.jsp" target="_self">
<input type="button" value="Cancel">
</a>
</form>
</fieldset>
</body>
</html>
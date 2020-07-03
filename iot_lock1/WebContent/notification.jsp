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
<legend>Alert Settings:</legend>
<form action="alert_otp_gen" method="post">
<label for="mob_no">Enter Mobile Number</label>
<input type ="text" name="mobile_no" id ="mob_no" maxlength ="10" required></br>
<p>Want Mobile Notification?</p>
<input type ="radio" name="enable" id ="yes" value="yes" required>
<label for="yes">YES</label></br>
<input type ="radio" name="enable" id ="no" value="no" required>
<label for="enable">NO </label></br>
<input type="submit" value="Submit">
</form>
</fieldset>
</body>
</html>
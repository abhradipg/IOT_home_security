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
<fieldset>
<legend>Login:</legend>
<form action="Login" method ="post">
<label for="uname">Enter Username</label>
<input type ="text" name="username" id ="uname" maxlength="40" required></br>
<label for="passm">Enter Password</label>
<input type ="password" name="password" maxlength="40" id ="pass" required></br>
<input type="submit" value ="Login">
</form>
</fieldset>
<fieldset>
<legend>Unlock Using OTP:</legend>
<form action="otp_unlock" method ="post">
<label for="otp">Enter OTP</label>
<input type ="text" name="otp" id ="otp" maxlength="4" required></br>
<input type="submit" value ="Unlock">
</form>
</fieldset>
</body>
</html>
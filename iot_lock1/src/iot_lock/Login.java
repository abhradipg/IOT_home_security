package iot_lock;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Login")
public class Login extends HttpServlet 
{
	
	boolean validate(String uname,String pass) throws Exception
	{
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String usernam="root";
		String passwor="password";
		String query= "select * from user_info";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,usernam,passwor);
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(query);
		while(rs.next())
		{
			
		    if(uname.equals(rs.getString("username"))&&pass.equals(rs.getString("pasword")))
		    {
			    rs.close();
			    st.close();
			    return true;
		    }
		}
		rs.close();
		st.close();
	    return false;
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	{   
		try {
		String uname=request.getParameter("username");
		String pass=request.getParameter("password");
		if(validate(uname,pass))
		    {
		    	HttpSession session = request.getSession();
			    session.setAttribute("uname",uname);
			    session.setAttribute("pass", pass);
			    response.sendRedirect("main.jsp");
		    }
		else
	    response.sendRedirect("login.jsp");
	      }
	catch(Exception ex)
		{
		System.out.println(ex);
		}
	}
}


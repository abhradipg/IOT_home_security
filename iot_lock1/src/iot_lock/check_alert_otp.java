package iot_lock;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/check_alert_otp")
public class check_alert_otp extends HttpServlet {
       
	boolean validateotp(String otp) throws Exception
	{
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String usernam="root";
		String passwor="password";
		String query= "select * from alert_otp";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,usernam,passwor);
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(query);
		while(rs.next())
		{
			
		    if(otp.equals(rs.getString("otp")))
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
	
	String getmobileno(String otp) throws Exception
	{
		String mobno="0";
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String usernam="root";
		String passwor="password";
		String query= "select * from alert_otp";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,usernam,passwor);
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(query);
		while(rs.next())
		{
			
		    if(otp.equals(rs.getString("otp")))
		    {
			    mobno=rs.getString("num");
		    	rs.close();
			    st.close();
			    return mobno;
		    }
		}
		return mobno;
	}
	
	String getenable(String otp) throws Exception
	{
		String enable="d";
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String usernam="root";
		String passwor="password";
		String query= "select * from alert_otp";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,usernam,passwor);
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(query);
		while(rs.next())
		{
			
		    if(otp.equals(rs.getString("otp")))
		    {
			    enable=rs.getString("en");
		    	rs.close();
			    st.close();
			    return enable;
		    }
		}
		rs.close();
		st.close();
	    return enable;
	}
	
	void insert(String mobile,String enable) throws Exception
	{
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String username="root";
		String password="password";
		String query= "insert into  alert (ind,num,en) values (?,?,?)";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,username,password);
		PreparedStatement st=con.prepareStatement(query);
		st.setInt(1, 1);
		st.setString(2, mobile);
		st.setString(3, enable);
		int no =st.executeUpdate();
		System.out.print(no);
		st.close();
		con.close();
	}
	
	void removealert() throws Exception
	{
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String username="root";
		String password="password";
		String query= "delete from alert where ind = ?";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,username,password);
		PreparedStatement st=con.prepareStatement(query);
		st.setInt(1, 1);
		int no =st.executeUpdate();
		System.out.print(no);
		st.close();
		con.close();
	}
	
	void remove(String otp) throws Exception
	{
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String username="root";
		String password="password";
		String query= "delete from alert_otp where otp = ?";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,username,password);
		PreparedStatement st=con.prepareStatement(query);
		st.setString(1, otp);
		int no =st.executeUpdate();
		System.out.print(no);
		st.close();
		con.close();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	{
	try {
		HttpSession session = request.getSession();
		if(session.getAttribute("uname")==null)
	    {
	    	response.sendRedirect("login.jsp");
	    }
		else
		{
			String otp=request.getParameter("otp");
			if(validateotp(otp))
			{
				String mobno=getmobileno(otp);
				String enable=getenable(otp);
				removealert();
				insert(mobno,enable);
				remove(otp);
				response.sendRedirect("main.jsp");
			}
			else
			{
				response.sendRedirect("validatealert_otp.jsp");
			}
		}
		
	}
	catch(Exception ex)
	{
    	System.out.println(ex);
	}
}
}

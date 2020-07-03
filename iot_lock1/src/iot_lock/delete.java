package iot_lock;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fazecast.jSerialComm.SerialPort;

import java.sql.*;
import java.util.Scanner;
@WebServlet("/delete")
public class delete extends HttpServlet {
	
	String getnumber() throws Exception
	{
		String num;
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String usernam="root";
		String passwor="password";
		String query= "select * from alert";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,usernam,passwor);
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(query);
		rs.next();
		System.out.print(rs.getString("en"));
		if(rs.getString("en").equals("e"))
		{
			num=rs.getString("num");
			rs.close();
    		st.close();
    		return num;
		}
		else
		{
			num="d";
			rs.close();
    		st.close();
    		return num;
		}
	}
	
	int validate(String uname,String pass) throws Exception
	{
		int iid;
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
			    iid=rs.getInt("id");
		    	rs.close();
			    st.close();
			    return iid;
		    }
		}
		rs.close();
		st.close();
	    return 0;
	}
	
	void removeuser(int id) throws Exception
	{
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String username="root";
		String password="password";
		String query= "delete from user_info where id = ?";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,username,password);
		PreparedStatement st=con.prepareStatement(query);
		st.setInt(1, id);
		int no =st.executeUpdate();
		System.out.print(no);
		st.close();
		con.close();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session=request.getSession();
			String uname=request.getParameter("username");
			String pass=request.getParameter("password");
			int id=validate(uname,pass);
			if(id!=0)
			    {
			    	removeuser(id);
			    	Integer rid =id;
			    	SerialPort s= SerialPort.getCommPort("COM13");
			        s.setComPortParameters(9600, 8, 1, 0);
			        s.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING|SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
			        if (s.openPort())
			        {
			            System.out.println("Port is opened");
			        }
			        else
			        {
			            System.out.println("Port in not opened");
			        }
			        Thread.sleep(3000);
			        s.getOutputStream().write('d');
	                s.getOutputStream().flush();
	                System.out.print("del");
			        Scanner data = new Scanner (s.getInputStream());
			    	  if(data.nextLine().equals("ok"))
			        	{System.out.print("ok");
			        	}		        
			        //data.close();
			        String neid=rid.toString();
			        char inpu;
			        for(int i=0;i<neid.length();i++)
			        {
			        	  inpu=neid.charAt(i);
			        	  s.getOutputStream().write(inpu);
			        }
	                s.getOutputStream().flush();
			        System.out.print(neid);
			        if(data.nextLine().equals("ok"))
		        	{System.out.print("ok");	
		        	}
		            data.close();
		            String num= getnumber();
		            System.out.println(num);
			        s.getOutputStream().write(num.getBytes());
		            s.getOutputStream().flush();
			        if(s.closePort())
			        {
			            System.out.println("Port is closed");
			        }
			        else
			        {
			            System.out.println("Port is not closed");
			        }
			    	session.removeAttribute("uname");
					session.invalidate();
					response.sendRedirect("login.jsp");
			    }
			else
		    response.sendRedirect("invalid.jsp");
		      }
		catch(Exception ex)
			{
			System.out.println(ex);
			}
	}
}

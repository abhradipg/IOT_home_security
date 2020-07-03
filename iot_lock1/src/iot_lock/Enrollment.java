package iot_lock;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import com.fazecast.jSerialComm.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Enrollment
 */
@WebServlet("/Enrollment")
public class Enrollment extends HttpServlet {
	
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
	
	int getid() throws Exception
	{
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String usernam="root";
		String passwor="password";
		String query= "select max(id) as nid from user_info";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,usernam,passwor);
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(query);
		rs.next();
		int newid =rs.getInt("nid");
		newid+=1;
		return (newid);
	}
	
	void insert(int nid,String uname,String pass) throws Exception
	{
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String username="root";
		String password="password";
		String query= "insert into user_info (id,username,pasword) values (?,?,?)";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,username,password);
		PreparedStatement st=con.prepareStatement(query);
		st.setInt(1, nid);
		st.setString(2, uname);
		st.setString(3, pass);
		int no =st.executeUpdate();
		System.out.print(no);
		st.close();
		con.close();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			if(session.getAttribute("uname")==null)
		    {
		    	response.sendRedirect("login.jsp");
		    }
			else
			{
				String uname=request.getParameter("nusername");
				String pass=request.getParameter("npassword");
				Integer newid=getid();
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
		        s.getOutputStream().write('e');
                s.getOutputStream().flush();
                System.out.print("enrl");
		       Scanner data = new Scanner (s.getInputStream());
		    	  if(data.nextLine().equals("ok"))
		        	{System.out.print("ok");
		        		}
		        	
		        
		        //data.close();
		        String neid=newid.toString();
		        char inpu;
		        for(int i=0;i<neid.length();i++)
		        {
		        	  inpu=neid.charAt(i);
		        	  s.getOutputStream().write(inpu);
		        }
                s.getOutputStream().flush();
		        System.out.print(newid);
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
				insert(newid,uname,pass);
				response.sendRedirect("main.jsp");
			}
		}
		catch (Exception ex)
		{
			System.out.print(ex);
		}
	}

}

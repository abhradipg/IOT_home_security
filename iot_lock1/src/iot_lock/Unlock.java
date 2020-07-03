package iot_lock;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Scanner;
import com.fazecast.jSerialComm.*;
import javax.servlet.http.HttpSession;

@WebServlet("/Unlock")
public class Unlock extends HttpServlet {
	int getid(String uname,String pass) throws Exception
	{
		int id;
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
			    id=rs.getInt("id");
		    	rs.close();
			    st.close();
			    return id;
		    }
		}
		rs.close();
		st.close();
	    return 0;
	}
	void insert(int nid,String tim) throws Exception
	{
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String username="root";
		String password="password";
		String query= "insert into access_info (id,atype,atime) values (?,?,?)";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,username,password);
		PreparedStatement st=con.prepareStatement(query);
		st.setInt(1, nid);
		st.setString(2, "Server");
		st.setString(3, tim);
		int no =st.executeUpdate();
		System.out.print(no);
		st.close();
		con.close();
	}
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session = request.getSession();
		if(session.getAttribute("uname")==null)
	    {
	    	response.sendRedirect("login.jsp");
	    }
		else
		{
			try
			{
			String uname=(String) session.getAttribute("uname");
			String pass=(String) session.getAttribute("pass");
			int eid =getid(uname,pass);
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
	        s.getOutputStream().write('u');
            s.getOutputStream().flush();
            Scanner data = new Scanner (s.getInputStream());
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
            SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date =new Date();
            String tim=formatter.format(date);
            insert(eid,tim);
            response.sendRedirect("main.jsp");
			}
			catch(Exception ex)
			{
				System.out.println(ex);
			}
		}
	}

}

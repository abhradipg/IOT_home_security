package iot_lock;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fazecast.jSerialComm.SerialPort;

@WebServlet("/unlock_otp_gen")
public class unlock_otp_gen extends HttpServlet {
	
	public static void insert(int id,String num,String otp) throws Exception
	{
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String username="root";
		String password="password";
		String query= "insert into unlock_otp (num,usr,otp) values (?,?,?)";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,username,password);
		PreparedStatement st=con.prepareStatement(query);
		st.setString(1, num);
		st.setInt(2,id);
		st.setString(3,otp);
		int no =st.executeUpdate();
		System.out.println(no);
		st.close();
		con.close();
	}
	
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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	{
	try
	{
		HttpSession session = request.getSession();
		if(session.getAttribute("uname")==null)
	    {
	    	response.sendRedirect("login.jsp");
	    }
		else
		{
			String mobno=request.getParameter("mobile_no");
			String uname=(String) session.getAttribute("uname");
			String pass=(String) session.getAttribute("pass");
			int eid = getid(uname,pass);
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
	        s.getOutputStream().write('l');
            s.getOutputStream().flush();
	        Scanner data = new Scanner (s.getInputStream());
	    	  if(data.nextLine().equals("ok"))
	        	{System.out.print("ok");
	        		}
            //data.close();
            System.out.println(mobno);
	        s.getOutputStream().write(mobno.getBytes());
            s.getOutputStream().flush();
            Thread.sleep(100);
            if(data.nextLine().equals("ok"))
        	{System.out.print("ok");
        		}
            data.close();
            Random rand=new Random();
            Integer ran_int=rand.nextInt(10000);
            String otp=ran_int.toString();
            s.getOutputStream().write(otp.getBytes());
            s.getOutputStream().flush();
            insert(eid,mobno,otp);
	        if(s.closePort())
	        {
	            System.out.println("Port is closed");
	        }
	        else
	        {
	            System.out.println("Port is not closed");
	        }
	        response.sendRedirect("main.jsp");
		}
	}
    catch(Exception ex)
	{
    	System.out.println(ex);
	}
}
}

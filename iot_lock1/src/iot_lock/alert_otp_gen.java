package iot_lock;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Random;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fazecast.jSerialComm.SerialPort;

@WebServlet("/alert_otp_gen")
public class alert_otp_gen extends HttpServlet {
	
	public static void insert(String num,String en,String otp) throws Exception
	{
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String username="root";
		String password="password";
		String query= "insert into alert_otp (num,en,otp) values (?,?,?)";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,username,password);
		PreparedStatement st=con.prepareStatement(query);
		st.setString(1, num);
		st.setString(2,en);
		st.setString(3,otp);
		int no =st.executeUpdate();
		System.out.println(no);
		st.close();
		con.close();
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
			String enable=request.getParameter("enable");
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
	        s.getOutputStream().write('o');
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
            insert(mobno,enable,otp);
	        if(s.closePort())
	        {
	            System.out.println("Port is closed");
	        }
	        else
	        {
	            System.out.println("Port is not closed");
	        }
	        response.sendRedirect("validatealert_otp.jsp");
		}
	}
    catch(Exception ex)
	{
    	System.out.println(ex);
	}
}
	}


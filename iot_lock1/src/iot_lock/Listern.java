package iot_lock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;

public class Listern {

	public static String getnumber() throws Exception
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
	
	public static void insert(int nid,String tim) throws Exception
	{
		String url="jdbc:mysql://127.0.0.1:3307/user_database";
		String username="root";
		String password="password";
		String query= "insert into access_info (id,atype,atime) values (?,?,?)";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,username,password);
		PreparedStatement st=con.prepareStatement(query);
		st.setInt(1, nid);
		st.setString(2, "Finger Print");
		st.setString(3,tim);
		int no =st.executeUpdate();
		System.out.println(no);
		st.close();
		con.close();
	}
	
	public static void main(String[] args) {
		try
		{
			SerialPort s= SerialPort.getCommPort("COM14");
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
	        int i=1,id;
	        String tim;
	        String num;
	        Scanner data = new Scanner (s.getInputStream());
	        while(i==1)
	        {
	        	id=Integer.parseInt(data.nextLine());
	        	System.out.println(id+"");
	        	SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
	            Date date =new Date();
	            tim=formatter.format(date);
	            insert(id,tim);
	            Thread.sleep(5000);
	            SerialPort s2= SerialPort.getCommPort("COM13");
		        s2.setComPortParameters(9600, 8, 1, 0);
		        s2.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING|SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
		        if (s2.openPort())
		        {
		            System.out.println("Port is opened");
		        }
		        else
		        {
		            System.out.println("Port in not opened");
		        }
		        Thread.sleep(3000);
		        s2.getOutputStream().write('s');
	            s2.getOutputStream().flush();
	            Scanner data2 = new Scanner (s2.getInputStream());
	            if(data2.nextLine().equals("ok"))
	        	{System.out.print("ok");	
	        	}
	            data2.close();
	            num= getnumber();
	            System.out.println(num);
		        s2.getOutputStream().write(num.getBytes());
	            s2.getOutputStream().flush();
	            if(s2.closePort())
		        {
		            System.out.println("Port is closed");
		        }
		        else
		        {
		            System.out.println("Port is not closed");
		        }
	        }
	        data.close();
	        if(s.closePort())
	        {
	            System.out.println("Port is closed");
	        }
	        else
	        {
	            System.out.println("Port is not closed");
	        }
		}
        catch(Exception ex)
		{
        	System.out.print(ex);
		}
	}

}

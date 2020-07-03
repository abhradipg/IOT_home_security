package iot_lock;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/History")
public class History extends HttpServlet {

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
            	String url="jdbc:mysql://127.0.0.1:3307/user_database";
        		String usernam="root";
        		String passwor="password";
        		String query= "select u.username as user,a.atype as method ,a.atime as time from access_info a,user_info u where a.id=u.id;";
        		Class.forName("com.mysql.cj.jdbc.Driver");
        		Connection con = DriverManager.getConnection(url,usernam,passwor);
        		Statement st=con.createStatement();
        		ResultSet rs=st.executeQuery(query);
        		response.setContentType("text");
        	    PrintWriter out = response.getWriter();
        	    out.println("<html>");
        	    out.println("<head>");
        	    out.println("<style>");
        	    out.println("input[type=\"button\"]{");
        	    out.println("background-color:white;");
        	    out.println("border-style:solid;");
        	    out.println("border-color:#fa6400;");
        	    out.println("border-radius:5px;");
        	    out.println("cursor:pointer;");
        	    out.println("padding-left:10px;");
        	    out.println("padding-right:10px;");
        	    out.println("padding-top:5px;");
        	    out.println("padding-bottom:5px;");
        	    out.println("font-size:1.5rem;");
        	    out.println("margin:5px;}");
        	    out.println("input[type=\"button\"]:hover{");
        	    out.println("background-color:#fa6400;");
        	    out.println("border-color:white;}");
        	    out.println("th{");
        	    out.println("background-color:#00ff66;");
        	    out.println("padding-top:10px;");
        	    out.println("padding-bottom:10px;");
        	    out.println("font-size:1.2rem;");
        	    out.println("text-align:center;}");
        	    out.println(".user{");
        	    out.println("width:30%;");
        	    out.println("border-right-style:solid;");
        	    out.println("border-right-width:1px;");
        	    out.println("border-right-color:#fa6400;}");
        	    out.println(".method{");
        	    out.println("width:18%;");
        	    out.println("border-left-style:solid;");
        	    out.println("border-left-width:1px;");
        	    out.println("border-left-color:#fa6400;");
        	    out.println("border-right-style:solid;");
        	    out.println("border-right-width:1px;");
        	    out.println("border-right-color:#fa6400;}");
        	    out.println(".time{");
        	    out.println("width:30%;");
        	    out.println("border-left-style:solid;");
        	    out.println("border-left-width:1px;");
        	    out.println("border-left-color:#fa6400;}");
        	    out.println("td{");
        	    out.println("text-align:center;");
        	    out.println("font-size:1.2rem;");
        	    out.println("padding-top:5px;");
        	    out.println("padding-bottom:5px;}");
        	    out.println("caption{");
        	    out.println("padding:10px;");
        	    out.println("font-size:2rem;");
        	    out.println("margin-bottom:10px;}");
        	    out.println("tr:nth-child(even) {background-color: #f2f2f2;}");
        	    out.println("table{padding-bottom:20px;margin:auto;}");
        	    out.println("</style>");
        	    out.println("</head>");
        	    out.println("<body>");
        	    out.println("<table cellspacing=\"0px\">");
        	    out.println("<caption>HISTORY</caption>");
        	    out.println("<tr>");
        	    out.println("<th class = \"user\">USER</th>");
        	    out.println("<th class = \"method\">ACCESS METHOD</th>");
        	    out.println("<th class = \"time\">TIME</th>");
        	    out.println("</tr>");
        		while(rs.next())
        		{
        			out.println("<tr>");
        			out.println("<td class = \"user\">"+rs.getString("user")+"</td>");
        			out.println("<td class = \"method\">"+rs.getString("method")+"</td>");
        			out.println("<td class = \"time\">"+rs.getString("time")+"</td>");
        			out.println("</tr>");
        		}
        		out.println("</table>");
        		out.println("<a href=\"main.jsp\"><input type=\"button\" value=\"Main Window\"></a>");
        		out.println("</body>");
        		out.println("</html>");
        		out.close();
        		rs.close();
        		st.close();
        	}
            catch(Exception ex)
            {
            	System.out.print(ex);
            }
		}
	}

}

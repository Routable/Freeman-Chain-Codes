import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Arrays;
import java.util.*;
import java.sql.*;
import java.text.NumberFormat;

Class Classifier {
	Connection con=null;
		Statement stmt=null;
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			con=DriverManager.getConnection(  
			"jdbc:oracle:thin:DM_USER13/password@10.1.144.85:1521:COSC436");  
			stmt=con.createStatement();
		}catch(Exception e){ System.out.println(e);}
        // get key coordinates
		ResultSet rs = null;
		ResultSet rs2 = null;
		try{
			String query = "SELECT * FROM DIGIT_TRAINING ";
			String query2 = "SELECT * FROM DIGIT_TEST";
			rs = stmt.executeQuery(query);
			//rs2 = stmt.executeQuery(query2);
			
			while(rs.next())
		      {
		        String fcc1 = rs.getString("SEC1_FCC_CODE");
		        int cls_lbl = rs.getInt("CLS_LBL");
		        // print the results
		        System.out.println("FCC: " + fcc1 + " Class label: " + cls_lbl);
		      }
		}catch(Exception e){ System.out.println(e);}
			/*while (rs2.next())
    {
      String fcc2 = rs2.getString("SEC1_FCC_CODE");
      int cls_lbl2 = rs2.getInt("CLS_LBL");
      // print the results
      System.out.println("FCC: " + fcc2 + " Class label: " + cls_lbl2);
    }
	rs.close();
    //stmt.close();*/
}
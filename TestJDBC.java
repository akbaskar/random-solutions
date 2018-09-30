import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
//import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.concurrent.TimeUnit.*;
public class TestJDBC {
	public boolean isDouble(Date oldDate, Date newDate){
		long difference = 0;
		difference = getDateDiff(oldDate, newDate);
		if (difference > 86400000)
			return false;
		else
			return true;
	}
	/**
	 * Get a diff between two dates
	 * @param date1 the oldest date
	 * @param date2 the newest date
	 */
	public static long getDateDiff(Date date1, Date date2) {
		long diffInMillies = date2.getTime() - date1.getTime();
		//return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
		return diffInMillies;
	}
 
	public static Time getCurrentJavaSqlTime(){
		Date date = new Date();
		return new Time(date.getTime());
	}
 
	public static void main(String[] args) throws Exception{ 
		String rfId = new String();
		int amountToBePaid = 0;
		rfId = "510003585AB4";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/visa","root","");
		PreparedStatement statement = con.prepareStatement("select time, vehicle_type, balance, flag from datas where rfid = ?");
		statement.setString(1, rfId);
		//Create a variable to execute query
		ResultSet result = statement.executeQuery();
		while(result.next()){
			//System.out.println(result.getString(1) + " " + result.getString(2) + " " + result.getString(3));
			//System.out.println(result.getString(1));
   
			//Vehicle_type
			String vType = result.getString(2);
   
			//Available Balance in the user's account
			int availableBalance = result.getInt(3);
			System.out.println("Initial available amount = " + availableBalance);
   
			//Flag used to check if the trip is single or double
			int flag = result.getInt(4);
   
			// oldDate is date in the Database
			String oldDateString = result.getString(1);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         
			//in Date format
			Date oldDate = formatter.parse(oldDateString);
			System.out.println("Old date in date format "+oldDate);
   
			//Updating a last seen time in database
			statement = con.prepareStatement("update datas set time = ? where rfid = ? ");
			Date newDate = new Date();
			Time time = getCurrentJavaSqlTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeString = df.format(time);
			System.out.println("New time (which being updated in DB)= " + timeString);
			statement.setString(1, timeString);
			statement.setString(2, rfId);
			statement.executeUpdate();
   
			TestJDBC obj = new TestJDBC();
			System.out.println("is double = ");
			System.out.println(obj.isDouble(oldDate, newDate));
   
			//Double trip amount details
			if(obj.isDouble(oldDate, newDate) && flag == 1){
				
				System.out.println("This is a Double Trip");
				flag = 0;
				statement = con.prepareStatement("update datas set flag = 0 where rfid = ? ");
				statement.setString(1, rfId);
				statement.executeUpdate();
				if (vType.equalsIgnoreCase("Car")){
					amountToBePaid = 20;
				}
				else if(vType.equalsIgnoreCase("BUS")){
					amountToBePaid = 40;
				}
				else if(vType.equalsIgnoreCase("TRUCK")){
					amountToBePaid = 120;
				}
				else{
					amountToBePaid = 150;
				} 
			}
			//Single trip amount details
			else{
				System.out.println("This is a single trip");
				flag = 1;
				statement = con.prepareStatement("update datas set flag = 1 where rfid = ? ");
				statement.setString(1, rfId);
				statement.executeUpdate();
    
				if (vType.equalsIgnoreCase("Car")){
					amountToBePaid = 40;
				}
				else if(vType.equalsIgnoreCase("BUS")){
					amountToBePaid = 70;
				}
				else if(vType.equalsIgnoreCase("TRUCK")){
					amountToBePaid = 150;
				}
				else{
					amountToBePaid = 200;
				}
			}
			if (availableBalance < amountToBePaid){
				System.out.println("Warning Msg: Recharge your account");
			}
			else{
				System.out.println("amount to be paid= "+amountToBePaid);
				availableBalance = availableBalance - amountToBePaid;
    
				//updating the Balance field in the database
				statement = con.prepareStatement("update datas set balance = ? where rfid = ? ");
				statement.setInt(1, availableBalance);
				statement.setString(2, rfId);
				statement.executeUpdate();
				System.out.println("Amount debted from the user. Available Balance= "+availableBalance);
			}
		}
		//if (!result.next()){
		// System.out.println("Invalid RFId");
		//}
  
  
	}
}


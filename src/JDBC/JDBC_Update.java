package JDBC;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

public class JDBC_Update {
	public static void main(String[] args) throws Exception{
		//Connect to DB
		Connection con = DBCConn.getConn();
		Statement stmt = con.createStatement();
		Scanner sc = new Scanner(System.in);
		// Get emp_id and new salary from user
		System.out.println("Enter empid to update:");
		String emp_id = sc.nextLine();
		System.out.println("Enter new salary:");
		double newSalary = sc.nextDouble();
		// Build nd execute update query
		int rowsAffected = stmt.executeUpdate("UPDATE emp SET emp_salary = " + newSalary + "WHERE emp_id = '" + emp_id + "'");
		//show result 
		if (rowsAffected > 0)
			System.out.println("Recored update sucessfully. ");
		else
			System.out.println("Employee ID not found. Update failed.");
		
		//Close connections
		stmt.close();
		con.close();
		sc.close();
	}

}
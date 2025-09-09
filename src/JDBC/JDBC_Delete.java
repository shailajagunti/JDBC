package JDBC;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

public class JDBC_Delete {
    public static void main(String[] args) throws Exception {
        // Connect to DB
        Connection con = DBCConn.getConn();
        Statement stmt = con.createStatement();
        Scanner sc = new Scanner(System.in);

        // Get emp_id from user
        System.out.println("Enter empid to delete:");
        String emp_id = sc.nextLine();

        // Build and execute delete query
        int rowsAffected = stmt.executeUpdate("DELETE FROM emp WHERE emp_id = '" + emp_id + "'");

        // Show result
        if (rowsAffected > 0)
            System.out.println("Record deleted successfully.");
        else
            System.out.println("Employee ID not found. Deletion failed.");

        // Close connections
        stmt.close();
        con.close();
        sc.close();
    }
}
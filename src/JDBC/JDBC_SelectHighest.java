package JDBC;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBC_SelectHighest {
    public static void main(String[] args) throws Exception {
        // Connect to DB
        Connection con = DBCConn.getConn();
        Statement stmt = con.createStatement();

        // Query to get highest salary employee
        String sql = "SELECT emp_id, emp_name, emp_salary " +
                     "FROM emp " +
                     "WHERE emp_salary = (SELECT MAX(emp_salary) FROM emp)";

        ResultSet rs = stmt.executeQuery(sql);

        // Display result
        if (rs.next()) {
            System.out.println("Employee with Highest Salary:");
            System.out.println("ID   : " + rs.getString("emp_id"));
            System.out.println("Name : " + rs.getString("emp_name"));
            System.out.println("Salary: " + rs.getDouble("emp_salary"));
        } else {
            System.out.println("No records found.");
        }

        // Close resources
        rs.close();
        stmt.close();
        con.close();
    }
}

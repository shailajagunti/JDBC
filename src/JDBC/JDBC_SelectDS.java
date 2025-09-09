package JDBC;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBC_SelectDS{
    public static void main(String[] args) throws Exception {
        // Connect to DB
        Connection con = DBCConn.getConn();
        Statement stmt = con.createStatement();

        // Query employees of DS department
        String sql = "SELECT emp_id, emp_name, emp_salary, department FROM emp WHERE department = 'DS'";

        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("Employees in DS Department:");
        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.println("ID: " + rs.getInt("emp_id") +
                               ", Name: " + rs.getString("emp_name") +
                               ", Salary: " + rs.getDouble("emp_salary") +
                               ", Department: " + rs.getString("department"));
        }

        if (!found) {
            System.out.println("No employees found in DS department.");
        }

        // Close resources
        rs.close();
        stmt.close();
        con.close();
    }
}

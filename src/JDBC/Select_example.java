package JDBC;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Select_example {
    public static void main(String[] args) throws Exception {
        Connection con = DBCConn.getConn();
        Statement stmt = con.createStatement();

        // Select all employees
        String sql = "SELECT emp_id, emp_name, department, emp_salary FROM emp";
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("ID\tName\t\tDepartment\tSalary");
        System.out.println("--------------------------------------------------");

        while (rs.next()) {
        	 System.out.println(rs.getInt("emp_id") + "\t" + rs.getString("emp_name") + "\t\t" + rs.getString("department") + "\t\t" + rs.getDouble("emp_salary"));
        }

        rs.close();
        stmt.close();
        con.close();
    }
}

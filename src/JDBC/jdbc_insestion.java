package JDBC;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

public class jdbc_insestion{
    public static void main(String[] args) throws Exception {
      Connection con = DBCConn.getConn();
        Statement stmt = con.createStatement();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter empid: ");
        String empid = sc.nextLine();

        System.out.print("Enter empname: ");
        String empname = sc.nextLine();
        System.out.print("Enter department: ");
        String dept = sc.nextLine();

        System.out.print("Enter salary: ");
        double salary = Double.parseDouble(sc.nextLine());
        String query = "INSERT INTO emp(emp_id, emp_name, department, emp_salary) VALUES ('" + empid + "', '" + empname + "', '" + dept + "', " + salary + ")";
        int i = stmt.executeUpdate(query);

        if (i == 1){
            System.out.println("Record inserted successfully!");
        } else {
            System.out.println("Failed to insert record.");
        }
        stmt.close();
        con.close();
        sc.close();
    }
}

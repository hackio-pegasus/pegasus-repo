import java.sql.*;

public class EmployeeSalesData {
    public static void main(String[] args) {
        // Connection details for ORADBLN2 database (Employees)
        String oradbln2Host = "ORADBLN2_HOST";
        String oradbln2Port = "ORADBLN2_PORT";
        String oradbln2Database = "ORADBLN2_DATABASE";
        String oradbln2User = "ORADBLN2_USER";
        String oradbln2Password = "ORADBLN2_PASSWORD";

        // Connection details for SQLSRVNY1 database (Sales)
        String sqlsrvny1Host = "SQLSRVNY1_HOST";
        String sqlsrvny1Port = "SQLSRVNY1_PORT";
        String sqlsrvny1Database = "SQLSRVNY1_DATABASE";
        String sqlsrvny1User = "SQLSRVNY1_USER";
        String sqlsrvny1Password = "SQLSRVNY1_PASSWORD";

        Connection connection1 = null;
        Connection connection2 = null;
        Statement statement1 = null;
        Statement statement2 = null;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;

        try {
            // Register the Oracle and SQL Server JDBC drivers
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Construct the connection URLs for ORADBLN2 and SQLSRVNY1 databases
            String url1 = "jdbc:oracle:thin:@" + oradbln2Host + ":" + oradbln2Port + ":" + oradbln2Database;
            String url2 = "jdbc:sqlserver://" + sqlsrvny1Host + ":" + sqlsrvny1Port + ";databaseName=" + sqlsrvny1Database;

            // Establish the connections to ORADBLN2 and SQLSRVNY1 databases
            connection1 = DriverManager.getConnection(url1, oradbln2User, oradbln2Password);
            connection2 = DriverManager.getConnection(url2, sqlsrvny1User, sqlsrvny1Password);

            // Create statement objects for each connection
            statement1 = connection1.createStatement();
            statement2 = connection2.createStatement();

            // Execute the query to select employees from ORADBLN2
            String query1 = "SELECT * FROM employees";
            resultSet1 = statement1.executeQuery(query1);

            // Iterate over employees and retrieve their sales data from SQLSRVNY1
            while (resultSet1.next()) {
                int employeeId = resultSet1.getInt("employee_id");
                String employeeName = resultSet1.getString("employee_name");
                // ... Add more column retrievals for employees table

                // Execute the query to select sales data for the current employee from SQLSRVNY1
                String query2 = "SELECT * FROM sales WHERE employee_id = " + employeeId;
                resultSet2 = statement2.executeQuery(query2);

                // Process the sales data for the current employee
                while (resultSet2.next()) {
                    int salesId = resultSet2.getInt("sales_id");
                    String productName = resultSet2.getString("product_name");
                    double amount = resultSet2.getDouble("amount");
                    // ... Add more column retrievals for sales table

                    // Display the employee and sales data
                    System.out.println("Employee ID: " + employeeId);
                    System.out.println("Employee Name: " + employeeName);
                    System.out.println("Sales ID: " + salesId);
                    System.out.println("Product Name: " + productName);
                    System.out.println("Amount: " + amount);
                    System.out.println();  // Print a newline
                }

                // Close the result set for sales data
                resultSet2.close();
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the JDBC driver. Please make sure the Oracle and SQL Server drivers are in the classpath.");
        } catch (SQLException e) {
            System.err.println("An error occurred while connecting to the database: " + e.getMessage());
        } finally {
            // Close the result sets, statements, and connections
            if (resultSet1 != null) {
                try {
                    resultSet1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (resultSet2 != null) {
                try {
                    resultSet2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement1 != null) {
                try {
                    statement1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement2 != null) {
                try {
                    statement2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection1 != null) {
                try {
                    connection1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection2 != null) {
                try {
                    connection2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

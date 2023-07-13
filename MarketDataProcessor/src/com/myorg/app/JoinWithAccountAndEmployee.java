import java.sql.*;

public class ReadJoinData {
    public static void main(String[] args) {
        // Connection details for ORADBLN2 database (dim_account table)
        String oradbln2Host = "ORADBLN2_HOST";
        String oradbln2Port = "ORADBLN2_PORT";
        String oradbln2Database = "ORADBLN2_DATABASE";
        String oradbln2User = "ORADBLN2_USER";
        String oradbln2Password = "ORADBLN2_PASSWORD";

        // Connection details for ORADBLN3 database (dim_employee table)
        String oradbln3Host = "ORADBLN3_HOST";
        String oradbln3Port = "ORADBLN3_PORT";
        String oradbln3Database = "ORADBLN3_DATABASE";
        String oradbln3User = "ORADBLN3_USER";
        String oradbln3Password = "ORADBLN3_PASSWORD";

        // Connection details for ORADBLN4 database (dim_product table)
        String oradbln4Host = "ORADBLN4_HOST";
        String oradbln4Port = "ORADBLN4_PORT";
        String oradbln4Database = "ORADBLN4_DATABASE";
        String oradbln4User = "ORADBLN4_USER";
        String oradbln4Password = "ORADBLN4_PASSWORD";

        Connection connection1 = null;
        Connection connection2 = null;
        Connection connection3 = null;
        Statement statement1 = null;
        Statement statement2 = null;
        Statement statement3 = null;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;
        ResultSet resultSet3 = null;

        try {
            // Register the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Construct the connection URLs for ORADBLN2, ORADBLN3, and ORADBLN4 databases
            String url1 = "jdbc:oracle:thin:@" + oradbln2Host + ":" + oradbln2Port + ":" + oradbln2Database;
            String url2 = "jdbc:oracle:thin:@" + oradbln3Host + ":" + oradbln3Port + ":" + oradbln3Database;
            String url3 = "jdbc:oracle:thin:@" + oradbln4Host + ":" + oradbln4Port + ":" + oradbln4Database;

            // Establish the connections to ORADBLN2, ORADBLN3, and ORADBLN4 databases
            connection1 = DriverManager.getConnection(url1, oradbln2User, oradbln2Password);
            connection2 = DriverManager.getConnection(url2, oradbln3User, oradbln3Password);
            connection3 = DriverManager.getConnection(url3, oradbln4User, oradbln4Password);

            // Create statement objects for each connection
            statement1 = connection1.createStatement();
            statement2 = connection2.createStatement();
            statement3 = connection3.createStatement();

            // Execute the query to select data from dim_account table in ORADBLN2
            String query1 = "SELECT * FROM dim_account";
            resultSet1 = statement1.executeQuery(query1);

            // Execute the query to select data from dim_employee table in ORADBLN3
            String query2 = "SELECT * FROM dim_employee";
            resultSet2 = statement2.executeQuery(query2);

            // Execute the query to select data from dim_product table in ORADBLN4
            String query3 = "SELECT * FROM dim_product";
            resultSet3 = statement3.executeQuery(query3);

            // Perform the join operation and display the result
            while (resultSet1.next() && resultSet2.next() && resultSet3.next()) {
                // Extract the values from the current rows
                int accountId = resultSet1.getInt("account_id");
                String accountName = resultSet1.getString("account_name");
                // ... Add more column retrievals for dim_account table

                int employeeId = resultSet2.getInt("employee_id");
                String employeeName = resultSet2.getString("employee_name");
                // ... Add more column retrievals for dim_employee table

                int productId = resultSet3.getInt("product_id");
                String productName = resultSet3.getString("product_name");
                // ... Add more column retrievals for dim_product table

                // Display the joined data
                System.out.println("Account ID: " + accountId);
                System.out.println("Account Name: " + accountName);
                System.out.println("Employee ID: " + employeeId);
                System.out.println("Employee Name: " + employeeName);
                System.out.println("Product ID: " + productId);
                System.out.println("Product Name: " + productName);
                System.out.println();  // Print a newline
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the Oracle JDBC driver. Please make sure it's in the classpath.");
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
            if (resultSet3 != null) {
                try {
                    resultSet3.close();
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
            if (statement3 != null) {
                try {
                    statement3.close();
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
            if (connection3 != null) {
                try {
                    connection3.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

import java.sql.*;

public class ReadSalesData {
    public static void main(String[] args) {
        // Connection details for SQLSRVNY1 database
        String sqlsrvny1Host = "SQLSRVNY1";
        String sqlsrvny1Port = "SQLSRVNY1_PORT";
        String sqlsrvny1Database = "SQLSRVNY1_DATABASE";
        String sqlsrvny1User = "SQLSRVNY1_USER";
        String sqlsrvny1Password = "SQLSRVNY1_PASSWORD";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Register the SQL Server driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Construct the connection URL
            String url = "jdbc:sqlserver://" + sqlsrvny1Host + ":" + sqlsrvny1Port + ";databaseName=" + sqlsrvny1Database;

            // Establish the connection to SQLSRVNY1 database
            connection = DriverManager.getConnection(url, sqlsrvny1User, sqlsrvny1Password);

            // Create a statement object
            statement = connection.createStatement();

            // Execute the query to select data from sales table
            String query = "SELECT * FROM sales";
            resultSet = statement.executeQuery(query);

            // Process the result set
            while (resultSet.next()) {
                // Extract the values from the current row
                int salesId = resultSet.getInt("sales_id");
                String productName = resultSet.getString("product_name");
                double amount = resultSet.getDouble("amount");
                // ... Add more column retrievals as needed

                // Display the retrieved data
                System.out.println("Sales ID: " + salesId);
                System.out.println("Product Name: " + productName);
                System.out.println("Amount: " + amount);
                System.out.println();  // Print a newline
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the SQL Server driver. Please make sure it's in the classpath.");
        } catch (SQLException e) {
            System.err.println("An error occurred while connecting to the database: " + e.getMessage());
        } finally {
            // Close the result set, statement, and connection
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

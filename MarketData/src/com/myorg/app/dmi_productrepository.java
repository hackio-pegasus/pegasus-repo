import java.sql.*;

public class ReadDimProduct {
    public static void main(String[] args) {
        // Connection details for ORADBLN4 database
        String oradbln4Host = "ORADBLN4";
        String oradbln4Port = "ORADBLN4_PORT";
        String oradbln4Database = "ORADBLN4_DATABASE";
        String oradbln4User = "ORADBLN4_USER";
        String oradbln4Password = "ORADBLN4_PASSWORD";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Register the PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Construct the connection URL
            String url = "jdbc:postgresql://" + oradbln4Host + ":" + oradbln4Port + "/" + oradbln4Database;

            // Establish the connection to ORADBLN4 database
            connection = DriverManager.getConnection(url, oradbln4User, oradbln4Password);

            // Create a statement object
            statement = connection.createStatement();

            // Execute the query to select data from dim_product table
            String query = "SELECT * FROM dim_product";
            resultSet = statement.executeQuery(query);

            // Process the result set
            while (resultSet.next()) {
                // Extract the values from the current row
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("product_name");
                String category = resultSet.getString("category");
                // ... Add more column retrievals as needed

                // Display the retrieved data
                System.out.println("Product ID: " + productId);
                System.out.println("Product Name: " + productName);
                System.out.println("Category: " + category);
                System.out.println();  // Print a newline
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the PostgreSQL driver. Please make sure it's in the classpath.");
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

import java.sql.*;

public class PersistDataBetweenDatabases {
    public static void main(String[] args) {
        // Connection details for ORADBLN1 database
        String oradbln1Host = "ORADBLN1_HOST";
        String oradbln1Port = "ORADBLN1_PORT";
        String oradbln1Database = "ORADBLN1_DATABASE";
        String oradbln1User = "ORADBLN1_USER";
        String oradbln1Password = "ORADBLN1_PASSWORD";

        // Connection details for ORADBLN2 database
        String oradbln2Host = "ORADBLN2_HOST";
        String oradbln2Port = "ORADBLN2_PORT";
        String oradbln2Database = "ORADBLN2_DATABASE";
        String oradbln2User = "ORADBLN2_USER";
        String oradbln2Password = "ORADBLN2_PASSWORD";

        Connection oradbln1Connection = null;
        Connection oradbln2Connection = null;
        Statement oradbln1Statement = null;
        Statement oradbln2Statement = null;

        try {
            // Register the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            // Construct the connection URL for ORADBLN1 and ORADBLN2 databases
            String oradbln1Url = "jdbc:postgresql://" + oradbln1Host + ":" + oradbln1Port + "/" + oradbln1Database;
            String oradbln2Url = "jdbc:postgresql://" + oradbln2Host + ":" + oradbln2Port + "/" + oradbln2Database;

            // Establish the connections to ORADBLN1 and ORADBLN2 databases
            oradbln1Connection = DriverManager.getConnection(oradbln1Url, oradbln1User, oradbln1Password);
            oradbln2Connection = DriverManager.getConnection(oradbln2Url, oradbln2User, oradbln2Password);

            // Create statement objects for each connection
            oradbln1Statement = oradbln1Connection.createStatement();
            oradbln2Statement = oradbln2Connection.createStatement();

            // Select data from dim_account table in ORADBLN1
            ResultSet accounts = oradbln1Statement.executeQuery("SELECT * FROM dim_account");

            // Insert data into dim_account table in ORADBLN2
            while (accounts.next()) {
                int accountId = accounts.getInt("account_id");
                String accountName = accounts.getString("account_name");
                String accountType = accounts.getString("account_type");
                // ... Add more column retrievals as needed

                String insertQuery = "INSERT INTO dim_account VALUES (" + accountId + ", '" + accountName + "', '" + accountType + "')";
                oradbln2Statement.executeUpdate(insertQuery);
            }

            // Commit the changes to ORADBLN2 database
            oradbln2Connection.commit();

            // Select data from dim_employee table in ORADBLN2
            ResultSet employees = oradbln2Statement.executeQuery("SELECT * FROM dim_employee");

            // Insert data into dim_employee table in ORADBLN1
            while (employees.next()) {
                int employeeId = employees.getInt("employee_id");
                String employeeName = employees.getString("employee_name");
                // ... Add more column retrievals as needed

                String insertQuery = "INSERT INTO dim_employee VALUES (" + employeeId + ", '" + employeeName + "')";
                oradbln1Statement.executeUpdate(insertQuery);
            }

            // Commit the changes to ORADBLN1 database
            oradbln1Connection.commit();

            System.out.println("Data persisted successfully!");

        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the PostgreSQL JDBC driver. Please make sure it's in the classpath.");
        } catch (SQLException e) {
            System.err.println("An error occurred while connecting to the database: " + e.getMessage());
        } finally {
            // Close the statements and connections
            if (oradbln1Statement != null) {
                try {
                    oradbln1Statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (oradbln2Statement != null) {
                try {
                    oradbln2Statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (oradbln1Connection != null) {
                try {
                    oradbln1Connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (oradbln2Connection != null) {
                try {
                    oradbln2Connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

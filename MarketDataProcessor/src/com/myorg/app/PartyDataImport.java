package com.myorg.app;
import java.sql.*;
public class PartyDataImport {

    public static void main(String[] args) {
        String oradbln2Host = "ORADBLN4";
        String oradbln2Port = "ORADBLN4_PORT";
        String oradbln2Database = "ORADBLN4_DATABASE";
        String oradbln2User = "ORADBLN4_USER";
        String oradbln2Password = "ORADBLN4_PASSWORD";

        try {
            // Step 1: Establish the database connection
            Connection connection = DriverManager.getConnection(oradbln2Host, username, password);


            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Construct the connection URLs for ORADBLN2 and SQLSRVNY1 databases
            String url1 = "jdbc:oracle:thin:@" + oradbln2Host + ":" + oradbln2Port + ":" + oradbln2Database;
            // Step 2: Query the dim_party table to retrieve party entity data
            connection1 = DriverManager.getConnection(url1, oradbln2User, oradbln2Password);
			statement1 = connection1.createStatement();
            ResultSet resultSet = statement1.executeQuery("SELECT * FROM dim_party");

            // Step 3: Iterate over the retrieved data
            while (resultSet.next()) {
                // Retrieve party entity data from the result set
                int partyId = resultSet.getInt("party_id");
                String partyName = resultSet.getString("party_name");
                // Retrieve other relevant columns as needed

                // Step 4: Prepare an SQL statement to insert the party entity data into the Party table
                String insertSql = "INSERT INTO Party (party_id, party_name) VALUES (?, ?)";
                PreparedStatement insertStatement = connection1.prepareStatement(insertSql);
                insertStatement.setInt(1, partyId);
                insertStatement.setString(2, partyName);
                // Set other relevant columns for insertion

                // Step 5: Execute the SQL statement to persist the data
                insertStatement.executeUpdate();

                // Close the insert statement
                insertStatement.close();
            }

            // Step 6: Close the resources
            resultSet.close();
            statement.close();
            connection.close();
            System.out.println("Data imported successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
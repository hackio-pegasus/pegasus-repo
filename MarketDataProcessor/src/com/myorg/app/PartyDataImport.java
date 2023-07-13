package com.myorg.app;
import java.sql.*;
public class PartyDataImport {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/ORADBLN4";
        String username = "your_username";
        String password = "your_password";

        try {
            // Step 1: Establish the database connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Step 2: Query the dim_party table to retrieve party entity data
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM dim_party");

            // Step 3: Iterate over the retrieved data
            while (resultSet.next()) {
                // Retrieve party entity data from the result set
                int partyId = resultSet.getInt("party_id");
                String partyName = resultSet.getString("party_name");
                // Retrieve other relevant columns as needed

                // Step 4: Prepare an SQL statement to insert the party entity data into the Party table
                String insertSql = "INSERT INTO Party (party_id, party_name) VALUES (?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertSql);
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
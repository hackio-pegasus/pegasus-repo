package com.myorg.app;

import java.sql.*;

public class PartyDataLoad {

    public static void main(String[] args) {
        String sourcejdbcUrl = "jdbc:mysql://localhost:3306/ORADBLN4";
        String targetjdbcUrl = "jdbc:mysql://localhost:3306/SQLSRVNY1";
        String username = "your_username";
        String password = "your_password";

        try {
            // Step 1: Establish the database connection
            Connection connection = DriverManager.getConnection(sourcejdbcUrl, username, password);
            Connection targetConnection = DriverManager.getConnection(targetjdbcUrl, username, password);

            // Step 2: Query the dim_party table to retrieve party data
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM dim_party");

            // Step 3: Iterate over the retrieved data
            while (resultSet.next()) {
                // Retrieve party data from the result set
                int partyId = resultSet.getInt("party_id");
                String partyName = resultSet.getString("party_name");
                // Retrieve other relevant columns as needed

                // Step 4: Prepare an SQL statement to insert the party data into the Party table
                String insertSql = "INSERT INTO Party (party_id, party_name) VALUES (?, ?)";
                PreparedStatement insertStatement = targetConnection.prepareStatement(insertSql);
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
            System.out.println("Party data loaded successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

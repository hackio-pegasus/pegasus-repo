package com.myorg.app;

import java.sql.*;

public class BusinessRelationDataImport {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/ORADBLN3";
		String jdbcUrlTarget = "jdbc:mysql://localhost:3306/SQLSRVNY2";
        String username = "your_username";
        String password = "your_password";

        try {
            // Step 1: Establish the database connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
	        Connection connection1 = DriverManager.getConnection(jdbcUrlTarget, username, password);

            // Step 2: Query the dim_businessrelation table to retrieve party entity data
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM dim_businessrelation");

            // Step 3: Iterate over the retrieved data
            while (resultSet.next()) {
                // Retrieve party entity data from the result set
                int businessRelationId = resultSet.getInt("business_relation_id");
                int partyId = resultSet.getInt("party_id");
                // Retrieve other relevant columns as needed

                // Step 4: Prepare an SQL statement to insert the party entity data into the BusinessRelation table
                String insertSql = "INSERT INTO BusinessRelation (business_relation_id, party_id) VALUES (?, ?)";
                PreparedStatement insertStatement = connection1.prepareStatement(insertSql);
                insertStatement.setInt(1, businessRelationId);
                insertStatement.setInt(2, partyId);
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

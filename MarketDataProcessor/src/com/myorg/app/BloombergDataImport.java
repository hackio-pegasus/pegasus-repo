package com.myorg.app.bbg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BloombergDataImport {

    public static void main(String[] args) {
        String bloombergApiUrl = "https://api.bloomberg.com/api/instrumentdata/mkp?cusip=YOUR_CUSIP";
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:ORADBLN2";
        String username = "your_username";
        String password = "your_password";

        try {
            // Step 1: Make the HTTP call to Bloomberg API
            URL url = new URL(bloombergApiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Step 2: Parse the received data
            // Implement your parsing logic here to extract the relevant data from the response string

            // Step 3: Establish the database connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Step 4: Prepare an SQL statement to insert the data into the MK_PRICE table
            String sql = "INSERT INTO dim_price (column1, column2, column3) VALUES (?, ?, ?)"; // Adjust the column names and table name
            PreparedStatement statement = connection.prepareStatement(sql);

            // Step 5: Execute the SQL statement to save the data into the table
            // Set values for the prepared statement based on the parsed data
            statement.setString(1, "value1"); // Adjust the column index and value
            statement.setString(2, "value2");
            statement.setString(3, "value3");

            statement.executeUpdate();

            // Step 6: Close the database connection
            statement.close();
            connection.close();
            System.out.println("Data imported successfully!");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
package com.myorg.app;

public class WriteAccountsToDb {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/ORADBLN1";
        String username = "your_username";
        String password = "your_password";
        String filePath = "path/to/account/file.txt";

        try {
            // Step 1: Load the JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Step 2: Establish the database connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Step 3: Read data from the account file and save to the account table
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Split the line into individual data fields
                    String[] data = line.split(",");

                    // Prepare the SQL statement
                    String sql = "INSERT INTO dim_account (account_number, account_name, balance) VALUES (?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    // Set values for the prepared statement
                    statement.setString(1, data[0].trim());  // Account Number
                    statement.setString(2, data[1].trim());  // Account Name
                    statement.setDouble(3, Double.parseDouble(data[2].trim()));  // Balance

                    // Execute the statement
                    statement.executeUpdate();

                    // Close the statement
                    statement.close();
                }
                System.out.println("Data imported successfully!");
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }

            // Step 4: Close the database connection
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
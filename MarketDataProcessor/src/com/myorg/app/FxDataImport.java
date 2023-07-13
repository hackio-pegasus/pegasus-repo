package com.myorg.app;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FxDataImport {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/ORADBLN3";
        String username = "your_username";
        String password = "your_password";

        try {
            // Step 1: Establish the database connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Step 2: Fetch FX data from the market using a REST call
            String fxData = fetchFxDataFromMarket();

            // Step 3: Parse the fetched FX data
            Map<String, Double> fxMap = parseFxData(fxData);

            // Step 4: Prepare an SQL statement to insert the FX data into the dm_fx table
            String insertSql = "INSERT INTO dm_fx (currency_pair, exchange_rate) VALUES (?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);

            // Step 5: Iterate over the parsed FX data and insert it into the dm_fx table
            for (Map.Entry<String, Double> entry : fxMap.entrySet()) {
                String currencyPair = entry.getKey();
                Double exchangeRate = entry.getValue();

                insertStatement.setString(1, currencyPair);
                insertStatement.setDouble(2, exchangeRate);
                insertStatement.executeUpdate();
            }

            // Step 6: Close the resources
            insertStatement.close();
            connection.close();
            System.out.println("FX data imported successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String fetchFxDataFromMarket() throws IOException {
        // Make a REST call to fetch FX data from the market using the appropriate endpoint
        // You can use HttpURLConnection or an HTTP client library like Apache HttpClient
        URL url = new URL("https://api.market.com/fxdata");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }

    private static Map<String, Double> parseFxData(String fxData) {
        // Implement your logic here to parse the fetched FX data
        // Extract the currency pair and exchange rate from the response
        // Return the parsed FX data as a Map with currency pair as the key and exchange rate as the value
        Map<String, Double> fxMap = new HashMap<>();
        // Parse the response and populate the fxMap accordingly
        return fxMap;
    }
}

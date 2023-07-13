import org.apache.commons.vfs2.*;

import java.sql.*;
import java.io.*;

public class PersistCurrencyDataToSFTP {
    public static void main(String[] args) {
        // Connection details for SQLSRVNY2 database
        String sqlsrvny2Host = "SQLSRVNY2_HOST";
        String sqlsrvny2Port = "SQLSRVNY2_PORT";
        String sqlsrvny2Database = "SQLSRVNY2_DATABASE";
        String sqlsrvny2User = "SQLSRVNY2_USER";
        String sqlsrvny2Password = "SQLSRVNY2_PASSWORD";

        // SFTP server details
        String sftpHost = "SFTP_HOST";
        String sftpPort = "SFTP_PORT";
        String sftpUser = "SFTP_USER";
        String sftpPassword = "SFTP_PASSWORD";
        String sftpDirectory = "SFTP_DIRECTORY";
        String sftpFileName = "currency_data.csv";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Register the SQL Server driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Construct the connection URL
            String url = "jdbc:sqlserver://" + sqlsrvny2Host + ":" + sqlsrvny2Port + ";databaseName=" + sqlsrvny2Database;

            // Establish the connection to SQLSRVNY2 database
            connection = DriverManager.getConnection(url, sqlsrvny2User, sqlsrvny2Password);

            // Create a statement object
            statement = connection.createStatement();

            // Execute the query to select currency data from SQLSRVNY2
            String query = "SELECT * FROM currency";
            resultSet = statement.executeQuery(query);

            // Create a temporary CSV file to store the data
            File tempFile = File.createTempFile("currency_data", ".csv");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            // Write the data to the CSV file
            while (resultSet.next()) {
                String currencyCode = resultSet.getString("currency_code");
                double exchangeRate = resultSet.getDouble("exchange_rate");
                // ... Add more column retrievals as needed

                // Write a line to the CSV file
                String line = currencyCode + "," + exchangeRate + "\n";
                writer.write(line);
            }

            writer.close();

            // Set up the SFTP connection
            FileSystemOptions opts = new FileSystemOptions();
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");

            String sftpUrl = "sftp://" + sftpUser + ":" + sftpPassword + "@" + sftpHost + ":" + sftpPort + "/" + sftpDirectory;
            FileObject sftpFileObject = VFS.getManager().resolveFile(sftpUrl, opts);

            // Upload the CSV file to the SFTP server
            FileObject fileToUpload = VFS.getManager().resolveFile(tempFile.getAbsolutePath());
            sftpFileObject.copyFrom(fileToUpload, Selectors.SELECT_SELF);

            // Cleanup
            fileToUpload.delete();
            sftpFileObject.close();

            System.out.println("Data persisted successfully to SFTP server!");

        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the SQL Server driver. Please make sure it's in the classpath.");
        } catch (SQLException e) {
            System.err.println("An error occurred while connecting to the database: " + e.getMessage());
        } catch (IOException | FileSystemException e) {
            System.err.println("An error occurred while transferring the file to the SFTP server: " + e.getMessage());
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

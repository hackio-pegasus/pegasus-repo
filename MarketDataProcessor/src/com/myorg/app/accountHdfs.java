import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.RemoteIterator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PersistAccountData {
    public static void main(String[] args) {
        // HDFS server details
        String hdfsHost = "HDFS_HOST";
        String hdfsPort = "HDFS_PORT";
        String hdfsPath = "HDFS_PATH";

        // ORADBLN2 database details
        String oradbln2Host = "ORADBLN2_HOST";
        String oradbln2Port = "ORADBLN2_PORT";
        String oradbln2Database = "ORADBLN2_DATABASE";
        String oradbln2User = "ORADBLN2_USER";
        String oradbln2Password = "ORADBLN2_PASSWORD";

        Connection connection = null;
        PreparedStatement statement = null;
        FileSystem hdfs = null;

        try {
            // Establish connection to ORADBLN2 database
            String url = "jdbc:oracle:thin:@" + oradbln2Host + ":" + oradbln2Port + ":" + oradbln2Database;
            connection = DriverManager.getConnection(url, oradbln2User, oradbln2Password);

            // Configure HDFS connection
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", "hdfs://" + hdfsHost + ":" + hdfsPort);
            hdfs = FileSystem.get(conf);

            // Specify the HDFS file path for account
            Path hdfsFilePath = new Path(hdfsPath + "/account");

            // Read data from HDFS file
            RemoteIterator<LocatedFileStatus> iterator = hdfs.listFiles(hdfsFilePath, false);
            while (iterator.hasNext()) {
                LocatedFileStatus fileStatus = iterator.next();
                Path file = fileStatus.getPath();

                BufferedReader br = new BufferedReader(new InputStreamReader(hdfs.open(file)));
                String line;
                while ((line = br.readLine()) != null) {
                    // Split the line into accountId, accountName, and other attributes based on your file format
                    String[] data = line.split(",");

                    // Prepare the SQL statement to insert the data into ORADBLN2
                    String insertQuery = "INSERT INTO account (account_id, account_name, ...) VALUES (?, ?, ...)";
                    statement = connection.prepareStatement(insertQuery);
                    statement.setString(1, data[0]);  // Set accountId
                    statement.setString(2, data[1]);  // Set accountName
                    // ... Set other attributes

                    // Execute the insert statement
                    statement.executeUpdate();
                }
                br.close();
            }

            System.out.println("Data persisted successfully!");

        } catch (SQLException e) {
            System.err.println("An error occurred while connecting to the database: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred while reading from HDFS: " + e.getMessage());
        } finally {
            // Close the statement and connection
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

            // Close the HDFS connection
            if (hdfs != null) {
                try {
                    hdfs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

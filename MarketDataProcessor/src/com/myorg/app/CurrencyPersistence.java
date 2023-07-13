package com.myorg.app;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class CurrencyPersistence {
    public static void main(String[] args) {
        String jmsConnectionFactoryName = "jms/ConnectionFactory";
        String jmsQueueName = "jms/MyQueue";
        String jdbcUrl = "jdbc:mysql://localhost:3306/SQLSRVNY2";
        String username = "your_username";
        String password = "your_password";

        try {
            // Step 1: Initialize JMS
            Context jndiContext = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup(jmsConnectionFactoryName);
            Queue queue = (Queue) jndiContext.lookup(jmsQueueName);

            // Step 2: Establish the JMS connection and session
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Step 3: Create a JMS consumer for reading messages
            MessageConsumer consumer = session.createConsumer(queue);

            // Step 4: Start the JMS connection
            connection.start();

            // Step 5: Receive and process JMS messages
            Message message = consumer.receive();
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String messageText = textMessage.getText();

                // Step 6: Establish the database connection
                Connection dbConnection = DriverManager.getConnection(jdbcUrl, username, password);

                // Step 7: Prepare an SQL statement to insert the message into the database
                String sql = "INSERT INTO dim_currency (content) VALUES (?)";
                PreparedStatement statement = dbConnection.prepareStatement(sql);
                statement.setString(1, messageText);

                // Step 8: Execute the SQL statement to persist the message
                statement.executeUpdate();

                // Step 9: Close the database resources
                statement.close();
                dbConnection.close();

                System.out.println("Message persisted successfully!");
            }

            // Step 10: Clean up JMS resources
            consumer.close();
            session.close();
            connection.close();
        } catch (NamingException | JMSException | SQLException e) {
            e.printStackTrace();
        }
    }
}

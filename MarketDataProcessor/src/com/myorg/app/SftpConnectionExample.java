package com.myorg.app;

import com.jcraft.jsch.*;

public class SftpConnectionExample {

    public static void main(String[] args) {
        String host = "example.com";
        int port = 22;
        String username = "your_username";
        String password = "your_password";

        String remoteFilePath = "/path/to/remote/account.txt";
        String localFilePath = "/path/to/local/account.txt";

        JSch jsch = new JSch();

        try {
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            // Download file from remote server
            channelSftp.get(remoteFilePath, localFilePath);
            System.out.println("File downloaded successfully!");

            // Upload file to remote server
            channelSftp.put(localFilePath, remoteFilePath);
            System.out.println("File uploaded successfully!");

            channelSftp.disconnect();
            session.disconnect();
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        }
    }
}

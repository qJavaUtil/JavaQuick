package com.demo.ftp;

import blxt.qjava.autovalue.autoload.AutoFtpClient;

public class FtpClientTest {

    public static void main(String[] args) throws Exception {
        AutoFtpClient autoFtpClient = new AutoFtpClient();
        MFTPClient mftpClient = (MFTPClient)autoFtpClient.inject(MFTPClient.class);
        // scanAutoLoad("");
    }
}

package com.demo.telnet;

import blxt.qjava.autovalue.autoload.AutoTelnet;
import blxt.qjava.qtelnet.QTelnetClient;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class TelnetTest {


    public static void main(String[] args) throws Exception {

      //  AutoTelnet autoFtpClient = new AutoTelnet();
      //  MQTelnetClient mqTelnetClient = autoFtpClient.inject(MQTelnetClient.class);

        TelnetOperator telnetOperator = new TelnetOperator("VT220");
        telnetOperator.login("127.0.0.1", 23, "ZhangJieLei","bbhn123");
        String res = telnetOperator.sendCommand("mkdir test");
        System.out.println("结果:" + res);
        res = telnetOperator.sendCommand("ls");
        System.out.println("结果:" + res);
    }



}

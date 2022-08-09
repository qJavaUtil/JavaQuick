package com.demo.telnet;

import blxt.qjava.qssh.QSSH;
import blxt.qjava.qtelnet.QTelnetClient;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author OpenJialei
 * @date 2022年04月09日 19:37
 */
public class SSHTest {

    public static void main(String[] args) throws IOException {
        String ip = "192.168.7.2";//change your ip
        String username = "root";//user
        String password = "root";//your server password
//        String ip = "192.168.3.60";//change your ip
//        String username = "root";//user
//        String password = "hn!3@60123";//your server password


        QSSH javaSsh = new QSSH()
            .setTag("测试")
            .setHost(ip)
            .setUserName(username)
            .setPassword(password)
            .build();

        // 回显
        javaSsh.setOnTelnetClientListener(new QTelnetClient.OnTelnetClientListener() {
            @Override
            public void onConnect(String tag, boolean isconnect) {
                System.out.println("连接" + isconnect);
            }

            @Override
            public void onDisConnect(String tag, boolean isconnect) {
                System.out.println("断开" + isconnect);
            }

            @Override
            public void onLogin(String tag, boolean isLogin) {

            }

            @Override
            public void onGetDate(String tag, byte[] data) {
                String msg =  new String(data, StandardCharsets.UTF_8);
                System.out.println("onGetDate:" + msg);
            }

            @Override
            public void onReceiver(String tag, String msg) {
            }
        });

        if(!javaSsh.connect()){
            System.out.println("连接失败");
            return;
        }

        // 连续输入
        javaSsh.sendPush("mkdir -p /home/app/client2");
        javaSsh.sendPush("ls");
        javaSsh.sendPush("cd /");
        javaSsh.sendPush("cd /home/app");
        javaSsh.sendPush("ls");
        // 独立执行
        System.out.println( "<<" + javaSsh.sendStandalone("pwd\n"));
        javaSsh.sendPush("pwd");

        System.out.println(javaSsh.sendStandalone("mkdir -p /home/app/client"));
        // 如果太早关闭, 回显会来不及接收
        // javaSsh.close();
    }
}

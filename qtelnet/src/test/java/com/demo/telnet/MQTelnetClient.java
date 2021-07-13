package com.demo.telnet;

import blxt.qjava.autovalue.inter.TelnetMark;
import blxt.qjava.qtelnet.QTelnetClient;

@TelnetMark(hostIP = "47.242.60.114", uname = "user", upwd = "user")
public class MQTelnetClient extends QTelnetClient implements QTelnetClient.OnTelnetClientListener {

    @Override
    public void onConnect(final String tag, final boolean isconnect) {
        System.out.println("连接: " + isconnect);
    }

    @Override
    public void onDisConnect(final String tag, final boolean isconnect) {
        System.out.println("断开链接: " + isconnect);
    }

    @Override
    public void onLogin(final String tag, final boolean isLogin) {
        System.out.println("登录: " + isLogin);
        System.out.println("结果:" + sendCommand("mkdir test"));
        sendCommandThread("ls");
    }

    @Override
    public void onReceiver(final String tag, final String data) {
        System.out.println("收到数据: " + data);
    }

    @Override
    public void onGetDate(final String tag, final byte[] data) {

    }

}

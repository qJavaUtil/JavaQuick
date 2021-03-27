package com.demo.ftp;

import blxt.qjava.autovalue.inter.FtpClientMark;
import blxt.qjava.qftp.QFTPClient;

import java.util.List;

@FtpClientMark(hostIp = "127.0.0.1", uname = "user", upwd = "user", port = 21, localCharset="UTF8", serverCharset="GB2312", ChangCharset=true)
public class MFTPClient extends QFTPClient implements QFTPClient.OnFTPClientListener {

    @Override
    public void onConnected(boolean isConnected) {
        System.out.println("链接" + isConnected);
    }

    @Override
    public void onDisConnected(boolean isConnected) {
        System.out.println("断开链接" + isConnected);
    }

    @Override
    public void onLogin(boolean isLogin) {
        System.out.println("登录" + isLogin);
        if (isLogin){
            makeMultiDir("/tmp/test");
            List<String> dirLists =  getDirList("/");
            for (String dirList : dirLists) {
                System.out.println("目录:" + dirList);
            }
        }
    }
}

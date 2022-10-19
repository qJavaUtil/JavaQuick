package com.demo.telnet;

import blxt.qjava.qtelnet.QTelnetClient;

public class TelnetTest {

    static String hostIp = "192.168.1.29";
    static int port = 23;
    static String username = "root";
    static String password = "root";

    public static void main(String[] args) throws Exception {
        QTelnetClientTest telnetClientTest = new QTelnetClientTest();
        telnetClientTest.test();
    }

    public static class QTelnetClientTest implements QTelnetClient.OnTelnetClientListener {

        public void test() throws InterruptedException {
            QTelnetClient telnetOperator = new QTelnetClient();
            telnetOperator.setHostIp(hostIp);
            telnetOperator.setPort(port);
            telnetOperator.setUsername(username);
            telnetOperator.setPassword(password);
            telnetOperator.setPrompt("#");
            telnetOperator.connect();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(telnetOperator.login()){
                System.out.println("登录成功");
            }
            else{
                System.out.println("登录失败");
                return;
            }

            // 登陆成功后, 再添加回调监听
//            telnetOperator.setOnTelnetClientListener(this);
//            telnetOperator.onReadThread();

            telnetOperator.setFilterColor(true);
            telnetOperator.setFilterAnsi(true);
            Thread.sleep(1000);
            String res =   telnetOperator.sendCommandWithReply("top");
            System.out.println(res);
          //  System.out.println(    telnetOperator.sendCommandWithReply("pwd"));
//            telnetOperator.write("pwd");
//            telnetOperator.write(new byte[]{13});
//            telnetOperator.write("ls /apps/app/test2");
//            telnetOperator.write(new byte[]{13});
//            Thread.sleep(1000);
          //  telnetOperator.write("/apps/app/test4/1653529652109_testddr3")
 //           telnetOperator.write("/apps/app/test2/1653374889008_iozone -i 0 –i 1 -i 2 -i 3 -i 4 -i 5 -i 6 -i 7 -r 1024K -s 3072K -f  /apps/app/file  -R");
//            String res = telnetOperator.sendCommand("mkdir test");
//            System.out.println("结果:" + res);
//            res = telnetOperator.sendCommand("ls");
//            System.out.println("结果:" + res);

            //telnetOperator.write("debug :1235 /apps/test5/application");
  //          telnetOperator.write("vi /root/startup.sh");


            while(true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }


        @Override
        public void onReceiver(String tag, String msg) {
            System.out.println(">: " + msg );
        }

        @Override
        public void onConnect(String tag, boolean isconnect) {

        }

        @Override
        public void onDisConnect(String tag, boolean isconnect) {

        }

        @Override
        public void onLogin(String tag, boolean isLogin) {

        }

        @Override
        public void onGetDate(String tag, byte[] data) {
         //   System.out.print((char)data[0]);
        }

    }



}

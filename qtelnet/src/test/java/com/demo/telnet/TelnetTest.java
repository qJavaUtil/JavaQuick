package com.demo.telnet;

import blxt.qjava.autovalue.autoload.AutoTelnet;
import blxt.qjava.qtelnet.QTelnetClient;

public class TelnetTest {

    public static void main(String[] args) throws Exception {

        AutoTelnet autoFtpClient = new AutoTelnet();
        MQTelnetClient mqTelnetClient = autoFtpClient.inject(MQTelnetClient.class);

    }

}

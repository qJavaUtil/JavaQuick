package test.model;

import blxt.qjava.autovalue.inter.AliasFor;
import blxt.qjava.autovalue.inter.Component;
import blxt.qjava.autovalue.inter.Run;
import blxt.qjava.autovalue.inter.network.UdpClient;
import blxt.qjava.qudp.QUdpClient;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/18 0:13
 */
@Component
@UdpClient(hostIp = "169.254.18.153", port = 8081)
public class MyUdpClient extends QUdpClient{


    String name = "老王";

    @Run(value = "msg=你好,", sleepTime = 100)
    public void init(@AliasFor("msg") String msg,@AliasFor("$name") String name){

        System.out.println("自动发送:" + msg + name);
        send(msg);
    }
}

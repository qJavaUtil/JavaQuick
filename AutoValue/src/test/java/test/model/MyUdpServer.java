package test.model;

import blxt.qjava.autovalue.inter.network.UdpListener;
import blxt.qjava.qudp.QUdpServer;

import java.net.DatagramPacket;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/17 21:46
 */
@UdpListener(port=8081)
public class MyUdpServer implements QUdpServer.QUdpListener{

    @Override
    public void OnUdpReceive(DatagramPacket packet) {
        System.out.print("接收数据包.");
        System.out.print(" 来源:" + packet.getAddress().getHostAddress());
        System.out.print(", 端口:" + packet.getPort());
        System.out.println(", 数据长度:" + packet.getLength());
    }
}

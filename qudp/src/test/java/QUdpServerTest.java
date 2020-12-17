import blxt.qjava.qudp.QUdpServer;

import java.net.DatagramPacket;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/10/20 0:58
 */
public class QUdpServerTest{

    public static void main(String[] args){

        QUdpServer Q = new QUdpServer( 8088, new QUdpServer.QUdpListener(){
            @Override
            public void OnUdpReceive(DatagramPacket packet) {
                System.out.println("收到消息,来自" + packet.getAddress().getHostAddress());
                // TODO 这对 buffer 进行解析
            }
        });
        Q.creat();

        Thread thread = new Thread(Q);

        // 这里run,应该放到线程重运行
        //Q.run();

        thread.start();
    }


}

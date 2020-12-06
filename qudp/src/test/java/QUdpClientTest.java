import blxt.qjava.qudp.QUdpClient;

import java.io.IOException;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/10/20 1:09
 */
public class QUdpClientTest {

    public static void main(String[] args) throws IOException {
        QUdpClient udpClient = new QUdpClient("127.0.0.1", 8088);
        udpClient.connect();
        udpClient.send("rewr");
    }
}

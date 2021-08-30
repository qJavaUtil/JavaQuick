package test;

import blxt.qjava.websocket.netty.NettyServer;
import blxt.qjava.websocket.netty.NettyWebSocketConfig;

/**
 * @author OpenJialei
 * @date 2021年08月24日 10:29
 */
public class WebsocketTest {

    public static void main(String[] args) throws Exception {
        NettyWebSocketConfig nettyWebSocketConfig = new NettyWebSocketConfig();
        nettyWebSocketConfig.setContextPath("/ws");
        nettyWebSocketConfig.setPort(8106);

        NettyServer nettyServer = new NettyServer(nettyWebSocketConfig);

        nettyServer.start();
    }
}

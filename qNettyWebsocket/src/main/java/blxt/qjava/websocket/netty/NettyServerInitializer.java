package blxt.qjava.websocket.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


import java.util.ArrayList;
import java.util.List;

/**
 * Netty 初始化
 * @author OpenJialei
 * @date 2021年08月24日 10:04
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private NettyWebSocketConfig webSocketConfig;

    public List<ChannelHandler> channelHandlers = new ArrayList<>();

    public NettyServerInitializer(NettyWebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline= socketChannel.pipeline();
        //以下三个是Http的支持
        //http解码器
        pipeline.addLast(new HttpServerCodec());
        //支持写大数据流
        pipeline.addLast(new ChunkedWriteHandler());
        //http聚合器
        pipeline.addLast(new HttpObjectAggregator(1024*64));
        //websocket支持,设置路由
        pipeline.addLast(new WebSocketServerProtocolHandler(webSocketConfig.getContextPath()));
        //添加自定义的助手类
        pipeline.addLast(new NettyDefaultHandler(webSocketConfig));
        // 遍历添加解析 handler
        for (ChannelHandler handler : channelHandlers){
            pipeline.addLast(handler);
        }
    }
}
package blxt.qjava.websocket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.annotation.PreDestroy;


/**
 * Netty-websocket Server
 * @author OpenJialei
 * @date 2021年08月24日 9:52
 */
public class NettyServer {
    NettyWebSocketConfig config;

    public NettyServer(NettyWebSocketConfig config) {
        this.config = config;
    }

    public void start() throws Exception {
        //创建主线程组，接收请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //创建从线程组，处理主线程组分配下来的io操作
        EventLoopGroup group = new NioEventLoopGroup();
        //创建netty服务器
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            // 绑定线程池, 设置主从线程组
            sb.group(group, bossGroup)
                // 指定使用的channel, 设置通道
                .channel(NioServerSocketChannel.class)
                // 绑定监听端口
                .localAddress(config.getPort())
                //子处理器，用于处理workerGroup中的操作
                .childHandler(new NettyServerInitializer(config));
            // 服务器异步创建绑定,启动server
            ChannelFuture cf = sb.bind().sync();
            // 关闭服务器通道
            cf.channel().closeFuture().sync();
        } finally {
            // 释放线程池资源
            group.shutdownGracefully().sync();
            bossGroup.shutdownGracefully().sync();
        }
    }

    public void destroy() {
        config.getBossGroup().shutdownGracefully();
        config.getWorkerGroup().shutdownGracefully();
    }
}



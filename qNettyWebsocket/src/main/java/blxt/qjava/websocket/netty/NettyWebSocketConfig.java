package blxt.qjava.websocket.netty;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Data;


/**
 * WebSocketConfig
 * @author Wilson
 * @date 2019/9/6
 **/
@Data
public class NettyWebSocketConfig {

    private Integer port;

    private String contextPath;

    private NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    private NioEventLoopGroup workerGroup= new NioEventLoopGroup();

}

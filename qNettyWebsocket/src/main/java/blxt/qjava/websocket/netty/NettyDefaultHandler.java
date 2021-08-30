package blxt.qjava.websocket.netty;

import blxt.qjava.websocket.netty.msg.WebSocketMessage;
import blxt.qjava.websocket.netty.util.RequestUtils;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 默认ws 解析器
 * @author OpenJialei
 * @date 2021年08月24日 10:04
 */
public class NettyDefaultHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    /** 所有正在连接的channel都会存在这里面，所以也可以间接代表在线的客户端 */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private NettyWebSocketConfig webSocketConfig;

    public NettyDefaultHandler(NettyWebSocketConfig webSocketConfig){
        this.webSocketConfig = webSocketConfig;
    }


    /** 在线人数 */
    public static int online;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // WebSocket通过Http握手建立起长连接
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            // 提取地址栏参数
            JSONObject paramsJson = RequestUtils.urlParamsToJson(request.uri());
            // 清空参数重置路径，故不能与上一行提取互换
            httpRequestHandle(ctx, request);
        }
        super.channelRead(ctx, msg);
    }


    /** 接收到客户都发送的消息 */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("收到消息:" + msg.text());
    }

    /** 客户端建立连接 */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channelGroup.add(ctx.channel());
        online=channelGroup.size();
        System.out.println("链接:" +ctx.channel().remoteAddress());
    }
    /** 关闭连接 */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.remove(ctx.channel());
        online=channelGroup.size();
        System.out.println("断开连接:" +ctx.channel().remoteAddress());
    }

    /** 出现异常 */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println("出现异常:" +ctx.channel().remoteAddress());
    }

    /**
     * 处理连接请求，客户端WebSocket发送握手包时会执行这一次请求
     *
     * @param ctx
     * @param request
     */
    private void httpRequestHandle(ChannelHandlerContext ctx, FullHttpRequest request) {
        String uri = request.uri();
        // 判断配置的websocket contextPath与请求地址中的contextPath是否一致
        if (webSocketConfig.getContextPath().equals(RequestUtils.getBasePath(uri))) {
            // 因为有可能携带了参数，导致客户端一直无法返回握手包，因此在校验通过后，重置请求路径
            request.setUri(webSocketConfig.getContextPath());
        } else {
            ctx.close();
        }
    }

    /** 给某个人发送消息 */
    public void sendMessage(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
    }

    /** 给每个人发送消息,除发消息人外 */
    public void broadcastMessage(ChannelHandlerContext ctx, String msg) {
        for(Channel channel:channelGroup){
            if(!channel.id().asLongText().equals(ctx.channel().id().asLongText())){
                channel.writeAndFlush(new TextWebSocketFrame(msg));
            }
        }
    }
}
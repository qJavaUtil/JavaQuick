package blxt.qjava.httpserver;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

/**
 * HttpServer 工厂,使用默认HttpHandler.@see {@link QHttpHandler}.
 * @Author: Zhang.Jialei
 * @Date: 2020/9/24 10:52
 */
public class HttpServerFactory {

    static HttpServerFactory instance = null;
    static HttpHandler httpHandler = new QHttpHandler();

    public static HttpServerFactory getInstance(){
        return instance;
    }

    public static HttpServerFactory newInstance(String path, int port, int backlog) throws IOException {
        if(instance == null){
            instance = new HttpServerFactory(path, port, backlog);
        }
        return instance;
    }

    /** HttpServer */
    HttpServer httpServer;
    /** 默认端口 */
    int port = 8080;
    /** 默认backlog */
    int backlog = 0;
    /** 默认根路径 */
    String path="/";
    /** 默认Executor */
    Executor executor = null;

    private HttpServerFactory(String path, int port, int backlog) throws IOException {
        this.port = port;
        this.backlog = backlog;
        this.path = path;

        bind();
    }

    private void bind() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), backlog);
        httpServer.createContext(path, httpHandler);
        httpServer.setExecutor(executor);
        System.out.println("启动HttpServer:" + port);
    }

    public static void setHttpHandler(HttpHandler httpHandler){
        HttpServerFactory.httpHandler = httpHandler;
    }

    public void start(){
        httpServer.start();
    }

}

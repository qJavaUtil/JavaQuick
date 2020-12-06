package blxt.qjava.httpserver;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

/**
 * HttpServer 工厂,使用默认HttpHandler.@see {@link QHttpHandler}.
 * @Author: Zhang.Jialei
 * @Date: 2020/9/24 10:52
 */
public class QHttpServer {

    static QHttpServer instance = null;

    public static QHttpServer getInstance(){
        return instance;
    }

    public static QHttpServer newInstance(String path, int port, int backlog) throws IOException {
        if(instance != null){
            return instance;
        }

        instance = new QHttpServer(path, port, backlog);

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

    private QHttpServer( String path, int port, int backlog) throws IOException {
        this.port = port;
        this.backlog = backlog;
        this.path = path;

        bind();
    }

    private void bind() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), backlog);
        httpServer.createContext(path, new QHttpHandler());
        httpServer.setExecutor(executor);
    }


    public void start(){
        httpServer.start();
    }


}

package blxt.qjava.httpserver;

import blxt.qjava.autovalue.autoload.AutoRestController;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

/**
 * HttpServer 工厂,使用默认HttpHandler.@see {@link QHttpHandler}.
 * @Author: Zhang.Jialei
 * @Date: 2020/9/24 10:52
 */
public class HttpServerFactory {

    static Logger logger = LoggerFactory.getLogger(HttpServerFactory.class);

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
        if(!this.path.startsWith("/")){
            this.path = "/" + this.path;
        }
        if(this.path.length() > 1 && this.path.endsWith("/")){
            this.path = this.path.substring(0, this.path.length() - 1);
        }

        bind();
    }

    private HttpServerFactory bind() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), backlog);
        httpServer.createContext(path, httpHandler);
        httpServer.setExecutor(executor);
        if(path.length() > 1){
            AutoRestController.appPath = path;
        }
        logger.debug("启动HttpServer, 端口:{}, 路径:{}", port, path);
        return this;
    }

    public static void setHttpHandler(HttpHandler httpHandler){
        HttpServerFactory.httpHandler = httpHandler;
    }

    public HttpServerFactory start(){
        httpServer.start();
        return this;
    }

}

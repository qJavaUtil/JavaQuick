package blxt.qjava.autovalue.autoload;


import blxt.qjava.autovalue.inter.EnHttpServer;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.reflect.PackageUtil;
import blxt.qjava.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpHandler;

@AutoLoadFactory(name="AutoHttpServer", annotation = EnHttpServer.class, priority = 40)
public class AutoHttpServer extends AutoLoadBase{

    public AutoHttpServer(){

    }

    @Override
    public  <T> T inject(Class<?> object) throws Exception {

        /** 默认端口 */
        int port = 8080;
        /** 默认backlog */
        int backlog = 0;
        /** 默认根路径 */
        String path="/";

        if(!AutoValue.isNull("server.port")){
            port = (int)AutoValue.getPropertiesValue("server.port", int.class);
        }
        if(!AutoValue.isNull("server.path")){
            path = (String)AutoValue.getPropertiesValue("server.path", String.class);
        }
        if(!AutoValue.isNull("server.HttpHandler")){
            String handlerName = (String)AutoValue.getPropertiesValue("server.HttpHandler", String.class);
            Class handlerNameClass = Class.forName(handlerName);
            if(PackageUtil.isInterfaces(handlerNameClass, HttpHandler.class)){
                throw  new Exception("错误的指定HttpHandler类," + handlerName);
            }
            HttpHandler handler = (HttpHandler)handlerNameClass.newInstance();
            HttpServerFactory.setHttpHandler(handler);
        }
        HttpServerFactory factory = HttpServerFactory.newInstance(path, port, backlog);
        factory.start();

        return (T) factory;
    }
}

package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.QJavaApplication;
import blxt.qjava.autovalue.inter.UdpListener;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.reflect.PackageUtil;
import blxt.qjava.autovalue.util.QThreadpool;
import blxt.qjava.qudp.QUdpServer;

import java.util.HashMap;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/17 21:18
 */
@AutoLoadFactory(name="AutoUdpServer", annotation = UdpListener.class, priority = 20)
public class AutoUdpServer extends AutoLoadBase{

    static HashMap<Object, QUdpServer> udpServerHashMap = new HashMap<>();

    public static void load(){
        QJavaApplication.addAutoLoadBases(AutoUdpServer.class);
    }

    @Override
    public Object inject(Class<?> object) throws Exception {
        // 从类注解UdpListener中获取监听端口

        UdpListener anno = object.getAnnotation(UdpListener.class);
        if(!PackageUtil.isInterfaces(object, QUdpServer.QUdpListener.class)){
            throw new Exception("拥有@UdpListener注解的方法,需要实现QUdpServer.QUdpListene接口");
        }
        // 自动实现QUdpServer
        QUdpServer.QUdpListener bean = (QUdpServer.QUdpListener)ObjectPool.getObject(object);
        int port = anno.port();
        QUdpServer udpServer = new QUdpServer(port, bean);
        udpServerHashMap.put(object, udpServer);
        udpServer.setMAX_LENGTH(anno.packageSize());
        if(!udpServer.creat()){
            return null;
        }
        QThreadpool.getInstance().execute(udpServer);

        return null;
    }


    /**
     * 关闭并移除服务
     * @param key
     */
    public static void close(Object key){
        QUdpServer server = udpServerHashMap.get(key);
        if(server != null){
            server.close();
        }
        server = null;
        udpServerHashMap.remove(key);
    }

}

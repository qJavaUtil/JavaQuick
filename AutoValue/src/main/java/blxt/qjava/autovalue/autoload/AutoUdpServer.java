package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.inter.network.UdpClient;
import blxt.qjava.autovalue.inter.network.UdpListener;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.util.PackageUtil;
import blxt.qjava.autovalue.util.QThreadpool;
import blxt.qjava.qudp.QUdpServer;

import java.lang.annotation.Annotation;
import java.util.List;

import static blxt.qjava.autovalue.util.PackageUtil.getClassName;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/17 21:18
 */
@AutoLoadFactory(annotation = UdpListener.class, priority = 20)
public class AutoUdpServer extends AutoLoadBase{


    @Override
    public void init(Class<?> object) throws Exception {

    }

    /**
     * 包扫描,扫描Configuration注解
     * @param packageName     要扫描的包路径
     * @throws Exception
     */
    @Override
    public void scan(String packageName) throws Exception {
        List<String> classNames = getClassName(packageName, true);
        if (classNames != null) {
            for (String className : classNames) {
                // 过滤测试类
                if (className.indexOf("test-classes") > 0) {
                    className = className.substring(className.indexOf("test-classes") + 13);
                }

                Class<?> objClass = Class.forName(className);
                Annotation classAnnotation = objClass.getAnnotation(UdpListener.class);
                if (classAnnotation == null) {
                    continue;
                }
                inject(objClass);
            }
        }
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
        udpServer.setMAX_LENGTH(anno.packageSize());
        if(!udpServer.creat()){
            throw new Exception("创建UdpListener失败,可能端口被占用:" + port);
        }
        QThreadpool.getInstance().execute(udpServer);

        return null;
    }
}

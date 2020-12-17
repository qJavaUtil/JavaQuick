package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.network.UdpClient;
import blxt.qjava.autovalue.inter.network.UdpListener;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.util.PackageUtil;
import blxt.qjava.autovalue.util.QThreadpool;
import blxt.qjava.qudp.QUdpClient;
import blxt.qjava.qudp.QUdpServer;

import java.lang.annotation.Annotation;
import java.util.List;

import static blxt.qjava.autovalue.util.PackageUtil.getClassName;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/17 21:18
 */
public class AutoUdpClient extends AutoLoadBase{


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
                Annotation classAnnotation = objClass.getAnnotation(UdpClient.class);
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

        UdpClient anno = object.getAnnotation(UdpClient.class);
        if(anno == null){
            return null;
        }

        if(!QUdpClient.class.isAssignableFrom(object)){
            throw new Exception("拥有@UdpClient,需要集成QUdpClient");
        }

        // 自动实现QUdpServer
        QUdpClient bean = (QUdpClient)ObjectPool.getObject(object);
        if(!bean.connect(anno.hostIp(), anno.port())){
            throw new Exception("创建QUdpClient失败,可能端口被占用:" + anno.port());
        }

        return null;
    }
}

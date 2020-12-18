package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.Component;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.inter.network.UdpClient;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.qudp.QUdpClient;

import java.lang.annotation.Annotation;
import java.util.List;

import static blxt.qjava.autovalue.util.PackageUtil.getClassName;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/17 21:18
 */
@AutoLoadFactory(annotation = UdpClient.class, priority = 20)
public class AutoUdpClient extends AutoLoadBase{

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

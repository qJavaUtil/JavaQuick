package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.QJavaApplication;
import blxt.qjava.autovalue.inter.UdpClient;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.util.ObjectValue;
import blxt.qjava.qudp.QUdpClient;
import blxt.qjava.qudp.QUdpServer;

import java.util.HashMap;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/17 21:18
 */
@AutoLoadFactory(name="AutoUdpClient", annotation = UdpClient.class, priority = 20)
public class AutoUdpClient extends AutoLoadBase{
   // static HashMap<Object, QUdpClient> udpClientHashMap = new HashMap<>();

    public static void load(){
        QJavaApplication.addAutoLoadBases(AutoUdpClient.class);
    }

    @Override
    public Object inject(Class<?> object) throws Exception {
        // 从类注解UdpListener中获取监听端口

        UdpClient anno = object.getAnnotation(UdpClient.class);
        if(anno == null){
            return null;
        }

        if(!QUdpClient.class.isAssignableFrom(object)){
            throw new Exception("拥有@UdpClient,需要继承QUdpClient");
        }

        // 自动实现QUdpClient
        QUdpClient bean = (QUdpClient)ObjectPool.getObject(object);
   //     String host = (String) ObjectValue.getValue(bean, String.class, anno.hostIp(), anno.hostIp());
        String host = anno.hostIp();
        int port = anno.port();

        if(!bean.connect(host, port)){
            throw new Exception("创建QUdpClient失败,可能端口被占用:" + anno.port());
        }

        return bean;
    }
}

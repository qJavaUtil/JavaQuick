package blxt.qjava.autovalue;


import blxt.qjava.autovalue.autoload.*;
import blxt.qjava.autovalue.inter.ComponentScan;
import blxt.qjava.autovalue.inter.ConfigurationScan;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.util.QThreadpool;

import java.util.ArrayList;

/**
 * qjava框架注释自动加载启动类
 *
 * @Author: Zhang.Jialei
 * @Date: 2020/12/6 16:32
 */
public class QJavaApplication {

    /**
     * 开始实现注解
     *
     * @param object 启动类,建议启动类在根包路径下,否则需要手动添加扫描路径
     * @throws Exception
     */
    public static void run(Class<?> object) throws Exception {

        if (object.getPackage() == null) {
            throw new Exception("启动类必须拥有包名");
        }
        // 先加载自身的注解
        if(ObjectPool.isEmpty(object)){
            ObjectPool.putObject(QJavaApplication.class);
            QJavaApplication.run(QJavaApplication.class);
        }

        // Configuration扫描, 实现@Value
        AutoValue autoValue = new AutoValue();
        autoValue.packageScan(object);
        // Component扫描,实现 @Autowired
        AutoObject autoObject = new AutoObject();
        autoObject.packageScan(object);
        // Component扫描,实现 @Run
        AutoMethod autoMethod = new AutoMethod();
        autoMethod.packageScan(object);

        AutoUdpServer autoUdpServer = new AutoUdpServer();
        autoUdpServer.packageScan(object);
        AutoUdpClient autoUdpClient = new AutoUdpClient();
        autoUdpClient.packageScan(object);
    }

//    public static void runByPaty(String path) throws Exception {
//        if (path == null) {
//            throw new Exception("启动类必须拥有包名");
//        }
//        // 先加载自身的注解
//        if(ObjectPool.isEmpty(QJavaApplication.class)){
//            ObjectPool.putObject(QJavaApplication.class);
//            QJavaApplication.run(QJavaApplication.class);
//        }
//
//        // Configuration扫描, 实现@Value
//        AutoValue autoValue = new AutoValue();
//        autoValue.packageScan(path);
//        // Component扫描,实现 @Autowired
//        AutoObject autoObject = new AutoObject();
//        autoObject.packageScan(path);
//        // Component扫描,实现 @Run
//        AutoMethod autoMethod = new AutoMethod();
//        autoMethod.packageScan(path);
//
//        AutoUdpServer autoUdpServer = new AutoUdpServer();
//        autoUdpServer.packageScan(path);
//        AutoUdpClient autoUdpClient = new AutoUdpClient();
//        autoUdpClient.packageScan(path);
//    }

}

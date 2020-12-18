package blxt.qjava.autovalue;


import blxt.qjava.autovalue.autoload.*;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ObjectPool;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static blxt.qjava.autovalue.util.PackageUtil.getClassName;

/**
 * qjava框架注释自动加载启动类
 *
 * @Author: Zhang.Jialei
 * @Date: 2020/12/6 16:32
 */
public class QJavaApplication {

    static List<AutoLoadBase> autoLoadBases = new ArrayList<>() ;

    static {
        // Configuration扫描, 实现@Value
//        autoLoadBases.add(new AutoValue());
//        // Component扫描,实现 @Autowired
//        autoLoadBases.add(new AutoObject());
//        // Component扫描,实现 @Run
//        autoLoadBases.add(new AutoMethod());
//        // Component扫描,实现@UdpListener
//        autoLoadBases.add(new AutoUdpServer());
//        // Component扫描,实现@UdpClient
//        autoLoadBases.add(new AutoUdpClient());
        autoLoadBases.addAll(scanAutoLoad("blxt.qjava.autovalue.autoload"));
    }


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

        // 扫描指定包路径, 实现自动装载
        for(AutoLoadBase autoLoad : autoLoadBases){
            autoLoad.packageScan(object);
        }

    }


    /***
     * 扫描AutoLoad
     * @param packageName  包名
     * @return
     */
    public static List<AutoLoadBase> scanAutoLoad(String packageName){
        List<AutoLoadBase> autoLoads = null;

        List<String> classNames = getClassName(packageName, true);
        if (classNames != null) {
            autoLoads = new ArrayList<>();
            for (String className : classNames) {
                // 过滤测试类
                if (className.indexOf("test-classes") > 0) {
                    className = className.substring(className.indexOf("test-classes") + 13);
                }

                try {
                    Class<?> objClass = Class.forName(className);
                    AutoLoadFactory annotation = objClass.getAnnotation(AutoLoadFactory.class);
                    if (annotation == null) {
                        continue;
                    }

                    AutoLoadBase bean = (AutoLoadBase) objClass.newInstance();
                    // 设置优先级
                    bean.setPriority(annotation.priority());
                    // 设置类注解扫描
                    bean.setAnnotation(annotation.annotation());

                    autoLoads.add(bean);
                } catch (ClassNotFoundException
                        | InstantiationException
                        | IllegalAccessException ignored) {
                }
            }
            // 按优先级排序, 优先加载优先值底的
            Collections.sort(autoLoads);
        }

        return autoLoads;
    }

}

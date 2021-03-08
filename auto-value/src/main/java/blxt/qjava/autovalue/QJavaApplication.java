package blxt.qjava.autovalue;


import blxt.qjava.autovalue.autoload.*;
import blxt.qjava.autovalue.inter.Configuration;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.reflect.PackageUtil;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.*;

/**
 * qjava框架注释自动加载启动类
 *
 * @Author: Zhang.Jialei
 * @Date: 2020/12/6 16:32
 */
public class QJavaApplication {

    static List<AutoLoadBase> autoLoadBases = new ArrayList<>() ;

    public static Map<String, String> listPassPageName = new HashMap<>();

    static {
        try {
            autoLoadBases.addAll(scanAutoLoad("blxt.qjava.autovalue.autoload"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        // 首次注入时
        if(ObjectPool.isEmpty(object)){
            // autovalue作为基础必需控件,必需优先启动, 并且只加载一次,扫描路径是输入的用户类
            AutoValue.scanPropertiesFile(object);
            // 先加载内部的注解
            ObjectPool.putObject(QJavaApplication.class);
            QJavaApplication.run(QJavaApplication.class);
            //System.out.println("内部自注解加载完成");
        }

        // 按优先级排序, 优先加载优先值底的
        Collections.sort(autoLoadBases);
        //System.out.println("开始注解:" + object.getName());

        // 扫描指定包路径, 实现自动装载
        for(AutoLoadBase autoLoad : autoLoadBases){
//            System.out.println(String.format("模块:%s, 优先级:%d, 扫描注解:%s",
//                    autoLoad.getName(),autoLoad.getPriority(), autoLoad.getAnnotation().getName()));
            autoLoad.packageScan(object);
        }

    }


    /***
     * 扫描AutoLoad
     * @param packageName  包名
     * @return
     */
    public static List<AutoLoadBase> scanAutoLoad(String packageName) throws IOException {
        List<AutoLoadBase> autoLoads = new ArrayList<>();

        //List<String> classNames = PackageUtil.getClassName(QJavaApplication.class.getClassLoader(), packageName);
        ImmutableSet<ClassPath.ClassInfo> classInfos =
                PackageUtil.getClassInfo(QJavaApplication.class.getClassLoader(), packageName);

        if(classInfos != null){
            for (ClassPath.ClassInfo classInfo : classInfos) {

 //               String className = classInfo.getName();
                // 过滤测试类
//                if (className.indexOf("test-classes") > 0) {
//                    className = className.substring(className.indexOf("test-classes") + 13);
//                }

                Class objClass = classInfo.load();
                if (!AutoLoadBase.class.isAssignableFrom(objClass)){
                    continue;
                }
                AutoLoadBase bean = analysisAutoLoad(objClass);
                if(bean == null) {
                    continue;
                }
                autoLoads.add(bean);
            }
        }


//        if (classNames != null) {
//            autoLoads = new ArrayList<>();
//            for (String className : classNames) {
//                // 过滤测试类
//                if (className.indexOf("test-classes") > 0) {
//                    className = className.substring(className.indexOf("test-classes") + 13);
//                }
//
//                try {
//                    Class objClass = Class.forName(className);
//                    if (!AutoLoadBase.class.isAssignableFrom(objClass)){
//                        continue;
//                    }
//                    AutoLoadBase bean = analysisAutoLoad(objClass);
//                    if(bean == null) {
//                       continue;
//                    }
//                    autoLoads.add(bean);
//                } catch (ClassNotFoundException ignored) {
//                    System.err.println("自动装载类实现异常:" + className);
//                }
//            }
//        }

        return autoLoads;
    }

    /**
     * AutoLoadBase 类实现解析
     * @param autoLoadClass
     * @return
     */
    public static AutoLoadBase analysisAutoLoad(Class<? extends AutoLoadBase> autoLoadClass){

        try {
            AutoLoadFactory annotation = autoLoadClass.getAnnotation(AutoLoadFactory.class);
            if (annotation == null) {
                return null;
            }

            AutoLoadBase autoLoadBean = autoLoadClass.newInstance();
            autoLoadBean.setName(annotation.name());
            // 设置优先级
            autoLoadBean.setPriority(annotation.priority());
            // 设置类注解扫描
            autoLoadBean.setAnnotation(annotation.annotation());

            return autoLoadBean;
        } catch (InstantiationException
                | IllegalAccessException ignored) {
            System.err.println("自动装载类实现异常:" + autoLoadClass.getName());
        }

        return null;
    }

    /**
     * 添加自动装载实现
     * @param autoLoad
     */
    public static void addAutoLoadBases(Class<? extends AutoLoadBase> autoLoad) {
        QJavaApplication.autoLoadBases.add(analysisAutoLoad(autoLoad));
    }
}

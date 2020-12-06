package blxt.qjava.autovalue;

import blxt.qjava.autovalue.inter.Configuration;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import static blxt.qjava.autovalue.PackageUtil.getClassName;

/**
 * qjava框架注释自动加载启动类
 * @Author: Zhang.Jialei
 * @Date: 2020/12/6 16:32
 */
public class QJavaApplication {

    public static void run(Class<?> object) throws ClassNotFoundException, Exception {

        if(object.getPackage() == null){
            throw new Exception("启动类必须拥有包名");
        }

        String packageName = object.getPackage().getName();



    }

    public static void run(String packageName) throws Exception {

        List<String> classNames = getClassName(packageName, true);
        if (classNames != null) {
            for (String className : classNames) {
                Class objClass = Class.forName(className);

                // 先处理优先级高的注解
                Annotation subAnnotation = objClass.getAnnotation(Configuration.class);
                printAnnotation("Configuration",subAnnotation);

                // 再处理其他的
               // Annotation[] allAnnos = objClass.getAnnotations();
                //printAnnotation("all",allAnnos);
            }
        }
    }

    /**
     * 注解分配处理
     * @param msg
     * @param annotations
     */
    private static void printAnnotation(String msg,Annotation... annotations){
        System.out.println("=============="+msg+"======================");
        if(annotations == null){
            System.out.println("Annotation is null");
        }
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }
        System.out.println();
    }
}

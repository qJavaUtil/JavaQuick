package blxt.qjava.autovalue.autoload;


import blxt.qjava.autovalue.inter.Component;
import blxt.qjava.autovalue.inter.ComponentScan;
import blxt.qjava.autovalue.interfaces.AutoLoad;
import blxt.qjava.autovalue.util.PackageUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/7 13:53
 */
public abstract class AutoLoadBase implements AutoLoad ,Comparable<AutoLoadBase>{

    public static boolean isDebug = false;

    /** 优先级 */
    int priority = 10;
    /** 扫描类注解 */
    Class<? extends Annotation > annotation;

    /**
     * Component扫描,实现 @Autowired
     * @param object object     启动类,建议启动类在根包路径下,否则需要手动添加扫描路径
     * @throws Exception
     */
    public void packageScan(Class<?> object) throws Exception {
        String path = getScanPackageName(object);
        init(object);
        if(path == null){
            path = object.getPackage().getName();
        }
        packageScan(path);
    }

    public void packageScan(String path) throws Exception {
        if (path != null) {
            /* 指定包扫描 */
            ArrayList<String> arrayList = getValuesSplit(path, ",");
            for (String p : arrayList) {
                scan(p);
            }
            return;
        }
        /* 默认包扫描 */
        scan(path);
    }

    /**
     * 包扫描, 对有Component注释的类, 自动实现 autoWired注解
     * @param packageName  要扫描的包名
     */
    @Override
    public void scan(String packageName) throws Exception {
        List<String> classNames = PackageUtil.getClassName(packageName, true);
        if (classNames != null) {
            for (String className : classNames) {
                // 过滤测试类
                if(className.indexOf("test-classes") > 0){
                    className = className.substring(className.indexOf("test-classes") + 13);
                }

                Class<?>  objClass = Class.forName(className);
                Annotation classAnnotation = objClass.getAnnotation(Component.class);
                if(classAnnotation == null){
                    continue;
                }
                inject(objClass);
            }
        }
    }

    /**
     * 获取注解中的参数列表
     * @param str
     * @param var1
     * @return
     */
    private ArrayList<String> getValuesSplit(String str, String var1){
        String[] paths = str.split(var1);
        ArrayList<String> arrayList = new ArrayList();
        for (String p : paths) {
            if (p == null || p.trim().isEmpty()) {
                continue;
            }
            arrayList.add(p);
        }
        return arrayList;
    }

    /**
     * 获取启动类的ComponentScan注解路径
     * @param objClass 要扫描的包路径
     * @throws Exception
     */
    public String getScanPackageName(Class<?> objClass) {
        ComponentScan annotation = objClass.getAnnotation(ComponentScan.class);
        if (annotation == null) {
            return null;
        }
        return annotation.value();
    }


    public void setPriority(int priority){
        this.priority = priority;
    }
    public int getPriority(){
        return priority;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    public int compareTo(AutoLoadBase bean) {
        // 根据优先级升序, 自动加载是,从第优先级开始加载
        return this.priority - bean.getPriority();
    }

}

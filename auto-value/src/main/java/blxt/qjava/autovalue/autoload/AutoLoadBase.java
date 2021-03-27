package blxt.qjava.autovalue.autoload;


import blxt.qjava.autovalue.QJavaApplication;
import blxt.qjava.autovalue.inter.ComponentScan;
import blxt.qjava.autovalue.interfaces.AutoLoad;
import blxt.qjava.autovalue.reflect.PackageUtil;
import blxt.qjava.autovalue.util.ObjectValue;
import blxt.qjava.utils.system.JavaVersionUtils;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static blxt.qjava.autovalue.QJavaApplication.listPassPageName;


/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/7 13:53
 */
public abstract class AutoLoadBase implements AutoLoad ,Comparable<AutoLoadBase>{

    public static boolean isDebug = false;
    /** java8 以上, 不允许使用setAccessible(true) **/
    public static boolean falSetAccessible = JavaVersionUtils.getJDKVersion() <= 52;

    String name = "";
    /** 优先级 */
    int priority = 10;
    /** 扫描类注解 */
    Class<? extends Annotation > annotation;


    /**
     * 注解初始化
     * */
    public void init(Class<?> rootClass){

    }


    /**
     * Component扫描,实现 @Autowired
     * @param object object     启动类,建议启动类在根包路径下,否则需要手动添加扫描路径
     * @throws Exception
     */
    public void packageScan(Class<?> object) {
        String path = getScanPackageName(object);
        init(object);
        if(path == null){
            path = object.getPackage().getName();
        }
        packageScan(path);
    }

    public void packageScan(String path) {
        if (path != null) {
            /* 指定包扫描 */
            ArrayList<String> arrayList = getValuesSplit(path, ",");
            for (String p : arrayList) {
                /* 忽略指定包 */
                if(listPassPageName.get(p) != null){
                    continue;
                }
                try{
                    scan(p);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return;
        }
        /* 默认包扫描 */
        /* 忽略指定包 */
        if(listPassPageName.get(path) != null){
            try {
                scan(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 包扫描, 对有Component注释的类, 自动实现 autoWired注解
     * @param packageName  要扫描的包名
     */
    @Override
    public void scan(String packageName) throws IOException {
        ImmutableSet<ClassPath.ClassInfo> classInfos =
                PackageUtil.getClassInfoAll(QJavaApplication.class.getClassLoader(), packageName);

        for (ClassPath.ClassInfo classInfo : classInfos) {
//

            Class objClass = null;
            objClass = classInfo.load();
            Annotation classAnnotation = objClass.getAnnotation(annotation);
            // 跳过注解
            if (objClass.isAnnotation()){
                continue;
            }
            if(classAnnotation == null){
                continue;
            }
            try {
                inject(objClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        List<String> classNames = PackageUtil.getClassNameAll(AutoLoadBase.class.getClassLoader(), packageName);

//        for (String className : classNames) {
//            // 过滤测试类
//            if(className.indexOf("test-classes") > 0){
//                className = className.substring(className.indexOf("test-classes") + 13);
//            }
//
//            try {
//                Class<?> objClass = Class.forName(className);
//                if (objClass.isEnum() || objClass.isAnnotation()
//                        || objClass.isInterface()){
//                    continue;
//                }
//                Annotation classAnnotation = objClass.getAnnotation(annotation);
//                if(classAnnotation == null){
//                    continue;
//                }
//                inject(objClass);
//            } catch (Exception e) {
//            }
//        }
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
        if(annotation.value().isEmpty()){
            return null;
        }
        return annotation.value();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public Object getValue(Object bean, Class<?> parameterType, String key, Object defalut){
        Object value = defalut;
        if(key.startsWith("$")){
            key = key.substring(1);
            try {
                value = ObjectValue.getObjectValue(bean, key, parameterType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

}

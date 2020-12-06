/***********************************************************************************************************************
 *   通过注释 + 反射,实现配置文件自动加载
 *   -- 功能
 *   实现int,string,boolean,short,float,double类型的参数自动获取
 *   -- 技术点
 *   类注释获取
 *   变量注释获取
 *   通过反射设置变量值
 **********************************************************************************************************************/
package blxt.qjava.autovalue;


import com.blxt.properties.PropertiesFactory;

import blxt.qjava.autovalue.inter.*;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import static blxt.qjava.autovalue.PackageUtil.getClassName;

/**
 * 自动装载属性
 *
 * @Author: Zhang.Jialei
 * @Date: 2020/9/28 17:26
 */
public class AutoValue {
    /**
     * 项目起始类
     */
    static Class<?> rootClass;
    /**
     * 默认的配置读取工具
     */
    static PropertiesFactory propertiesFactory;

    /**
     * 默认配置文件
     */
    private static String filePath[] = new String[]{"resources/application.properties",
            "application.properties",
            "../application.properties",
            "../../application.properties",
            "../../../application.properties"};

    /***
     * 自动属性初始化
     * @param rootClass      项目起始类
     * @throws IOException
     */
    public static void init(Class<?> rootClass) throws Exception {
        if (AutoValue.rootClass != null) {
            return;
        }
        AutoValue.rootClass = rootClass;
        String appPath = getPath();
        for (String path : filePath) {
            File file = new File(appPath + File.separator + path);
            if (file.exists()) {
                propertiesFactory = new PropertiesFactory(file);
                System.out.println("AutoValue 默认配置文件:" + file.getPath());
                break;
            }
        }
        if (propertiesFactory == null) {
            System.out.println("AutoValue 没有找到配置文件:" + getPath());
        }
    }

    /**
     * 包扫描,扫描Configuration注解
     */
    public static void scan(String packageName) throws Exception {
        List<String> classNames = getClassName(packageName, true);
        if (classNames != null) {
            for (String className : classNames) {
                // 过滤测试类
                if (className.indexOf("test-classes") > 0) {
                    className = className.substring(className.indexOf("test-classes") + 13);
                }

                Class<?> objClass = Class.forName(className);
                Annotation classAnnotation = objClass.getAnnotation(Configuration.class);
                if (classAnnotation == null) {
                    continue;
                }

                Object obj = ObjectPool.putObject(objClass);
                if (obj == null) {
                    continue;
                }
                autoVariable(obj);
            }
        }
    }

    /**
     * 从指定 Properties 中获取配置
     *
     * @param bean
     * @param properties
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static void autoVariable(Object bean, PropertiesFactory properties) throws IllegalAccessException {
        // 获取f对象对应类中的所有属性域
        Field[] fields = bean.getClass().getDeclaredFields();


        // 遍历属性
        for (Field field : fields) {
            // 过滤 final 元素
            if ((field.getModifiers() & 16) != 0) {
                continue;
            }
            Value valuename = field.getAnnotation(Value.class);
            if (valuename == null) {
                continue;
            }

            // 参数类型
            String fieldType = field.getGenericType().toString();

            // 获取原来的访问控制权限
            boolean accessFlag = field.isAccessible();
            // 修改访问控制权限
            field.setAccessible(true);

            // 属性值
            Object value = null;

            if (fieldType.endsWith("String")) {
                value = properties.getStr(valuename.value());
            } else if (fieldType.endsWith("Integer") || fieldType.endsWith("int")) {
                value = properties.getInt(valuename.value());
            } else if (fieldType.endsWith("Short") || fieldType.endsWith("short")) {
                value = properties.getShort(valuename.value());
            } else if (fieldType.endsWith("Boolean") || fieldType.endsWith("boolean")) {
                value = properties.getBoolean(valuename.value());
            } else if (fieldType.endsWith("Double") || fieldType.endsWith("double")) {
                value = properties.getDouble(valuename.value());
            } else if (fieldType.endsWith("Float") || fieldType.endsWith("float")) {
                value = properties.getFloat(valuename.value());
            } else if (fieldType.endsWith("Long") || fieldType.endsWith("long")) {
                value = properties.getLong(valuename.value());
            }
            if (value != null) {
                field.set(bean, value);
            }

            // 恢复访问控制权限
            field.setAccessible(accessFlag);
        }
    }


    /**
     * 自动 获取变量名
     *
     * @param bean
     */
    public static void autoVariable(Object bean) throws IllegalAccessException, IOException {
        PropertiesFactory properties = propertiesFactory;

        // 从类注解Configuration中获取值,判断是否是自定义的配置文件路径
        Configuration anno = bean.getClass().getAnnotation(Configuration.class);
        if (anno != null) {
            String propertiePath = anno.value();
            if (!propertiePath.trim().isEmpty()) {
                // 如果文件路径是以 . 开头的,那么认为这是相对路径
                if (propertiePath.startsWith(".")) {
                    properties = new PropertiesFactory(new File(getPath() + File.separator + propertiePath));
                } else {
                    properties = new PropertiesFactory(new File(propertiePath));
                }
            }
        }

        // TODO 实现Configuration注解中的proxyBeanMethods,自定义的properties解析类
        autoVariable(bean, properties);
    }

    /**
     * 自动 获取变量名
     *
     * @param bean
     * @param directory 目录
     * @param fileName  文件名
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static void autoVariable(Object bean, String directory, String fileName) throws IllegalAccessException, IOException {
        // 如果文件路径是以 . 开头的,那么认为这是相对路径
        PropertiesFactory properties =
                new PropertiesFactory(new File(directory + File.separator + fileName));

        autoVariable(bean, properties);
    }

    /**
     * 获取jar运行路径
     *
     * @return
     */
    public static String getPath() {
        String path = rootClass.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (System.getProperty("os.name").contains("dows")) {
            path = path.substring(1, path.length());
        }
        if (path.contains("jar")) {
            path = path.substring(0, path.lastIndexOf("."));
            return path.substring(0, path.lastIndexOf("/"));
        }
        return path.replace("/classes/", "")
                .replace("/test-classes/", "")
                .replace("/target", "/src/test");
    }


}

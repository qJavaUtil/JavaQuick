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


import blxt.qjava.autovalue.interfaces.AutoLoadBase;
import blxt.qjava.autovalue.util.ConvertTool;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.util.PackageUtil;
import blxt.qjava.properties.PropertiesFactory;

import blxt.qjava.autovalue.inter.*;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import static blxt.qjava.autovalue.util.PackageUtil.getClassName;

/**
 * 自动装载属性
 *
 * @Author: Zhang.Jialei
 * @Date: 2020/9/28 17:26
 */
public class AutoValue extends AutoLoadBase {
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
    private static String filePath[] = new String[]{
            "./resources/config/application.properties",
            "./config/application.properties",
            "./resources/application.properties",
            "./application.properties",
            "../application.properties",
            "../../application.properties",
            "../../../application.properties"};

    /***
     * 自动属性初始化
     * @param rootClass      项目起始类
     * @throws IOException
     */
    @Override
    public void init(Class<?> rootClass) throws Exception {
        if (AutoValue.rootClass != null) {
            return;
        }
        AutoValue.rootClass = rootClass;
        String appPath = PackageUtil.getPath(rootClass);
        for (String path : filePath) {
            File file = new File(appPath + File.separator + path);
            if (file.exists()) {
                propertiesFactory = new PropertiesFactory(file);
                System.out.println("AutoValue 默认配置文件:" + file.getPath());
                break;
            }
        }
        if (propertiesFactory == null) {
            System.out.println("AutoValue 没有找到配置文件:" + PackageUtil.getPath(rootClass));
        }
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
                Annotation classAnnotation = objClass.getAnnotation(Configuration.class);
                if (classAnnotation == null) {
                    continue;
                }
                inject(objClass);
            }
        }
    }


    @Override
    public Object inject(Class<?> objClass) throws Exception {
        Object bean = ObjectPool.putObject(objClass);
        autoVariable(bean);
        return bean;
    }

    /**
     * 自动 获取变量名
     *
     * @param bean
     */
    public void autoVariable(Object bean) throws Exception {
        PropertiesFactory properties = propertiesFactory;
        String classPath = PackageUtil.getPath(rootClass);

        // 从类注解Configuration中获取值,判断是否是自定义的配置文件路径
        PropertySource anno = bean.getClass().getAnnotation(PropertySource.class);
        if (anno != null) {
            String propertiePath = anno.value();
            String propertieCode = anno.encoding();
            if (!propertiePath.trim().isEmpty()) {
                // 如果文件路径是以 . 开头的,那么认为这是相对路径
                if (propertiePath.startsWith(".")) {
                    properties = new PropertiesFactory(new File(classPath + File.separator + propertiePath),
                            propertieCode);
                } else {
                    properties = new PropertiesFactory(new File(propertiePath), propertieCode);
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
    public void autoVariable(Object bean, String directory, String fileName) throws Exception {
        // 如果文件路径是以 . 开头的,那么认为这是相对路径
        PropertiesFactory properties =
                new PropertiesFactory(new File(directory + File.separator + fileName));

        autoVariable(bean, properties);
    }


    /**
     * 从指定 Properties 中获取配置
     *
     * @param bean
     * @param properties
     * @throws IllegalAccessException
     * @throws IOException
     */
    public void autoVariable(Object bean, PropertiesFactory properties) throws Exception {
        // 获取f对象对应类中的所有属性域
        Field[] fields = bean.getClass().getDeclaredFields();

        // 遍历属性
        for (Field field : fields) {
            // 过滤 final 元素
            if ((field.getModifiers() & 16) != 0) {
                continue;
            }

            // 获取Properties的key
            String valuenameKey = getKey(bean, field);
            if(valuenameKey == null){
                continue;
            }

            // 获取原来的访问控制权限
            boolean accessFlag = field.isAccessible();
            // 修改访问控制权限
            field.setAccessible(true);

            // 属性值
            Object value = getPropertiesValue(valuenameKey, properties, bean, field);

            if (value != null) {
                field.set(bean, value);
            }

            // 恢复访问控制权限
            field.setAccessible(accessFlag);
        }
    }


    /**
     * 获取Properties的key
     * @param bean    bean类
     * @param field   元素值
     * @return
     */
    private String getKey(Object bean, Field field){
        Value valuename = field.getAnnotation(Value.class);
        ConfigurationProperties prefix = bean.getClass().getAnnotation(ConfigurationProperties.class);

        String keyBase = "";
        String keyName = "";
        if(prefix != null && !prefix.prefix().isEmpty()){
            keyBase = prefix.prefix() + ".";
        }

        if(valuename != null){
            keyName = valuename.value().trim();
        }
        else if(!keyBase.isEmpty()){
            keyName = field.getName();
        }

        String key = keyBase + keyName;
        if(key.isEmpty()){
            return null;
        }
        else{
            return key;
        }

    }

    /**
     * 是否忽略忽略未知的 配置元素key
     * @param bean
     * @return
     */
    private boolean isignoreUnknownFields(Object bean){
        ConfigurationProperties prefix = bean.getClass().getAnnotation(ConfigurationProperties.class);

        if (prefix == null){
            return false;
        }
        return prefix.ignoreUnknownFields();
    }


    /**
     * 获取属性值
     * @param key
     * @param properties
     * @param bean
     * @param field
     * @return
     * @throws Exception
     */
    private Object getPropertiesValue(String key, PropertiesFactory properties,
                                      Object bean, Field field) throws Exception{
        // 属性值
        Object value = null;
        String fieldType = field.getGenericType().toString();

        if(properties.isEmpty(key)){
            // 忽略没填写的key的元素
            if(isignoreUnknownFields(bean)){
                return null;
            }
            throw new Exception("元素的key不存在:" + key + "\r\n路径:" + properties.getPropertiesFile().getPath());
        }

        value = ConvertTool.convert(properties.getStr(key), field.getType());

        return value;
    }


    /**
     * 获取启动类的ComponentScan注解路径
     *
     * @param objClass 要扫描的包路径
     * @throws Exception
     */
    @Override
    public String getScanPackageName(Class<?> objClass) {
        ComponentScan annotation = objClass.getAnnotation(ComponentScan.class);
        if (annotation == null) {
            return null;
        }
        return annotation.value();
    }


}

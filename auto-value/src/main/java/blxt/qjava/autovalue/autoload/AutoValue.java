/***********************************************************************************************************************
 *   通过注释 + 反射,实现配置文件自动加载
 *   -- 功能
 *   实现int,string,boolean,short,float,double类型的参数自动获取
 *   -- 技术点
 *   类注释获取
 *   变量注释获取
 *   通过反射设置变量值
 **********************************************************************************************************************/
package blxt.qjava.autovalue.autoload;


import blxt.qjava.autovalue.inter.*;
import blxt.qjava.autovalue.util.ConvertTool;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.reflect.PackageUtil;
import blxt.qjava.properties.PropertiesFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * 自动装载属性
 *
 * @Author: Zhang.Jialei
 * @Date: 2020/9/28 17:26
 */
//@AutoLoadFactory(name="AutoValue", annotation = Configuration.class, priority = 1)
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
    private final static String propertiesFileScanPath[] = new String[]{
            "./resources/config/application.properties",
            "./config/application.properties",
            "./resources/application.properties",
            "./application.properties",
            "../application.properties",
            "../../application.properties",
            "../../../application.properties"};


    /**
     * 设置默认配置文件.这里直接指定InputStream,方便android等不同文件系统的向使用
     * @param inputStream  文件流
     * @param codeing      文件编码
     * @return
     */
    public static boolean setPropertiesFile(InputStream inputStream,  String codeing){
        try {
            propertiesFactory = new PropertiesFactory(inputStream, codeing);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /***
     * 自动属性初始化
     * @param rootClass      项目起始类
     * @throws IOException
     */
    @Override
    public void init(Class<?> rootClass) {
        if (AutoValue.rootClass != null) {
            return;
        }
        AutoValue.rootClass = rootClass;

        // 扫描默认的配置文件
        if(propertiesFactory == null){
            String appPath = PackageUtil.getPath(rootClass);
            propertiesFactory = scanPropertiesFile(appPath);
        }
        if (propertiesFactory == null) {
            System.out.println("AutoValue 没有找到配置文件:" + PackageUtil.getPath(rootClass));
        }
        else{
            System.out.println("AutoValue 默认配置文件:" + propertiesFactory.getPropertiesFile().getAbsolutePath());
        }
    }

    /**
     * 扫描配置文件
     * @param rootPath 项目根路径
     * @return
     */
    public static PropertiesFactory scanPropertiesFile(String rootPath){
        for (String path : propertiesFileScanPath) {
            File file = new File(rootPath + File.separator + path);
            if (file.exists()) {
                try {
                    propertiesFactory = new PropertiesFactory(file);
                    return propertiesFactory;
                } catch (IOException e) {
                    continue;
                }
            }
        }
        return null;
    }

    @Override
    public Object inject(Class<?> objClass) throws Exception {
        Object bean = ObjectPool.putObject(objClass);
        autoVariable(bean, false);
        return bean;
    }

    /**
     * 自动 获取变量名
     * @param bean
     */
    @Deprecated
    public void autoVariable(Object bean)
    {
        autoVariable(bean, false);
    }

    /**
     * 自动 获取变量名
     * @param bean
     * @param fal  是否添加到对象池
     */
    public void autoVariable(Object bean, boolean fal) {
        PropertiesFactory properties = propertiesFactory;

        // 从类注解Configuration中获取值,判断是否是自定义的配置文件路径
        PropertySource anno = bean.getClass().getAnnotation(PropertySource.class);
        if (anno != null) {
            properties = getProperties(anno.value(), anno.encoding());
        }

        autoVariable(bean, properties, fal);
    }

    /**
     * 自动 获取变量名f
     *
     * @param bean
     * @param directory 目录
     * @param fileName  文件名
     * @throws IllegalAccessException
     * @throws IOException
     */
    @Deprecated
    public void autoVariable(Object bean, String directory, String fileName) throws Exception {
        autoVariable(bean, directory, fileName , false);
    }

    /**
     *
     * @param bean
     * @param directory 目录
     * @param fileName  文件名
     * @param fal       是否塞入对象池
     * @throws Exception
     */
    public void autoVariable(Object bean, String directory, String fileName, boolean fal) throws Exception {
        // 如果文件路径是以 . 开头的,那么认为这是相对路径
        PropertiesFactory properties =
                new PropertiesFactory(new File(directory + File.separator + fileName));
        autoVariable(bean, properties, fal);
    }

    /**
     * 从指定 Properties 中获取配置
     * @param bean
     * @param properties
     */
    public void autoVariable(Object bean, PropertiesFactory properties){
        autoVariable(bean, properties, false);
    }

    /**
     * 从指定 Properties 中获取配置
     * @param bean
     * @param properties
     * @param fal         是否添加进对象池
     */
    @Deprecated
    public void autoVariable(Object bean, PropertiesFactory properties, boolean fal) {
        if (fal){
            ObjectPool.putObject(bean.getClass(), bean);
        }

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
                try {
                    field.set(bean, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            // 恢复访问控制权限
            field.setAccessible(accessFlag);
        }
    }



    /**
     * 获取Properties的key
     * 由@Value注解和Field名字组成, 自动替换 _ 成 .
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
        else{ // 将_替换成.
           // return key.replaceAll("_", ".");
            return key.trim();
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
                                      Object bean, Field field){
        // 属性值
        Object value = null;
        String fieldType = field.getGenericType().toString();

        if(properties.isEmpty(key)){
            // 忽略没填写的key的元素
            if(isignoreUnknownFields(bean)){
                return null;
            }
            System.err.println("元素的key不存在:" + key + "\r\n路径:" + properties.getPropertiesFile().getAbsolutePath());
        }

        if(field.getType().isArray()){
            value = ConvertTool.convertArry(properties.getStr(key), field.getType());
        }else{
            value = ConvertTool.convert(properties.getStr(key), field.getType());
        }

        return value;
    }

    /**
     * 获取默认参数
     * @param key
     * @param valueType
     * @return
     */
    public static Object getPropertiesValue(String key, Class<?> valueType){
        // 属性值
        Object value = null;
        if(valueType.isArray()){
            value = ConvertTool.convertArry(propertiesFactory.getStr(key), valueType);
        }else{
            value = ConvertTool.convert(propertiesFactory.getStr(key), valueType);
        }
        return value;
    }

    /**
     * 获取配置文件工厂
     * @param propertiePath
     * @param propertieCode
     * @return
     */
    public static PropertiesFactory getProperties(String propertiePath, String propertieCode){

        if (propertiePath.trim().isEmpty()) {
            return propertiesFactory;
        }
        PropertiesFactory properties = propertiesFactory;
        String classPath = PackageUtil.getPath(rootClass);

        // 如果文件路径是以 . 开头的,那么认为这是相对路径
        File file;
        if (propertiePath.startsWith(".")) {
            file = new File(classPath + File.separator + propertiePath);
        } else {
            file = new File(propertiePath);
        }
        try {
            properties = new PropertiesFactory(file, propertieCode);
        }catch (Exception e){
            System.err.println("配置文件找不到:" + file.getAbsolutePath());
        }

        return properties;
    }

    /**
     * 检查某个值是否存在
     * @param key
     * @return
     */
    public static boolean isNull(String key){
        return propertiesFactory.isEmpty(key);
    }
}

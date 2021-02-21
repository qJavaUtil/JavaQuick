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


import blxt.qjava.autovalue.inter.Configuration;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ConvertTool;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.reflect.PackageUtil;
import blxt.qjava.autovalue.util.ValueFactory;
import blxt.qjava.properties.PropertiesFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 自动装载属性
 *
 * @Author: Zhang.Jialei
 * @Date: 2020/9/28 17:26
 */
@AutoLoadFactory(name="AutoValue", annotation = Configuration.class, priority = 1)
public class AutoValue extends AutoLoadBase {
    /**
     * 项目起始类
     */
    static Class<?> rootClass;
    /**
     * 默认的配置读取工具
     */
    public static PropertiesFactory propertiesFactory;

    /**
     * 默认配置文件
     */
    private final static String propertiesFileScanPath[] = new String[]{
            "./resources/config/application.properties",
            "./config/application.properties",
            "./resources/application.properties",
            "config/application.properties",
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
        ValueFactory value = new ValueFactory(propertiesFactory);
        value.setFalSetAccessible(falSetAccessible);
        value.setWorkPath(PackageUtil.getPath(rootClass));
        value.autoVariable(objClass, true);
        Object bean = ObjectPool.putObject(objClass);
        return bean;
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
     * 检查某个值是否存在
     * @param key
     * @return
     */
    public static boolean isNull(String key){
        return propertiesFactory.isEmpty(key);
    }

}

package blxt.qjava.autovalue.util;

import blxt.qjava.autovalue.autoload.AutoValue;
import blxt.qjava.autovalue.inter.ConfigurationProperties;
import blxt.qjava.autovalue.inter.PropertySource;
import blxt.qjava.properties.PropertiesFactory;
import blxt.qjava.utils.Converter;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;


/**
 * 反射配置文件值
 */
@Data
public class ValueFactory {

    PropertiesFactory propertiesFactory = AutoValue.propertiesFactory;
    boolean falSetAccessible = true;
    String workPath = "";

    public ValueFactory(){

    }

    public ValueFactory(PropertiesFactory propertiesFactory){
        this.propertiesFactory = propertiesFactory;
    }


    /**
     * 自动 获取变量名
     * @param classzs
     */
    public  <T extends Object> T  autoVariable(Class<?> classzs) throws Exception {
        PropertiesFactory properties = propertiesFactory;

        // 从类注解Configuration中获取值,判断是否是自定义的配置文件路径
        PropertySource anno = classzs.getAnnotation(PropertySource.class);
        if (anno != null) {
            properties = getProperties(anno.value(), anno.encoding());
        }

       return autoVariable(classzs, properties);
    }

    /**
     * 从指定 Properties 中获取配置
     * @param classzs
     * @param properties
     */
    public  <T extends Object> T  autoVariable(Class<?> classzs, PropertiesFactory properties) {
        return autoVariable(classzs, properties, true);
    }

    /**
     * 从指定 Properties 中获取配置
     * @param classzs
     * @param properties
     * @param fal         是否添加进对象池
     */
    @Deprecated
    public <T> T autoVariable(Class<?> classzs, PropertiesFactory properties, boolean fal) {

        T bean = null;
        if (fal){
            bean = ObjectPool.putObject(classzs);
        }
        else{
            try {
                bean = (T)classzs.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // 获取f对象对应类中的所有属性域
        Field[] fields = classzs.getDeclaredFields();
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

            // 属性值
            Object value = getPropertiesValue(valuenameKey, properties, bean, field);
            if (value != null) {
                ObjectValue.setObjectValue(bean,field, value, falSetAccessible);
            }
        }

        return bean;
    }



    /**
     * 获取Properties的key
     * 由@Value注解和Field名字组成, 自动替换 _ 成 .
     * @param bean    bean类
     * @param field   元素值
     * @return
     */
    private String getKey(Object bean, Field field){
        blxt.qjava.autovalue.inter.Value valuename = field.getAnnotation(blxt.qjava.autovalue.inter.Value.class);
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
     * 从properties中获取参数
     * @param key
     * @param valueType
     * @return
     */
    public Object getPropertiesValue(String key, Class<?> valueType) {
        // 属性值
        if(valueType.isArray()){
             return Converter.toObjects(propertiesFactory.getStr(key), valueType);
        }
        else{
            return Converter.toObject(propertiesFactory.getStr(key), valueType);
        }
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
        if(properties.isEmpty(key)){
            // 忽略没填写的key的元素
            if(isignoreUnknownFields(bean)){
                return null;
            }
            System.err.println("元素的key不存在:" + key + "\r\n路径:" + properties.getPropertiesFile().getAbsolutePath());
        }

        if (field.getType().equals(List.class)) {
            ParameterizedType pt = (ParameterizedType) field.getGenericType();
            try {
                return Converter.toObjectList(propertiesFactory.getStr(key), pt);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        else{
            return getPropertiesValue(key,  field.getType());
        }

    }


    /**
     * 检查某个值是否存在
     * @param key
     * @return
     */
    public boolean isNull(String key){
        return propertiesFactory.isEmpty(key);
    }

    /**
     * 获取配置文件工厂
     * @param propertiePath
     * @param propertieCode
     * @return
     */
    public PropertiesFactory getProperties(String propertiePath, String propertieCode){

        if (propertiePath.trim().isEmpty()) {
            return propertiesFactory;
        }
        PropertiesFactory properties = propertiesFactory;

        // 如果文件路径是以 . 开头的,那么认为这是相对路径
        File file;
        if (propertiePath.startsWith(".")) {
            file = new File(workPath + File.separator + propertiePath);
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

    public ValueFactory setPropertiesFactory(PropertiesFactory propertiesFactory) {
        this.propertiesFactory = propertiesFactory;
        return this;
    }


    public ValueFactory setPropertiesFactory(InputStream inputStream, String codeing){
        try {
            propertiesFactory = new PropertiesFactory(inputStream, codeing);
        } catch (IOException e) {
            return null;
        }
        return this;
    }


    public ValueFactory setFalSetAccessible(boolean falSetAccessible) {
        this.falSetAccessible = falSetAccessible;
        return this;
    }

    public ValueFactory setWorkPath(String workPath) {
        this.workPath = workPath;
        return this;
    }
}

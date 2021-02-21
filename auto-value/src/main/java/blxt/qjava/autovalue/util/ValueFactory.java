package blxt.qjava.autovalue.util;

import blxt.qjava.autovalue.autoload.AutoValue;
import blxt.qjava.autovalue.inter.ConfigurationProperties;
import blxt.qjava.autovalue.inter.PropertySource;
import blxt.qjava.properties.PropertiesFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;


/**
 * 反射配置文件值
 */
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
    @Deprecated
    public void autoVariable(Class<?> classzs) throws Exception {
        autoVariable(classzs, false);
    }

    /**
     * 自动 获取变量名
     * @param classzs
     * @param fal  是否添加到对象池
     */
    public void autoVariable(Class<?> classzs, boolean fal) throws Exception {
        PropertiesFactory properties = propertiesFactory;

        // 从类注解Configuration中获取值,判断是否是自定义的配置文件路径
        PropertySource anno = classzs.getAnnotation(PropertySource.class);
        if (anno != null) {
            properties = getProperties(anno.value(), anno.encoding());
        }

        autoVariable(classzs, properties, fal);
    }

    /**
     * 自动 获取变量名f
     *
     * @param classzs
     * @param directory 目录
     * @param fileName  文件名
     * @throws IllegalAccessException
     * @throws Exception
     */
    @Deprecated
    public void autoVariable(Class<?> classzs, String directory, String fileName) throws Exception {
        autoVariable(classzs, directory, fileName , false);
    }

    /**
     *
     * @param classzs
     * @param directory 目录
     * @param fileName  文件名
     * @param fal       是否塞入对象池
     * @throws Exception
     */
    public void autoVariable(Class<?> classzs, String directory, String fileName, boolean fal) throws Exception {
        // 如果文件路径是以 . 开头的,那么认为这是相对路径
        PropertiesFactory properties =
                new PropertiesFactory(new File(directory + File.separator + fileName));
        autoVariable(classzs, properties, fal);
    }

    /**
     * 从指定 Properties 中获取配置
     * @param classzs
     * @param properties
     */
    public void autoVariable(Class<?> classzs, PropertiesFactory properties) {
        autoVariable(classzs, properties, false);
    }

    /**
     * 从指定 Properties 中获取配置
     * @param classzs
     * @param properties
     * @param fal         是否添加进对象池
     */
    @Deprecated
    public void autoVariable(Class<?> classzs, PropertiesFactory properties, boolean fal) {

        Object bean = null;
        if (fal){
            bean = ObjectPool.putObject(classzs);
        }
        else{
            try {
                bean = classzs.newInstance();
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
     * 获取默认参数
     * @param key
     * @param valueType
     * @return
     */
    public Object getPropertiesValue(String key, Class<?> valueType){
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

    public PropertiesFactory getPropertiesFactory() {
        return propertiesFactory;
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


    public boolean isFalSetAccessible() {
        return falSetAccessible;
    }

    public ValueFactory setFalSetAccessible(boolean falSetAccessible) {
        this.falSetAccessible = falSetAccessible;
        return this;
    }

    public String getWorkPath() {
        return workPath;
    }

    public ValueFactory setWorkPath(String workPath) {
        this.workPath = workPath;
        return this;
    }
}

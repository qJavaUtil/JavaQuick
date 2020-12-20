package blxt.qjava.properties;

import java.io.*;
import java.util.Properties;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/28 20:09
 */
public class PropertiesFactory extends Properties {

    File propertiesFile;

    public PropertiesFactory(File file) throws IOException {
        this(file, "utf-8");
    }

    public PropertiesFactory(File file, String codeing) throws IOException {
        this(new FileInputStream(file), codeing);
        propertiesFile = file;
    }

    public PropertiesFactory(InputStream inputStream, String codeing) throws IOException {
        checkNotNull(inputStream, "包含属性的文件不能为空");
        InputStreamReader reader = new InputStreamReader(inputStream, codeing);
        load(reader);

        if(propertiesFile == null){
            propertiesFile = new File("InputStream输入,路径未知");
        }
    }

    public File getPropertiesFile() {
        return propertiesFile;
    }

    public String getStr(final String key) {
        final String value = getProperty(key);
        return value;
    }

    public Integer getInt(final String key) {
        checkNotNull(key, "获取属性的键不能为null");
        final String value = getProperty(key);
        return Integer.parseInt(value);
    }

    public Boolean getBoolean(final String key) {
        checkNotNull(key, "获取属性的键不能为null");
        final String value = getProperty(key);
        return "true".equals(value) || "TRUE".equals(value);
    }

    public Float getFloat(final String key) {
        checkNotNull(key, "获取属性的键不能为null");
        final String value = getProperty(key);
        return Float.parseFloat(value);
    }

    public Double getDouble(final String key) {
        checkNotNull(key, "获取属性的键不能为null");
        final String value = getProperty(key);
        return Double.parseDouble(value);
    }

    public Short getShort(final String key) {
        checkNotNull(key, "获取属性的键不能为null");
        final String value = getProperty(key);
        return Short.parseShort(value);
    }

    public Long getLong(final String key){
        checkNotNull(key, "获取属性的键不能为null");
        final String value = getProperty(key);
        return Long.parseLong(value);
    }

    @Override
    public String getProperty(String var1) {
        Object var2 = super.get(var1);
        String var3 = var2 instanceof String ? (String)var2 : null;
        var3 = var3 == null && this.defaults != null ? this.defaults.getProperty(var1) : var3;
        checkNotNull(var3, "元素的值不能是null:" + var1);
        return var3;
    }

    /**
     * 判断某个键值是否存在
     * @param key
     * @return
     */
    public boolean isEmpty(String key){
        Object var = super.get(key);
        if(var == null){
            return true;
        }
        return false;
    }

    /**
     * 空值检查
     *
     * @param reference
     * @param errorMessage
     * @param <T>
     * @return
     */
    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }

}

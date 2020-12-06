/*
 * Copyright 2018 dc-square GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package blxt.qjava.properties;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


/**
 * 配置文件读取工具
 *
 * @author Michael Walter
 */
public abstract class PropertiesReader {

    private final File configFilePath;
    Properties properties;

    /**
     * 配置文件目录
     * @param configFilePath
     */
    public PropertiesReader(final File configFilePath) {
        checkNotNull(configFilePath, "配置文件的路径不能为空");
        this.configFilePath = configFilePath;
    }

    /**
     * 从文件读取配置.
     * configFilePath + File.separator + getFilename()
     * @return <b>true</b> if properties are loaded, else <b>false</b>.
     */
    public boolean readPropertiesFromFile() {

        final File file = new File(configFilePath + File.separator + getFilename());

        try {
            loadProperties(file);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * 从中获取具有给定密钥的属性 {@link Properties}.
     *
     * @param key 要查找的属性的名称.
     * @return 值的属性（如果存在）, <b>null</b> if key or {@link Properties} doesn't exist or the value is an empty string.
     */
    public String getProperty(final String key) {
        checkNotNull(key, "获取属性的键不能为null.");

        if (properties == null) {
            return null;
        }

        final String property = properties.getProperty(key);

        if (property == null || property.isEmpty()) {
            return null;
        }

        return property;
    }

    /**
     * 从配置加载属性 {@link File} into {@link Properties}.
     *
     * @param file {@link File} where to load the properties from.
     * @throws IOException If properties could not be read from <b>file</b>.
     */
    private void loadProperties(final File file) throws IOException {
        checkNotNull(file, "包含属性的文件不能为空");

        try (final FileReader in = new FileReader(file)) {
            properties = new Properties();
            properties.load(in);
        }
    }

    /**
     * 检查是否存在强制属性.
     *
     * @param property 要检查的属性.
     * @return 属性是否勋在,0存在, 1不存在
     */
    public int checkMandatoryProperty(final String property) {
        checkNotNull(property, "强制属性不能为空");

        final String value = getProperty(property);

        if (value == null) {
            return 1;
        }
        return 0;
    }


    /**
     * 获取String属性
     *
     * @param key          属性值.
     * @param defaultValue 默认值.
     * @return 属性的值,或者默认值.
     */
    public String validateStringProperty(final String key, final String defaultValue) {
        checkNotNull(key, "获取属性的键不能为null");
        checkNotNull(defaultValue, "属性的默认值不能为null");

        final String value = getProperty(key);

        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    /**
     * 获取int属性
     * @param key             key
     * @param defaultValue    默认值
     * @param zeroAllowed     是否允许值为0
     * @param negativeAllowed 知否允许值为负数
     * @return 属性值,或者默认值
     */
    public Integer validateIntProperty(final String key, final int defaultValue, final boolean zeroAllowed, final boolean negativeAllowed) {
        checkNotNull(key, "获取属性的键不能为null");

        final String value = properties.getProperty(key);

        if (value == null) {
            return new Integer(defaultValue);
        }

        Integer valueAsInt;

        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }

        // 值不能为零。使用默认值
        if (!zeroAllowed && valueAsInt == 0) {
            return null;
        }

        // 值不能为负。使用默认值
        if (!negativeAllowed && valueAsInt < 0) {
            return null;
        }

        return valueAsInt;
    }

    public String getStr(final String key){
        final String value = properties.getProperty(key);
        return value;
    }

    public Integer getInt(final String key){
        checkNotNull(key, "获取属性的键不能为null");
        final String value = properties.getProperty(key);
        return Integer.parseInt(value);
    }

    public Boolean getBoolean(final String key){
        checkNotNull(key, "获取属性的键不能为null");
        final String value = properties.getProperty(key);
        return "true".equals(value) || "TRUE".equals(value);
    }

    public Float getFloat(final String key){
        checkNotNull(key, "获取属性的键不能为null");
        final String value = properties.getProperty(key);

        return Float.parseFloat(value);
    }

    public Double getDouble(final String key){
        checkNotNull(key, "获取属性的键不能为null");
        final String value = properties.getProperty(key);
        return Double.parseDouble(value);
    }

    public Short getShort(final String key){
        checkNotNull(key, "获取属性的键不能为null");
        final String value = properties.getProperty(key);
        return Short.parseShort(value);
    }

    /**
     * 空值检查
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

    /**
     * 获取配置文件名
     * @return
     */
    public abstract String getFilename();


}
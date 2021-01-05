package com.qjava.qsql.utils;

import blxt.qjava.properties.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;


/**
 * 读取包含redis配置的属性文件，并提供一些使用{@link Properties}的实用方法.
 * @author 张家磊
 */
public class RedisConfiguration extends PropertiesReader {

    private static final Logger log = LoggerFactory.getLogger(RedisConfiguration.class);

    private static final String HOST =          "redis.host";
    private static final String PORT =          "redis.port";
    private static final String PASSWORD =      "redis.password";
    private static final String BLOCK =         "redis.block";
    private static final String EXPXTIME =      "redis.expxtime";
    private static final String MAX_TOTAL =     "redis.pool.max.total";
    private static final String MAX_IDLE =      "redis.pool.max.idle";
    private static final String MAX_WAIT =      "redis.pool.max.wait";
    private static final String MIN_IDLE =      "redis.pool.min.idle";
    private static final String REDIS_TIMEOUT = "redis.timeout";
    /** 在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。 */
    private static final String ONBORROW =      "redis.test.onborrow";
    /** 在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。 */
    private static final String ONRETURN =      "redis.test.onreturn";


    public RedisConfiguration(File configFilePath) {
        super(configFilePath);
    }


    @Override
    public String getFilename() {
        return "redis.properties";
    }

    /**
     * 检查配置缺失
     * @return
     */
    public boolean check(){

        if (!readPropertiesFromFile()) {
            log.error("无法读取属性");
            return false;
        }

        if (!validateConfiguration()) {
            log.error("未设置至少一个强制性属性");
            return false;
        }
        return true;
    }


    /**
     * 检查强制属性是否存在并且有效。强制属性是端口和主机.
     *
     */
    public boolean validateConfiguration() {
        int countError = 0;

        countError += checkMandatoryProperty(HOST);
        countError += checkMandatoryProperty(PORT);
        countError += checkMandatoryProperty(EXPXTIME);

        if (countError != 0){
            return false;
        }

        // check for valid port value
        final String port = getProperty(PORT);
        try {
            final int intPort = Integer.parseInt(port);

            if (intPort < 0 || intPort > 65535) {
                log.error("强制性属性{}的值不在有效的端口范围内.", PORT);
                countError++;
            }

        } catch (NumberFormatException e) {
            log.error("强制性属性{}的值不是数字.", PORT);
            countError++;
        }

        // check if host is still --INFLUX-DB-IP--
        final String host = getProperty(HOST);

        if (host.equals("--INFLUX-DB-IP--")) {
            countError++;
        }

        return countError == 0;
    }



    public String getHost() {
        return  validateStringProperty(HOST, "localhost");
    }

    public Integer getPort() {
        final Integer port;

        try {
            port = Integer.parseInt(getProperty(PORT));
        } catch (NumberFormatException e) {
            log.error("{}的值不是数字", PORT);
            return null;
        }

        return port;
    }

    public Integer getBlock() {
        final Integer port;

        try {
            port = Integer.parseInt(getProperty(BLOCK));
        } catch (NumberFormatException e) {
            log.error("{}的值不是数字", BLOCK);
            return null;
        }

        return port;
    }

    public Integer getExpxtime(){
        final Integer port;

        try {
            port = Integer.parseInt(getProperty(EXPXTIME));
        } catch (NumberFormatException e) {
            log.error("{}的值不是数字", EXPXTIME);
            return null;
        }

        return port;
    }

    public String getPassword() {
        return  validateStringProperty(PASSWORD, "");
    }

    public boolean getOnborrow() {
        return validateStringProperty(ONBORROW, "true").equals("true");
    }

    public boolean getOnreturn() {
        return validateStringProperty(ONRETURN, "true").equals("true");
    }

    public Integer getMaxActive() {

        return validateIntProperty(MAX_TOTAL, -1, true, true);
    }

    public int getMaxIdle() {
        return validateIntProperty(MAX_IDLE, 100, true, true);
    }

    public int getMaxWait() {
        return validateIntProperty(MAX_WAIT, -1, true, true);
    }

    public int getMinIdle() {
        return validateIntProperty(MIN_IDLE, 0, true, true);
    }

    public int getRedis_timeout() {
        return validateIntProperty(REDIS_TIMEOUT, 0, true, true);
    }

}

package com.qjava.qsql.redis;


import blxt.qjava.autovalue.inter.Component;
import blxt.qjava.autovalue.inter.Configuration;
import blxt.qjava.autovalue.inter.PropertySource;
import blxt.qjava.autovalue.inter.Value;

@Component
@Configuration
@PropertySource(ignoreResourceNotFound=true)
public class ResisBean {

    /** 主机ip */
    @Value("redis.host")
    public String redisIp;
    /** 端口 */
    @Value("redis.port")
    public Integer redisPort;
    /** 密码 */
    @Value("redis.password")
    public String pwd;
    /** 连接超时（毫秒） */
    @Value("redis.timeout")
    public int redisTimeout = 3000;
    /** 默认数据库 */
    @Value("redis.block")
    public int block = 0;
    public int expxtime = 3000;

    @Value("redis.pool.max.total")
    public int maxTotal = 10000;
    @Value("redis.pool.max.idle")
    public int maxIdle = 100;
    @Value("redis.pool.max.wait")
    public int maxWait = 10;
    @Value("redis.pool.min.idle")
    public int minIdle = 1;
    @Value("redis.test.onborrow")
    public boolean testOnBorrow = true;
    @Value("redis.test.onreturn")
    public boolean testOnReturn = true;

    public ResisBean() {

    }

    public ResisBean(String redisIp, Integer redisPort, String pwd, int redisTimeout, int block) {
        this.redisIp = redisIp;
        this.redisPort = redisPort;
        this.pwd = pwd;
        this.redisTimeout = redisTimeout;
        this.block = block;
    }
}

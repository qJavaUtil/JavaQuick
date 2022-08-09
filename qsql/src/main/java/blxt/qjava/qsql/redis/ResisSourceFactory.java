package blxt.qjava.qsql.redis;

import blxt.qjava.qsql.utils.RedisConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.util.List;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/12 16:43
 */
public class ResisSourceFactory {

    /** jedis连接池 */
    public static RedisPool redisPool;

    static ResisBean resisBean = null;

    public static  boolean newInstance(File file){
        if (redisPool != null){
            return true;
        }
        System.out.println("redis连接池配置:" + file.getAbsolutePath());
        RedisConfiguration properties = new RedisConfiguration(file);
        if (!properties.check()){
            System.out.println("redis连接池配置文件错误," +  file.getPath() + File.separator + properties.getFilename());
        }

        // Srouce工厂模式
        redisPool = createDataSource(properties);

        System.out.println("redis连接池配置成功");
        return true;
    }



    public static RedisPool createDataSource(RedisConfiguration properties){

        ResisBean resisBean = new ResisBean(properties.getHost(), properties.getPort(),
                properties.getPassword(), properties.getRedis_timeout(),properties.getBlock() );

        /** 默认key有效时间 */
        resisBean.expxtime = properties.getExpxtime();
        /** 最大连接数 */
        resisBean.maxTotal = properties.getMaxActive();
        resisBean.maxIdle = properties.getMaxIdle();
        resisBean.maxWait = properties.getMaxWait();
        resisBean.minIdle = properties.getMinIdle();
        resisBean.testOnBorrow = properties.getOnborrow();
        resisBean.testOnReturn = properties.getOnreturn();

        return createDataSource(resisBean);
    }


    /**
     *
     * @return
     */
    public static RedisPool createDataSource(ResisBean resisBean){
        ResisSourceFactory.resisBean = resisBean;

        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(resisBean.maxTotal);
        config.setMaxIdle(resisBean.maxIdle);
        config.setMinIdle(resisBean.minIdle);

        config.setTestOnBorrow(resisBean.testOnBorrow);
        config.setTestOnReturn(resisBean.testOnReturn);

        // 连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。
        config.setBlockWhenExhausted(false);

        // 集群
//        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(redisIp, redisPort);
//        jedisShardInfo1.setPassword(pwd);
//
//        List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
//        list.add(jedisShardInfo1);
//        pool = new ShardedJedisPool(config, list);

        return createDataSource(config);
    }

    public static RedisPool createDataSource(JedisPoolConfig config){
        redisPool = new RedisPool(config,
                resisBean.redisIp, resisBean.redisPort, resisBean.redisTimeout, resisBean.pwd, resisBean.block);
        return redisPool;
    }


    public static Jedis getJedis() {
        if(redisPool == null){
            return null;
        }
        return redisPool.getResource();
    }

    public static boolean returnBrokenResource(Jedis jedis) {
        if(redisPool == null){
            return false;
        }
        redisPool.returnBrokenResource(jedis);
        return true;
    }


    public static boolean returnResource(Jedis jedis) {
        if(redisPool == null){
            return false;
        }
        redisPool.returnResource(jedis);
        return true;
    }


}

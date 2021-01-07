package blxt.qjava.qsql.redis;

import redis.clients.jedis.*;


/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/12 14:22
 */
public class RedisPool extends JedisPool {

    public RedisPool(JedisPoolConfig config, String redisIp, int redisPort, int redisTimeout, String pwd, int block){
        super(config, redisIp, redisPort, redisTimeout, pwd, block);
    }

    @Override
    public void returnBrokenResource(Jedis jedis) {
        super.returnBrokenResource(jedis);
    }


    @Override
    public void returnResource(Jedis jedis) {
        super.returnResource(jedis);
    }
}



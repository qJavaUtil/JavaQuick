package com.qjava.qsql.redis;

import redis.clients.jedis.Jedis;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/12 16:27
 */
public class RedisPoolUtil {

    private ResisSourceFactory resisSourceFactory;

    /**
     * 设置key的有效期，单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null){
                return null;
            }
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
            return result;
        }
        ResisSourceFactory.returnResource(jedis);
        return result;
    }

    /**
     * 设置, exTime的单位是秒
     * @param key
     * @param value
     * @param exTime
     * @return
     */
    public static boolean setEx(String key,String value,int exTime){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null){
                return false;
            }
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
            return "OK".equals(result);
        }
        ResisSourceFactory.returnResource(jedis);
        return "OK".equals(result);
    }

    /**
     * 设置
     * @param key
     * @param value
     * @return
     */
    public static boolean set(String key,String value){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null){
                return false;
            }
            result = jedis.set(key,value);
        } catch (Exception e) {
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
            return "OK".equals(result);
        }
        ResisSourceFactory.returnResource(jedis);
        return "OK".equals(result);
    }

    /**
     * 获取字符串
     * @param key
     * @return
     */
    public static String get(String key){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null){
                return null;
            }
            result = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
            return result;
        }
        ResisSourceFactory.returnResource(jedis);
        return result;
    }

    /**
     * 设置bit
     * @return
     */
    public static boolean setBit(String key, long offset, String value){
        Jedis jedis = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null) {
                return false;
            }
            return jedis.setbit(key, offset, value);
        }catch (Exception e){
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
        }

        return false;
    }


    /**
     * 设置bit
     * @return
     */
    public static boolean setBit(String key, long offset, boolean value){
        Jedis jedis = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null) {
                return false;
            }
            return jedis.setbit(key, offset, value);
        }catch (Exception e){
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
        }
        return false;
    }

    /**
     * 获取bit
     * @return
     */
    public static boolean getBit(String key, long offset){
        Jedis jedis = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null) {
                return false;
            }
            return jedis.getbit(key, offset);
        }catch (Exception e){
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
        }

        return false;
    }

    /**
     * 获取bit
     * @return
     */
    public static boolean getBit(byte[] key, long offset){
        Jedis jedis = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null) {
                return false;
            }

            return jedis.getbit(key, offset);
        }catch (Exception e){
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
        }

        return false;
    }

    /**
     * bit计数
     * @param key
     * @return
     */
    public static long bitcount(String key){
        Jedis jedis = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null) {
                return -1;
            }
            return jedis.bitcount(key);
        }catch (Exception e){
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
        }

        return -1;
    }

    /**
     * bit计数
     * @param key
     * @return
     */
    public static long bitcount(String key, long start, long end){
        Jedis jedis = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null) {
                return -1;
            }
            return jedis.bitcount(key, start, end);
        }catch (Exception e){
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
        }

        return -1;
    }



    /**
     * 判断存在
     * @param key
     * @return
     */
    public static boolean exit(String key){
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null){
                return false;
            }
            result = jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
            return false;
        }
        ResisSourceFactory.returnResource(jedis);
        return result;
    }

    /**
     * 删除
     * @param key
     * @return
     */
    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null){
                return null;
            }
            result = jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
            return result;
        }
        ResisSourceFactory.returnResource(jedis);
        return result;
    }

    /***
     * 自增
     * @param key
     * @return
     */
    public static Long incr(String key){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null){
                return null;
            }
            result = jedis.incr(key);
        } catch (Exception e) {
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
            return null;
        }
        ResisSourceFactory.returnResource(jedis);
        return result;
    }

    /**
     * 自增
     * @param key
     * @param value
     * @return
     */
    public static Long incr(String key, long value){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null){
                return null;
            }
            result = jedis.incrBy(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
            return null;
        }
        ResisSourceFactory.returnResource(jedis);
        return result;
    }

    /**
     * 自增
     * @param key
     * @param increment
     * @return
     */
    public Double incrByFloat(String key, double increment) {
        Jedis jedis = null;
        Double result = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null){
                return null;
            }
            result = jedis.incrByFloat(key, increment);
        } catch (Exception e) {
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
            return null;
        }
        ResisSourceFactory.returnResource(jedis);
        return result;
    }

    /**
     * 自减
     * @param key
     * @return
     */
    public static Long decr(String key){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null){
                return null;
            }
            result = jedis.decr(key);
        } catch (Exception e) {
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
            return null;
        }
        ResisSourceFactory.returnResource(jedis);
        return result;
    }

    /**
     * 自减
     * @param key
     * @return
     */
    public static Long decr(byte[] key){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = ResisSourceFactory.getJedis();
            if (jedis == null){
                return null;
            }
            result = jedis.decr(key);
        } catch (Exception e) {
            e.printStackTrace();
            ResisSourceFactory.returnBrokenResource(jedis);
            return null;
        }
        ResisSourceFactory.returnResource(jedis);
        return result;
    }

}
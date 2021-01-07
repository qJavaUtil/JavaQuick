package redis;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import blxt.qjava.qsql.redis.RedisPoolUtil;
import org.junit.jupiter.api.Test;
import utils.HashTool;

public class RedisConnetionTest {

//    RedisConnetion redisConnetion = new RedisConnetion("auth.zhangjialei.cn", "redis", 8379, 0,
//            -100, 100, 0, 0, 0);

    @Test
    public void send() {
        boolean fal = false;
        HashTool hashTool = new HashTool();

        TimeInterval timer = DateUtil.timer();
        timer.start();


//        System.out.println(    ":" + "zzzzzzzz".hashCode());
//        System.out.println(    ":" + HashTool.getcode("zzzzzzzz"));

        String produc = "bit.demo";

        System.out.println("开始设置");
        for (int i = 0; i < 100; i++){
            RedisPoolUtil.setBit(produc, i, i % 2 == 0);
        }

        System.out.println("设置完成,耗时" + timer.intervalMs());
        timer.start();

        for (int i = 0; i < 100; i++){
            boolean res = RedisPoolUtil.getBit(produc, i);
            System.out.print((res ? 1 : 0) + " ");
        }

        System.out.println("\r\n读取完成,耗时" + timer.intervalMs());

        /* 设备离在线记录 **/
        String deviceId = "sn14514";
        timer.start();
        long hash = deviceId.hashCode() & Integer.MAX_VALUE;
        System.out.println(hash + ",耗时" + timer.intervalMs());

        RedisPoolUtil.setBit(produc, hash, false);

        boolean res = RedisPoolUtil.getBit(produc,hash);
        System.out.println("设备在线" + res);

        /* 设备在线统计  */
        System.out.println("设备总在线" + RedisPoolUtil.bitcount(produc));
    }
}

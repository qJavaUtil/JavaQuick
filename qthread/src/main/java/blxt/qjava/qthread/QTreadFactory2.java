package blxt.qjava.qthread;

import blxt.qjava.autovalue.inter.Value;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/8 10:23
 */
public class QTreadFactory2 {

    /** 核心线程池数量 */
    @Value("thread.corePoolSize")
    int THREAD_COREPOOLSIZE = 1;
    /** 最大线程池数量 */
    @Value("thread.maximumPoolSize")
    int THREAD_MAXIMUMPOOLSIZE = 5;
    /** 线程最大的空闲存活时间 */
    @Value("thread.keepAliveTime")
    int THREAD_KEEPALIVETIME = 60;
    /** 拒绝策略 */
    @Value("thread.rejected")
    String THREAD_REJECTED = "CallerRuns";

}

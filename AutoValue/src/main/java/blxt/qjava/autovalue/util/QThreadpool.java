package blxt.qjava.autovalue.util;

import blxt.qjava.autovalue.inter.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于并发设计的线程池
 *
 * @Author: Zhang.Jialei
 * @Date: 2020/9/17 11:04
 */
@Configuration
@Component
@ConfigurationProperties(ignoreUnknownFields = true)
public class QThreadpool {

    static QThreadpool instance;

    /** 用于指定核心线程数量 */
    @Value("thread.corePoolSize")
    public int corePoolSize=5;

    /** 指定最大线程数 */
    @Value("thread.maximumPoolSize")
    public int maximumPoolSize = 10;

    /** 指定线程空闲后的最大存活时间 */
    @Value("thread.keepAliveTime")
    public int keepAliveTime = 60;

    /** 拒绝策略 */
    @Value("thread.rejected")
    public String rejected;

    /**
     * 并发型线程池
     */
    ThreadPoolExecutor pool = null;
    /**
     * 定时线程池
     */
    ScheduledExecutorService timePool = null;

    /** 默认线程工厂 */
    public static ThreadFactory defaultThreadFactory = new DefaultThreadFactory();;


    public static QThreadpool getInstance(){
        return (QThreadpool)ObjectPool.getObject(QThreadpool.class);
    }


    public QThreadpool(){

    }

    @Run
    public void init(){
        RejectedExecutionHandler handler = null;
        if(rejected == null){
            handler = new ThreadPoolExecutor.AbortPolicy();
        }
        rejected = rejected.toUpperCase();
        if(rejected.equals("ABORTPOLICY")){
            handler = new ThreadPoolExecutor.AbortPolicy();
        } else if(rejected.equals("DISCARDOLDESTPOLICY")){
            handler = new ThreadPoolExecutor.DiscardOldestPolicy();
        } else if(rejected.equals("DiscardPolicy")){
            handler = new ThreadPoolExecutor.DiscardPolicy();
        } else if(rejected.equals("CALLERRUNSPOLICY")){
            handler = new ThreadPoolExecutor.CallerRunsPolicy();
        }
        else{
            handler = new ThreadPoolExecutor.AbortPolicy();
        }

        pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                defaultThreadFactory, handler);

        timePool = Executors.newScheduledThreadPool(corePoolSize, defaultThreadFactory);
    }

    public void execute(Runnable runnable){
        pool.execute(runnable);
    }


    /**
     * 延时任务
     * @param command
     * @param delay
     * @param unit
     * @return
     */
    public ScheduledFuture<?> schedule(Runnable command,
                                       long delay, TimeUnit unit){
       return timePool.schedule(command, delay, unit);
    }

    /**
     * 延时任务
     * @param callable
     * @param delay
     * @param unit
     * @param <V>
     * @return
     */
    public <V> ScheduledFuture<V> schedule(Callable<V> callable,
                                           long delay, TimeUnit unit){
        return timePool.schedule(callable, delay, unit);
    }

    /**
     * 循环任务，按照上一次任务的发起时间计算下一次任务的开始时间
     * @param command
     * @param initialDelay
     * @param period
     * @param unit
     * @return
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                  long initialDelay,
                                                  long period,
                                                  TimeUnit unit){
        return timePool.scheduleAtFixedRate(command, initialDelay, period, unit);
    }


    /**
     * 循环任务，以上一次任务的结束时间计算下一次任务的开始时间
     * @param command
     * @param initialDelay
     * @param delay
     * @param unit
     * @return
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     TimeUnit unit){

        return timePool.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }



    /**
     * 默认的线程工厂
     */
    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    public ThreadPoolExecutor getPool() {
        return pool;
    }

    public ScheduledExecutorService getTimePool() {
        return timePool;
    }
}

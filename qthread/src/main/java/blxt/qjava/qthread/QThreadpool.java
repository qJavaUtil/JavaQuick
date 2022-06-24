package blxt.qjava.qthread;

import blxt.qjava.qthread.utils.GeneralScheduledThreadPoolExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于并发设计的线程池
 *
 * @Author: Zhang.Jialei
 * @Date: 2020/9/17 11:04
 */
public class QThreadpool {

    static QThreadpool instance;

    /**
     * 并发型线程池
     */
    ThreadPoolExecutor pool = null;
    /**
     * 定时线程池
     */
    GeneralScheduledThreadPoolExecutor timePool = null;

    /** 默认线程工厂 */
    public static ThreadFactory defaultThreadFactory;


    static {
        defaultThreadFactory = new DefaultThreadFactory();
        try {
            QTreadFactory.creatDefaultPool();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static QThreadpool getInstance(){
        return instance;
    }

    /**
     * 并发型线程池
     * IO密集型任务  = 一般为2*CPU核心数（常出现于线程中：数据库数据交互、文件上传下载、网络数据传输等等）
     * CPU密集型任务 = 一般为CPU核心数+1（常出现于线程中：复杂算法）
     * 混合型任务  = 视机器配置和复杂度自测而定
     *
     * @param corePoolSize    用于指定核心线程数量
     * @param maximumPoolSize 指定最大线程数
     */
    public static QThreadpool newInstance(int corePoolSize,
                                          int maximumPoolSize,
                                          int keepAliveTime) {

        return newInstance(corePoolSize, maximumPoolSize, keepAliveTime,  new ThreadPoolExecutor.AbortPolicy(),false);
    }

    public static QThreadpool newInstance(int corePoolSize,
                                          int maximumPoolSize,
                                          int keepAliveTime,
                                          RejectedExecutionHandler handler) {

        return newInstance(corePoolSize, maximumPoolSize, keepAliveTime, handler,false);
    }

    /**
     *
     * @param corePoolSize
     * @param maximumPoolSize
     * @param create          是否强制新建
     */
    protected static QThreadpool newInstance(int corePoolSize, int maximumPoolSize, int keepAliveTime,
                                             RejectedExecutionHandler handler,
                                             boolean create) {

        if (instance != null  && !create) {
            return instance;
        }

        instance = new QThreadpool(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), defaultThreadFactory, handler);

        return instance;
    }


    /**
     * @param corePoolSize       用于指定核心线程数量
     * @param maximumPoolSize    指定最大线程数
     * @param keepAliveTime      指定线程空闲后的最大存活时间
     * @param unit               指定线程空闲后的最大存活时间单位, 默认秒
     * @param workQueue          是线程池的缓冲队列,还未执行的线程会在队列中等待,默认的实现是一个无界的 LinkedBlockingQueue
     *
     */
    public QThreadpool(int corePoolSize, int maximumPoolSize, int keepAliveTime,
                        TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                        RejectedExecutionHandler handler){

        if(pool == null) {
            pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        if(timePool == null) {
            timePool = new GeneralScheduledThreadPoolExecutor(corePoolSize, threadFactory);
        }

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

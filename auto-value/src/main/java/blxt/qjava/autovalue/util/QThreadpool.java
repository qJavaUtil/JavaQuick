package blxt.qjava.autovalue.util;

import blxt.qjava.autovalue.inter.*;

import java.util.HashMap;
import java.util.Map;
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

    /** 线程池工厂 */
    @Value("thread.threadfactory")
    public String threadFactoryName;

    TimeUnit unit = TimeUnit.SECONDS;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
    ThreadFactory threadFactory = new DefaultThreadFactory();
    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

    /**
     * 并发型线程池
     */
    ThreadPoolExecutor pool = null;
    /**
     * 定时线程池
     */
    ScheduledExecutorService timePool = null;

    /** 拒绝策略 */
    static Map<String, RejectedExecutionHandler> rejectedList = new HashMap<>();

    static {
        rejectedList.put("abortpolicy", new ThreadPoolExecutor.AbortPolicy()); // 丢弃任务并抛出RejectedExecutionException异常
        rejectedList.put("abort", new ThreadPoolExecutor.AbortPolicy());
        rejectedList.put("callerruns", new ThreadPoolExecutor.CallerRunsPolicy());
        rejectedList.put("callerrunspolicy", new ThreadPoolExecutor.CallerRunsPolicy()); // 由调用线程处理该任务
        rejectedList.put("discard", new ThreadPoolExecutor.DiscardPolicy());
        rejectedList.put("discardpolicy", new ThreadPoolExecutor.DiscardPolicy());  // 也是丢弃任务，但是不抛出异常
        rejectedList.put("discardoldest", new ThreadPoolExecutor.DiscardOldestPolicy()); // 丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
        rejectedList.put("discardoldestpolicy", new ThreadPoolExecutor.DiscardOldestPolicy());
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
                new LinkedBlockingQueue<Runnable>(), new DefaultThreadFactory(), handler);

        return instance;
    }


    public static QThreadpool getInstance(){
        if(ObjectPool.isEmpty(QThreadpool.class)){
            ObjectPool.putObject(QThreadpool.class);
            instance = (QThreadpool)ObjectPool.getObject(QThreadpool.class);
            instance.init();
        }
        else {
            instance = (QThreadpool) ObjectPool.getObject(QThreadpool.class);
        }
        return instance;
    }


    public QThreadpool(){
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
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.workQueue = workQueue;
        this.threadFactory = threadFactory;
        this.handler = handler;
    }

    @Run
    public void init(){
        handler = getRejectedExecutionHandler(rejected);
        threadFactory = getThreadFactory(threadFactoryName);

        pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue,  threadFactory, handler);

        timePool = Executors.newScheduledThreadPool(corePoolSize, threadFactory);

    }

    /**
     * 获取线程工厂
     * @param threadFactory
     * @return
     */
    private ThreadFactory getThreadFactory(String threadFactory){
        ThreadFactory defaultThreadFactory = new DefaultThreadFactory();

        if(threadFactory != null && !threadFactory.isEmpty()){
            try {
                Class threadFactoryClass = Class.forName(threadFactory);
                threadFactoryClass.newInstance();
            } catch (Exception e) {
                System.err.println("线程工厂加载失败,请设置入参为空的的公开构造方法:" + threadFactory);
            }
        }

        return defaultThreadFactory;
    }

    /**
     * 获取拒绝策略
     * @param rejected
     * @return
     */
    private RejectedExecutionHandler getRejectedExecutionHandler(String rejected){
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        if(rejected == null){
            return handler;
        }
        rejected = rejected.toLowerCase();

        for(Map.Entry<String, RejectedExecutionHandler> entry : rejectedList.entrySet()){
            String mapKey = entry.getKey();
            if(rejected.equals(mapKey)){
                handler = entry.getValue();
                return handler;
            }
        }

        return handler;
    }

    /** 快速执行线程,不经过线程池 */
    public Thread run(Runnable runnable){
        Thread thread =  new Thread(runnable);
        thread.start();
        return thread;
    }

    /** 执行线程 */
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


    @Override
    public String toString() {
        return "QThreadpool{" +
                "corePoolSize=" + corePoolSize +
                ", maximumPoolSize=" + maximumPoolSize +
                ", keepAliveTime=" + keepAliveTime +
                ", rejected='" + rejected + '\'' +
                ", threadFactory='" + threadFactoryName + '\'' +
                '}';
    }

    /**
     * 默认的线程工厂
     */
    public static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
            namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
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

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
    public String threadFactory;

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
        rejectedList.put("abortpolicy", new ThreadPoolExecutor.AbortPolicy());
        rejectedList.put("abort", new ThreadPoolExecutor.AbortPolicy());
        rejectedList.put("callerruns", new ThreadPoolExecutor.CallerRunsPolicy());
        rejectedList.put("CallerRunsPolicy", new ThreadPoolExecutor.CallerRunsPolicy());
        rejectedList.put("discard", new ThreadPoolExecutor.DiscardPolicy());
        rejectedList.put("discardpolicy", new ThreadPoolExecutor.DiscardPolicy());
        rejectedList.put("discardoldest", new ThreadPoolExecutor.DiscardOldestPolicy());
        rejectedList.put("discardoldestpolicy", new ThreadPoolExecutor.DiscardOldestPolicy());
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
      //  init();
    }

    @Run
    public void init(){
        RejectedExecutionHandler handler = getRejectedExecutionHandler(rejected);
        ThreadFactory defaultThreadFactory = getThreadFactory(threadFactory);

        pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                defaultThreadFactory, handler);

        timePool = Executors.newScheduledThreadPool(corePoolSize, defaultThreadFactory);

        System.out.println(toString());
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
                System.err.println("线程工厂加载失败,请设置入参为空的的公开构造方法." + threadFactory);
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
                ", threadFactory='" + threadFactory + '\'' +
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

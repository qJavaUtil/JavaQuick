package blxt.qjava.qthread.utils;

import java.util.concurrent.ScheduledFuture;

/**
 * 自定义Runnable封装, 支持停止循环任务.
 * @author OpenJialei
 */
public abstract class GeneralTask implements Runnable{

    volatile ScheduledFuture<?> scheduledFuture = null;

    public GeneralTask() {

    }

    /**
     * 执行.
     */
    public abstract void doRun();

    /**
     * 取消定时任务.
     * @param mayInterruptIfRunning 任务运行标记
     */
    public void cancel(boolean mayInterruptIfRunning) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(mayInterruptIfRunning);
        }
    }

    /**
     * 执行自定义任务.
     */
    @Override
    public void run() {
        doRun();
    }
}

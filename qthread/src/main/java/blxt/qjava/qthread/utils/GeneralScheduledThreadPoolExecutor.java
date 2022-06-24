package blxt.qjava.qthread.utils;

import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * 自定义 ScheduledThreadPoolExecutor.
 * @author OpenJialei
 */
public class GeneralScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {
    public GeneralScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    public GeneralScheduledThreadPoolExecutor(int var1, ThreadFactory var2) {
        super(var1, var2);
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
        if (runnable instanceof GeneralTask) {
            ((GeneralTask) runnable).scheduledFuture = task;
        }
        return task;
    }
}

package blxt.qjava.qthread.delayed;

import blxt.qjava.qthread.utils.GeneralTask;
import blxt.qjava.qthread.QThreadpool;

import java.util.concurrent.TimeUnit;

/**
 * 延时数据对象.
 *
 * @author OpenJialei
 * @param <T>
 */
public abstract class AbstractDelayedTask<T> extends GeneralTask {

    /**
     * 时间精度
     */
    long initialDelay;

    /**
     * 上一次操作时间.
     */
    long mLastActionTime;

    /**
     * sleepTime.
     * @param initialDelay sleepTime
     */
    public void setDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }


    /**
     * 开始倒计时.
     */
    public void start(){
        QThreadpool.getInstance()
                .scheduleWithFixedDelay(this, initialDelay, initialDelay, TimeUnit.MILLISECONDS);
    }

    /**
     * 取消定时任务.
     */
    public void cancel() {
        super.cancel(false);
    }


}

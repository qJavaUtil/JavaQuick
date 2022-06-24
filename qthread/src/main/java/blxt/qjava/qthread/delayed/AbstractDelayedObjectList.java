package blxt.qjava.qthread.delayed;

import java.util.ArrayList;
import java.util.List;

/**
 * 延时数据对象.
 *
 * @author OpenJialei
 * @param <T>
 */
public abstract class AbstractDelayedObjectList<T> extends AbstractDelayedTask<T> implements DelayedObjectList<T> {

    /**
     * 时间精度
     */
    long initialDelay;

    /**
     * 数据集合
     */
    List<T> valves = new ArrayList<>();

    /**
     * 上一次操作时间.
     */
    private long mLastActionTime;

    public AbstractDelayedObjectList() {

    }

    /**
     * 构造.
     * @param sleepTime  初始睡眠
     */
    public AbstractDelayedObjectList(long sleepTime) {
        this.initialDelay = sleepTime;
    }

    /**
     * 构造.
     * @param sleepTime  初始睡眠
     * @param size       数据队列长度
     */
    public AbstractDelayedObjectList(long sleepTime, int size) {
        valves = new ArrayList<T>(size);
        this.initialDelay = sleepTime;
    }

    /**
     * 更新数据
     * @param object 新数据对象
     */
    public synchronized void put(T object){
        valves.add(object);
        mLastActionTime = System.currentTimeMillis();
    }

    /**
     * 执行倒计时任务.
     */
    @Override
    public void doRun() {
        // 查询更新
        if (System.currentTimeMillis() - mLastActionTime > initialDelay) {
            // 倒计时结束,输出数据
            if (!valves.isEmpty()) {
                onTimeOver(valves);
                valves.clear();
            }
        }
    }

}

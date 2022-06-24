package blxt.qjava.qthread.delayed;

/**
 * 延时数据对象.
 *
 * @author OpenJialei
 * @param <T>
 */
public abstract class AbstractDelayedObject<T> extends AbstractDelayedTask<T> implements ObjectUpdate<T>, DelayedObject<T> {

   // QThreadpool qThreadpool = new QThreadpool();

    /**
     * 上次数据缓存
     */
    T valve;

    public AbstractDelayedObject() {

    }

    /**
     * 构造
     * @param sleepTime  初始睡眠
     */
    public AbstractDelayedObject(long sleepTime) {
        this.initialDelay = sleepTime;
    }

    /**
     * 设置默认值
     * @param valve valve
     */
    public void setValue(T valve){
        this.valve = valve;
    }


    /**
     * 更新数据.
     * @param object 新数据对象
     */
    public synchronized void put(final T object){
        put(object, this);
    }

    /**
     * 更新数据.
     * @param object       新数据对象
     * @param objectUpdate 自定义数据更新处理方法
     */
    public synchronized void put(final T object, final ObjectUpdate<T> objectUpdate){
        this.valve = objectUpdate.onUpdateValue(this.valve, object);
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
            valve = onTimeOver(valve);
        }
    }

}

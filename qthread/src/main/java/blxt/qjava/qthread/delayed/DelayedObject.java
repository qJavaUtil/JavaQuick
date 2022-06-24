package blxt.qjava.qthread.delayed;


/**
 * 倒计时数据更新.
 * @author OpenJialei
 */
public interface DelayedObject<T> {

    /**
     * 数据响应.
     *
     * @param valves valves
     */
    T onTimeOver(final T valves);
}

package blxt.qjava.qthread.delayed;


import java.util.List;

/**
 * 倒计时数据更新.
 * @author OpenJialei
 */
public interface DelayedObjectList<T> {

    /**
     * 数据响应.
     *
     * @param valves valves
     */
    void onTimeOver(final List<T> valves);
}

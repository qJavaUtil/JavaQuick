package blxt.qjava.qthread.delayed;

/**
 * 数据更新接口
 * @author OpenJialei
 */
public interface ObjectUpdate<T> {

    /**
     * 旧数据处理.
     *
     * @param oldValue 旧数据
     * @param newValue 新数据
     */
    T onUpdateValue(T oldValue, T newValue);
}

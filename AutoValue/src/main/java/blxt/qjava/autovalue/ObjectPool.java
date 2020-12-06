package blxt.qjava.autovalue;

import java.util.HashMap;


/**
 * 数据池
 * @Author: Zhang.Jialei
 * @Date: 2020/12/5 21:56
 */
public class ObjectPool {
    static HashMap<Class<?>, Object> datas = new HashMap<>();


    /**
     * 从对象池获取对象,如果对象不存在,将会新建
     * @param object
     * @return
     */
    public static Object getObject(Class<?>  object){
        Object obj = datas.get(object);
        if(obj == null){
            obj = ObjectPool.putObject(object);
        }
        return obj;
    }

    /**
     * 塞入新对象
     * @param object
     * @return
     */
    public static Object putObject(Class<?>  object){
        try {
            Object obj = object.newInstance();
            putObject(object, obj);
            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 塞入新对象
     * @param key
     * @param object
     */
    public static void putObject(Class<?> key, Object object){
        if(datas.get(object) == null){
            datas.put(key, object);
        }
    }

}

package blxt.qjava.autovalue.util;

import blxt.qjava.autovalue.reflect.PackageUtil;

import java.util.HashMap;


/**
 * 数据池
 * @Author: Zhang.Jialei
 * @Date: 2020/12/5 21:56
 */
public class ObjectPool {
    static HashMap<Class<?>, Object> datas = new HashMap<>();

    /**
     * 判断某个对象是否存在
     * @param object
     * @return
     */
    public static boolean isEmpty(Class<?>  object){
        Object obj = datas.get(object);
        if(obj == null){
            return true;
        }
        return false;
    }

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

    /**
     * 塞入接口实现类
     */
    public static boolean putInterfaceImp(Class<?> key, Object object) {
        if(PackageUtil.isInterfaces(object.getClass(), key)){
            putObject(key, object);
            return true;
        }
        throw new RuntimeException(object.getClass().getName() + "未实现" + key.getName());
    }


}

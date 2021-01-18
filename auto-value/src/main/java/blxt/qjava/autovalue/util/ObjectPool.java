package blxt.qjava.autovalue.util;

import blxt.qjava.autovalue.reflect.PackageUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        return putObjectWithParams(object, null);
    }

    /**
     * 通过反射,构建带参的构造
     * @param object
     * @param params
     * @return
     */
    public static Object putObjectWithParams(Class<?>  object, Object[] params){
        try {
            Object obj = null;
            if (params != null) {
                Class[] argsClass = new Class[params.length];
                for (int i = 0, j = params.length; i < j; i++) {
                    argsClass[i] = params[i].getClass();
                }
                Constructor cons = object.getConstructor(argsClass);
                obj = cons.newInstance(params);
            }
            else{
                obj = object.newInstance();
            }
            putObject(object, obj);
            return obj;
        } catch (InstantiationException | IllegalAccessException |
                NoSuchMethodException |InvocationTargetException e) {
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

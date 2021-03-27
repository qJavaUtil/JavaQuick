package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.Autowired;
import blxt.qjava.autovalue.inter.Component;
import blxt.qjava.autovalue.inter.Server;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.util.ObjectValue;

import javax.xml.ws.Service;
import java.lang.reflect.Field;

/**
 * 自动装载对象
 * @Author: Zhang.Jialei
 * @Date: 2020/12/4 12:34
 */
@AutoLoadFactory(name="AutoObject", annotation = Server.class, priority = 1)
public class AutoServer extends AutoLoadBase {

    /**
     * Autowired 注解注册
     *
     * @param object  class class的构造函数必须是public的
     * @return 初始化后的实例对象
     */
    @Override
    public  <T> T  inject(Class<?> object) throws Exception {
        Autowired valuename = object.getAnnotation(Autowired.class);


        T bean = ObjectPool.getObject(object);



        ObjectPool.upObject(object, bean);
        return bean;
    }

    /**
     * Autowired 注解注册
     *
     * @param object  class class的构造函数必须是public的
     * @return 初始化后的实例对象
     */
    public Object autoWiredRegister(Class<?> object) throws Exception {

        return inject(object);
    }


    /**
     * 将对象注入统一的Object管理池.
     *
     * @param object   class class的构造函数必须是public的
     * @return 初始化后的实例对象
     */
    public static Object register(Class<?>  object){
        return  ObjectPool.putObject(object);
    }


}

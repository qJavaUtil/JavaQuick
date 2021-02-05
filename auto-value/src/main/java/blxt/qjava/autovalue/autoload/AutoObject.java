package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.Autowired;
import blxt.qjava.autovalue.inter.Component;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.util.ObjectValue;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 自动装载对象
 * @Author: Zhang.Jialei
 * @Date: 2020/12/4 12:34
 */
@AutoLoadFactory(name="AutoObject", annotation = Component.class, priority = 2)
public class AutoObject extends AutoLoadBase {

    /**
     * Autowired 注解注册
     *
     * @param object  class class的构造函数必须是public的
     * @return 初始化后的实例对象
     */
    @Override
    public Object inject(Class<?> object) throws Exception {
        Object bean = ObjectPool.getObject(object);

        // 获取f对象对应类中的所有属性域
        Field[] fields = bean.getClass().getDeclaredFields();

        // 遍历属性
        for (Field field : fields)
        {
            // 过滤 final 元素, 获取修饰关键词  Modifier.toString(field.getModifiers())
            if ((field.getModifiers() & 16) != 0){
                continue;
            }
            Autowired valuename = field.getAnnotation(Autowired.class);
            if(valuename == null){
                continue;
            }

            // 属性值
            Object value;
            try {
                // 根据类型,获取对象
                Class<?> objClass = field.getType();
                /* 递归, 子元素内的Autowired注解实现 value = ObjectPool.getObject(objClass); */
                value = autoWiredRegister(objClass);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            // 设置值
            ObjectValue.setObjectValue(bean,field, value, falSetAccessible);

        }
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

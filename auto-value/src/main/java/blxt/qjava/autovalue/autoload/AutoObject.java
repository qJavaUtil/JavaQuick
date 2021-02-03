package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.Autowired;
import blxt.qjava.autovalue.inter.Component;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ObjectPool;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
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
    public Object inject(Class<?> object) throws IntrospectionException {
        Object bean = ObjectPool.getObject(object);

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(bean);
                    System.out.println(value);
                }
            }
        } catch (Exception e) {

        }

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

            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), object);
            Method wM = pd.getWriteMethod();//获得写方法
            wM.invoke(bean, 2);

            // 获取原来的访问控制权限
            boolean accessFlag = field.isAccessible();
            // 修改访问控制权限
            field.setAccessible(true);

            // 属性值
            Object value;
            try {
                // 根据类型,获取对象
                Class<?> objClass = field.getType();
                /* 递归, 子元素内的Autowired注解实现 value = ObjectPool.getObject(objClass); */
                value = autoWiredRegister(objClass);
                field.set(bean, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }


            // 恢复访问控制权限
            field.setAccessible(accessFlag);
        }
        return bean;
    }

    /**
     * Autowired 注解注册
     *
     * @param object  class class的构造函数必须是public的
     * @return 初始化后的实例对象
     */
    public Object autoWiredRegister(Class<?> object){

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

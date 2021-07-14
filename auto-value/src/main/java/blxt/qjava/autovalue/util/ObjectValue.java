package blxt.qjava.autovalue.util;

import blxt.qjava.autovalue.autoload.AutoValue;
import blxt.qjava.autovalue.reflect.PackageUtil;
import org.apache.commons.beanutils.ConvertUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObjectValue {

    public static  <T> T  getObjectValue(Object bean, String key, Class<?> parameterType) throws Exception {
        return getObjectValue(bean, key, parameterType, false);
    }
    /**
     * 获取
     *
     * @param bean
     * @param key
     * @param parameterType
     * @param toLowerCase      忽略大小写
     * @return
     * @throws Exception
     */
    public static  <T> T  getObjectValue(Object bean, String key, Class<?> parameterType, Boolean ...toLowerCase)
            throws Exception {
        Object obj = null;
        if(key.startsWith("$")) {
            key = key.substring(1);
        }
        // 先从当前类的变量中匹配
        Field field = PackageUtil.findField(bean.getClass(), key);
        if(field == null){
            throw new Exception("field 没找到." + bean.getClass().getName() + ":" + key);
        }
        //获取属性值
//                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), bean.getClass());
//                //Method wM = pd.getWriteMethod();//获得写方法
//                Method getter = pd.getReadMethod();
//                obj = getter.invoke(bean);
        boolean accessFlag = field.isAccessible();
        field.setAccessible(true);
        obj = field.get(bean);
        field.setAccessible(accessFlag);
        // 再从配置文件中匹配
        if (obj == null && !AutoValue.isNull(key)) {
            obj = AutoValue.getPropertiesValue(key, parameterType);
        }
        return (T) obj;
    }

    /**
     * 设置元素值
     *
     * @param bean
     * @param field
     * @param value
     * @param falSetAccessible
     * @return
     */
    public static boolean setObjectValue(Object bean, Field field, Object value, boolean falSetAccessible) {
        try {
            // 这里对值做一个转换
            if (value != null && !value.getClass().equals(field.getType())){
                value = ConvertUtils.convert(value, field.getType());
            }

            if (falSetAccessible) {
                // 获取原来的访问控制权限
                boolean accessFlag = field.isAccessible();
                field.setAccessible(true);
                field.set(bean, value);
                field.setAccessible(accessFlag);
            } else {
                field.set(bean, value);
            }
            return true;
        } catch (Exception e) {
            //log.error("负值异常:{}<-{}", field.getName(), value);
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 使用set
     * @param bean
     * @param field
     * @param value
     * @return
     */
    public static boolean setter(Object bean, Field field, Object value) {
        try {
            // 设置值
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), bean.getClass());
            //获得写方法 
            Method wM = pd.getWriteMethod();
            // Method getter = property.getReadMethod();
            wM.invoke(bean, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 调佣get
     * @param bean
     * @param field
     * @return
     */
    public static Object getter(Object bean, Field field){
        try {
            // 设置值
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), bean.getClass());
            Method getter = pd.getReadMethod();
            return getter.invoke(bean);
        } catch (Exception e) {
            return null;
        }
    }

}

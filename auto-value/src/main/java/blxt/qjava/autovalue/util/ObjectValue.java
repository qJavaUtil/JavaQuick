package blxt.qjava.autovalue.util;

import blxt.qjava.autovalue.autoload.AutoValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Slf4j
public class ObjectValue {


    /**
     * 获取
     *
     * @param bean
     * @param key
     * @param parameterType
     * @return
     * @throws Exception
     */
    public static  <T> T  getObjectValue(Object bean, String key, Class<?> parameterType) throws Exception {
        Object obj = null;
        if(key.startsWith("$")) {
            key = key.substring(1);
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        // 先从当前类的变量中匹配
        for (Field field : fields) {
            if (key.equals(field.getName())) {
                if (field.getType() != parameterType) {
                    throw new Exception(String.format("变量:($%s)和内部参数类型不一致,请检查. 变量类型:%s, 所需类型:%s", key, field.getType(), parameterType));
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

                break;
            }
        }
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
            Object convert = ConvertUtils.convert(value, field.getType());
            if (falSetAccessible) {
                // 获取原来的访问控制权限
                boolean accessFlag = field.isAccessible();
                field.setAccessible(true);
                field.set(bean, field);
                field.setAccessible(accessFlag);
            } else {
                field.set(bean, convert);
            }
            return true;
        } catch (Exception e) {
            log.error("负值异常:{}<-{}", field.getName(), value);
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

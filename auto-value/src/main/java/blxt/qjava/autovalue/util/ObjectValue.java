package blxt.qjava.autovalue.util;

import blxt.qjava.autovalue.autoload.AutoValue;

import java.lang.reflect.Field;

public class ObjectValue {


    /**
     * 获取
     * @param bean
     * @param key
     * @param parameterType
     * @return
     * @throws Exception
     */
    public static Object getObjectValue(Object bean, String key, Class<?> parameterType) throws Exception {
        Object obj = null;
        Field[] fields = bean.getClass().getDeclaredFields();
        // 先从当前类的变量中匹配
        for(Field field : fields){
            if(key.equals(field.getName())){
                if(field.getType() != parameterType){
                    throw new Exception(String.format("变量:($%s)和内部参数类型不一致,请检查. 变量类型:%s, 所需类型:%s", key, field.getType(), parameterType));
                }
                //打开私有访问
                boolean accessFlag = field.isAccessible();
                field.setAccessible(true);
                //获取属性值
                obj = field.get(bean);
                field.setAccessible(accessFlag);
                break;
            }
        }
        // 再从配置文件中匹配
        if(obj == null && !AutoValue.isNull(key)){
            obj = AutoValue.getPropertiesValue(key, parameterType);
        }
        return obj;
    }

}

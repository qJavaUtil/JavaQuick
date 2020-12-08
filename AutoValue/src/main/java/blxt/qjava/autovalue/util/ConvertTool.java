package blxt.qjava.autovalue.util;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/8 15:31
 */
public class ConvertTool {


    /**
     * 常见类型值转换
     * @param str
     * @param parametertype
     * @return
     */
    public static Object convert(String str, Class<?> parametertype){
        Object value = null;
        if(parametertype == String.class){
            value = str;
        }
        else if(parametertype == Integer.class || parametertype == int.class ){
            value = Integer.parseInt(str);
        }
        else if(parametertype == Float.class || parametertype == float.class){
            value = Float.parseFloat(str);
        }
        else if(parametertype == Boolean.class || parametertype == boolean.class){
            value = Boolean.parseBoolean(str);
        }
        else if(parametertype == Long.class || parametertype == long.class){
            value = Long.parseLong(str);
        }
        else if(parametertype == Short.class || parametertype == short.class){
            value = Short.parseShort(str);
        }
        else if(parametertype == Double.class || parametertype == double.class){
            value = Double.parseDouble(str);
        }

        return value;
    }


}

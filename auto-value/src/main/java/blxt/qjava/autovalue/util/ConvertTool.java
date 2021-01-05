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
        str = str.trim();
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

    public static Object[] convertArry(String str, Class<?> parametertype){
        Object[] value = null;
        str = str.trim();
        if(str.startsWith("[") && str.endsWith("]")){
            str = str.substring(1);
            str = str.substring(0, str.length() - 1).trim();
        }
        String[] valueArreys = str.split(",");
        if(parametertype == String[].class){
            value = valueArreys;
        }
        else if(parametertype == Integer[].class || parametertype == int[].class ){
            value = new Integer[valueArreys.length];
            for(int i = 0; i < valueArreys.length; i++){
                value[i] = Integer.parseInt(valueArreys[i]);
            }
        }
        else if(parametertype == Float[].class || parametertype == float[].class){
            value = new Float[valueArreys.length];
            for(int i = 0; i < valueArreys.length; i++){
                value[i] = Float.parseFloat(valueArreys[i]);
            }
        }
        else if(parametertype == Boolean[].class || parametertype == boolean[].class){
            value = new Boolean[valueArreys.length];
            for(int i = 0; i < valueArreys.length; i++){
                value[i] = Boolean.parseBoolean(valueArreys[i]);
            }
        }
        else if(parametertype == Long[].class || parametertype == long[].class){
            value = new Long[valueArreys.length];
            for(int i = 0; i < valueArreys.length; i++){
                value[i] = Long.parseLong(valueArreys[i]);
            }
        }
        else if(parametertype == Short[].class || parametertype == short[].class){
            value = new Short[valueArreys.length];
            for(int i = 0; i < valueArreys.length; i++){
                value[i] = Short.parseShort(valueArreys[i]);
            }
        }
        else if(parametertype == Double[].class || parametertype == double[].class){
            value = new Double[valueArreys.length];
            for(int i = 0; i < valueArreys.length; i++){
                value[i] = Double.parseDouble(valueArreys[i]);
            }
        }

        return value;
    }


}

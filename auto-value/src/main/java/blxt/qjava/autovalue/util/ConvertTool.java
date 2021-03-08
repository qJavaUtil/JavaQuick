package blxt.qjava.autovalue.util;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static List convertList(String str, ParameterizedType pt) throws Exception {
        if (!pt.getRawType().equals(List.class)) {
            throw new Exception(pt + "不是List类型");
        }

        // 判断泛型类的类型
        if (pt.getActualTypeArguments()[0].equals(String.class)) {
            return new ArrayList(Arrays.asList(convertArry(str, String[].class)));
        } else if (pt.getActualTypeArguments()[0].equals(int.class)
                || pt.getActualTypeArguments()[0].equals(Integer[].class)) {
            return new ArrayList(Arrays.asList(convertArry(str, Integer[].class)));
        } else if (pt.getActualTypeArguments()[0].equals(float.class)
                || pt.getActualTypeArguments()[0].equals(Float.class)) {
            return new ArrayList(Arrays.asList(convertArry(str, Float[].class)));
        }else if (pt.getActualTypeArguments()[0].equals(boolean.class)
                || pt.getActualTypeArguments()[0].equals(Boolean.class)) {
            return new ArrayList(Arrays.asList(convertArry(str, Boolean[].class)));
        }else if (pt.getActualTypeArguments()[0].equals(long.class)
                || pt.getActualTypeArguments()[0].equals(Long.class)) {
            return new ArrayList(Arrays.asList(convertArry(str, Long[].class)));
        }else if (pt.getActualTypeArguments()[0].equals(short.class)
                || pt.getActualTypeArguments()[0].equals(Short.class)) {
            return new ArrayList(Arrays.asList(convertArry(str, Short[].class)));
        }else if (pt.getActualTypeArguments()[0].equals(double.class)
                || pt.getActualTypeArguments()[0].equals(Double.class)) {
            return new ArrayList(Arrays.asList(convertArry(str, Double[].class)));
        }
        else{
            throw new Exception(pt + "不支持的List<T>类型:" + pt);
        }
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

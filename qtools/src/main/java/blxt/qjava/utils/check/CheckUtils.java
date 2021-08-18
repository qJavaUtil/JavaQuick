package blxt.qjava.utils.check;


import blxt.qjava.utils.exception.DataException;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author MrWang
 * @version v1.0
 * @date 2019/02/20
 * @Description 自定义校验工具类
 */
public class CheckUtils {

    /**
     * 字符串校验
     */
    public static class STR{
        /**
         * 字符串非空校验
         *
         * @param filed   要校验的字段
         * @param message message
         * @param code    code
         * @param data    data
         */
        public static void stringIsNullCheck(String filed, String message, Integer code, Object data) {
            if (StringCheck.isBlank(filed)) {
                throw new DataException(code, message, data);
            }
        }

        /**
         * 数字校验
         *
         * @param filed   要校验的字段
         * @param message message
         * @param code    code
         * @param data    data
         */
        public static void isNumberCheck(String filed, String message, Integer code, Object data) {
            if (!StringCheck.isNumber(filed)) {
                throw new DataException(code, message, data);
            }
        }



        /**
         * 整数校验
         *
         * @param filed   要校验的字段
         * @param message message
         * @param code    code
         * @param data    data
         */
        public static void isIntCheck(String filed, String message, Integer code, Object data) {
            if (!StringCheck.isInteger(filed)) {
                throw new DataException(code, message, data);
            }
        }

        /**
         * 邮件校验
         *
         * @param filed   要校验的字段
         * @param message message
         * @param code    code
         * @param data    data
         */
        public static void isEmailCheck(String filed, String message, Integer code, Object data) {
            if (!StringCheck.isEmail(filed)) {
                throw new DataException(code, message, data);
            }
        }

        /**
         * 手机号校验
         *
         * @param filed   要校验的字段
         * @param message message
         * @param code    code
         * @param data    data
         */
        public static void isMobileCheck(String filed, String message, Integer code, Object data) {
            if (!StringCheck.isMobile(filed)) {
                throw new DataException(code, message, data);
            }
        }
    }

    public static class FILE{
        /**
         * 判断是否是文件,如果文件不存在,或者是文件夹,将抛出异常
         * @param file
         * @param message
         * @param code
         * @param data
         */
        public static void checkFile(File file, String message, Integer code, Object data){
            if(isEmpty(file)){
                throw new DataException(code, message, data);
            }
            if(!file.isFile()){
                throw new DataException(code, message, data);
            }
        }

        /**
         * 判断是否是文件夹, 如果文件夹不存在,或则是文件,就抛出异常
         * @param file
         * @param message
         * @param code
         * @param data
         */
        public static void checkFileDirectory(File file, String message, Integer code, Object data){
            if(isEmpty(file)){
                throw new DataException(code, message, data);
            }
            if(!file.isDirectory()){
                throw new DataException(code, message, data);
            }
        }

    }


    /**
     * 空值检查
     * 如果非空,就抛出异常
     * @param object
     * @param message
     * @param code
     * @param data
     */
    public static <T> T objectCheckNull(T object, String message, Integer code, Object data) {
        if (isEmpty(object)) {
            throw new DataException(code, message, data);
        }
        return object;
    }

    /**
     * 非空值检查
     * 如果非空值,就抛出异常
     * @param object
     * @param message
     * @param code
     * @param data
     */
    public static <T> T objectCheckNotNull(T object, String message, Integer code, Object data) {
        if (isNotEmpty(object)) {
            throw new DataException(code, message, data);
        }
        return object;
    }

    /**
     * 对象是否不为空(新增)
     *
     * @return
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 对象是否为空
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        // 特殊判断, 按照经验使用频率排序
        if (o instanceof String) {
            return ((String) o).isEmpty();
        }else if (o instanceof List) {
            return ((List) o).isEmpty();
        } else if (o.getClass().isArray()) {
            return ((Object[]) o).length == 0;
        } else if (o instanceof Map) {
            return ((Map) o).isEmpty();
        }else if (o instanceof Set) {
            return ((Set) o).isEmpty();
        } else if(o instanceof File){
            return !((File) o).exists();
        }
        return false;
    }
}
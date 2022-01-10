package blxt.qjava.utils.check;


import blxt.qjava.autovalue.exception.DataException;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MrWang
 * @version v1.0
 * @date 2019/02/20
 * @Description 自定义校验工具类
 */
public class CheckUtils {
    /** 用户名中英文 正则*/
    static final String NAME_MATCHER_CN = "^[\\w\\-－＿\\.@()（）[０-９]\u4e00-\u9fa5\uFF21-\uFF3A\uFF41-\uFF5A]+$";
    /** 用户名英文 正则*/
    static final String NAME_MATCHER_EN = "^[\\w\\-－＿\\.@()（）[０-９]]+$";

    public static void main(String[] args) {
        CheckUtils.nameCheck("1231asdfag", true);
        System.out.println("通过0");
        CheckUtils.nameCheck("1231asdfag", true);
        System.out.println("通过1");
        CheckUtils.nameCheck("1231asdfag", true);
        System.out.println("通过2");
        CheckUtils.nameCheck("1231Asdfag", false);
        System.out.println("通过3");
        CheckUtils.nameCheck("1231Asdfag.", true);
        System.out.println("通过4");
        CheckUtils.nameCheck("０1231Asdfag.@", true);
        System.out.println("通过5");
        CheckUtils.nameCheck("1231Asdfag.@（）()", true);
        System.out.println("通过6");
        CheckUtils.nameCheck("1231Asdfag.@#", true);
        System.out.println("通过7");
    }

    /**
     * 字符串校验
     */
    public static class STR{


        /**
         * 数字校验
         *
         * @param filed   要校验的字段
         * @param message message
         * @param code    code
         * @param data    data
         */
        public static void isNumber(String filed, String message, Integer code, Object data) {
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
        public static void isInt(String filed, String message, Integer code, Object data) {
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
        public static void isEmail(String filed, String message, Integer code, Object data) {
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
        public static void isMobile(String filed, String message, Integer code, Object data) {
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
     * 检查是否满足密码设定
     *  * 密码不能为空，
     *  * 密码长度不小于8位数，包含小写字母、大写字母、数字和特殊字符。
     * @param password     要检查的字符串
     * @param complexity   需要的复杂度,0 ~ 4, 0 不检查, 4 要求小于8位数，包含小写字母、大写字母、数字和特殊字符。
     * @return
     */
    public static String passwordCheck(String password, int complexity){
        if (isEmpty(password)) {
            throw new DataException(20104, "密码不能为空", null);
        }
        if(complexity > 0 ){
            if(password.length() < 8 ){
                throw new DataException(20104, "密码长度不能小于8", null);
            }
            if(password.length() > 511 ){
                throw new DataException(20104, "密码长度不能长于511", null);
            }
            int sum = 0;
            sum += password.matches(".*[a-z]{1,}.*") ? 1 : 0;
            sum += password.matches(".*[A-Z]{1,}.*") ? 1 : 0;
            sum += password.matches(".*\\d{1,}.*") ? 1 : 0;
            sum += password.matches(".*[~!@#$%^&*\\.?]{1,}.*") ? 1 : 0;

            if(sum < complexity){
                throw new DataException(20104,
                    String.format("需要包含小写字母、大写字母、数字和特殊字符(英文符号)中的%d个组合", complexity),
                    password);
            }
        }
        return password;
    }

    /**
     * 检查是否满足 用户名称检查
     *  * 用户名称不能为空，
     *  * 用户名称小于2位数，包含小写字母、大写字母、数字
     *  * 不包含和特殊字符
     * @param name         要检查的字符串
     * @param cn           是否支持中文
     * @return
     */
    public static String nameCheck(String name, boolean cn){
        if (isEmpty(name)) {
            throw new DataException(20105, "名称不能为空", null);
        }
        if(name.length() < 2 ){
            throw new DataException(20105, "名称长度不能小于2", name);
        }
        if(name.length() > 63 ){
            throw new DataException(20105, "名称长度不能长于63", null);
        }

        // 正则检查
        Pattern pattern = null;
        if(cn){
            pattern = Pattern.compile(NAME_MATCHER_CN);
        }
        else{
            pattern = Pattern.compile(NAME_MATCHER_EN);
        }

        Matcher matcher = pattern.matcher(name);
        if(!matcher.matches()){
            if(cn){
                throw new DataException(20105, "名称只能用中英文和数字", name);
            }
            else{
                throw new DataException(20105, "名称只能用英文和数字", name);
            }
        }

        return name;
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


    /**
     * 判断是否是基础类型
     * @param classes
     * @return
     */
    public static boolean isPrimitive(Class<?> classes) {
        if(classes.isPrimitive()){
            return true;
        }
        if(classes.equals(String.class)){
            return true;
        }
        try {
            return ((Class<?>)classes.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }
}
package blxt.qjava.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/***
 * 快速转换工具
 */
public class Converter {
    static final byte[] HEX_TABLE = {0,0x1,0x2,0x3,0x4,0x5,0x6,0x7,0x8,0x9,0xA,0xB,0xC,0xD,0xE,0xF};
    static final char[] HEX_CHAR_TABLE = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    /**
     * byte[] 转 char[]
     * @param bytes
     * @param code    编码,如"UTF-8"
     * @return
     */
    public static char[] toChars(byte[] bytes, String code) {
        Charset cs = Charset.forName(code);
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    /**
     * byte[] 转 string
     * @param bytes
     * @param code   编码,如"UTF-8"
     * @return
     */
    public static String toStr(byte[] bytes, String code) {
        char[] a = toChars(bytes, code);
        return new String(a);
    }

    /**
     * byte[] 格式化成 16进制 字符串
     * @param src
     * @param format  格式, 如 "%x "
     * @return
     */
    public static String toStrFormat(byte[] src, String format) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            stringBuilder.append(String.format(format, new Object[]{Byte.valueOf(src[i])}));
        }

        return stringBuilder.toString();
    }

    /**
     * int[] 格式化成 字符串
     * @param src
     * @param format 格式, 如 "%x "
     * @return
     */
    public static String toStrFormat(int[] src, String format) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            stringBuilder.append(String.format(format, new Object[]{Integer.valueOf(src[i])}));
        }

        return stringBuilder.toString();
    }

    public static short[] toShorts(String licString) {
        if (licString == null || licString.equals("")) {
            return null;
        }
        licString = licString.toUpperCase();
        int length = licString.length();
        char[] hexChars = licString.toCharArray();
        short[] date = new short[length];
        for (int i = 0; i < length; i++) {
            date[i] = (short) hexChars[i];
        }
        return date;
    }

    public static byte[] toBytes(char[] chars, String code) {
        Charset cs = Charset.forName(code);
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        byte[] bytes = bb.array();

        int i = bytes.length;
        for (; i > 1 && bytes[i - 1] == 0; i--) ;
        byte[] bytes_new = new byte[i];
        System.arraycopy(bytes, 0, bytes_new, 0, i);
        return bytes_new;
    }

    /**
     * ByteBuffer 转 byte[]
     * @param bytes
     * @return
     */
    public static byte[] toBytes(ByteBuffer bytes) {
        int len = bytes.limit() - bytes.position();
        byte[] bytes1 = new byte[len];
        bytes.get(bytes1);
        return bytes1;
    }

    /**
     * 16进制字符串转 byte
     * 如 A1 B3 FE 转成 byte[]
     * @param content  数据
     * @param split    数据分隔符, 如' '或'0x'
     * @return
     */
    public static byte[] toBytesByFormat(String content, String split) {
        String[] beans = content.split(split);
        byte[] byteNum = new byte[beans.length];

        int index = 0;
        for (String bean : beans) {
            if (bean == null || bean.isEmpty()) {
                continue;
            }
            bean = bean.trim();
            if (bean.length() > 0) {
                byteNum[index++] = Byte.parseByte(bean, 0x10);
            }
        }
        return byteNum;
    }

    /**
     * 16进制字符串转 byte
     * 如 A1 B3 FE 转成 byte[]
     * @param hexString  数据
     * @return
     */
    public static byte[] toBytesByHex(String hexString){
        if(hexString==null || hexString.length()==0){
            return null;
        }
        if(hexString.length()%2!=0){
            throw new RuntimeException();
        }
        byte[] data = new byte[hexString.length()/2];
        char[] chars = hexString.toCharArray();
        for(int i=0;i<hexString.length();i=i+2){
            data[i/2] = (byte)(HEX_TABLE[getHexCharValue(chars[i])]<<4 | HEX_TABLE[getHexCharValue(chars[i+1])]);
        }
        return data;
    }

    /**
     * char to int
     * @param c
     * @return
     */
    private static int getHexCharValue(char c){
        int index = 0;
        for(char c1: HEX_CHAR_TABLE){
            if(c==c1){
                return index;
            }
            index++;
        }
        return 0;
    }

    /**
     * long 转 bytes
     * @param num
     * @return
     */
    public static byte[] toBytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ix++) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) (int) (num >> offset & 0xFFL);
        }
        return byteNum;
    }

    /**
     * int 转 bytes
     * @param num
     * @return
     */
    public static byte[] toBytes(int num) {
        byte[] result = new byte[4];
        result[0] = (byte)((num >> 24) & 0xFF);
        result[1] = (byte)((num >> 16) & 0xFF);
        result[2] = (byte)((num >> 8) & 0xFF);
        result[3] = (byte)(num & 0xFF);
        return result;
    }


    /**
     * int 转 byte
     * @param num
     * @return
     */
    public static byte toByte(int num) {
        return (byte) (num & 0xFF);
    }

    /**
     * byte 转 long
     * @param byteNum
     * @return
     */
    public static long toLong(byte[] byteNum) {
        long num = 0L;
        for (int ix = 0; ix < 8; ix++) {
            num <<= 8L;
            num |= (byteNum[ix] & 0xFF);
        }
        return num;
    }


    /**
     * byte 转int
     * @param byteNum
     * @return
     */
    public static int toInt(byte byteNum) {
        return (byteNum > 0) ? byteNum : (0x80 + 0x80 + byteNum);
    }

    /**
     * byte[4]转int
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int toInt(byte[] bytes) {
        int value=0;
        for(int i = 0; i < 4; i++) {
            int shift= (3-i) * 8;
            value +=(bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * byte[2]转int
     * 两个十六进制转为十进制,高位在后，返回十进制数
     * @param b1
     * @param b2
     * @return
     */
    public static int toInt(byte b1, byte b2){
        int deci = (b1|(b2<<8));
        return deci;
    }

    /**
     * byte[2]转toFloat
     * 两个十六进制转为十进制,高位在后，返回十进制数缩小十倍保留一位小数值
     * @param b1
     * @param b2
     * @return
     */
    public static float toFloat(byte b1, byte b2){
        int deci = (b1|(b2<<8));
        return (float)(deci*0.1);
    }

    /**
     * 提取数字.
     * @param text 员数字
     * @return 获取数字
     */
    public static String getNumberStr(final String text){
        final String regEx="([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        StringBuilder stringBuilder = new StringBuilder();
        while (m.find()) {
            stringBuilder.append((m.group()));
        }
        return stringBuilder.toString();
    }



    /**
     * 获取当前格式化时间
     * @param format
     * @return
     */
    public static String getTimeStr(String format) {
        Date currentTime = new Date();
        return getTimeStr(currentTime, format);
    }

    /**
     * 获取格式化时间
     * @param currentTime
     * @param format
     * @return
     */
    public static String getTimeStr(Date currentTime, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取文件名和文件后缀
     * @param file
     * @return   [0] 文件名; [1] 文件后缀
     */
    public static String[] getFileDisassemble(String file){
        int sum = file.lastIndexOf(".");
        return new String[]{
                file.substring(0, sum),
                file.substring(sum + 1)
        };
    }

    /**
     * 转map
     * @param object
     * @return
     */
    public static Map<String, Object> toMap(JSONObject object) {
        //map对象
        Map<String, Object> map = new HashMap<>();
        //循环转换
        Iterator it = object.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }


    /**
     * url中的参数转json
     * 如 <code>String paramStr = "a=a1&b=b1&c=c1";</code>
     *
     * @param paramStr
     * @return
     */
    public static JSONObject urlParam2Json(String paramStr) {
        if(getJSONType(paramStr)){
            return  JSON.parseObject(paramStr);
        }
        String[] params = paramStr.split("&");
        JSONObject obj = new JSONObject();
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].split("=|:");
            if (param.length >= 2) {
                String key = param[0];
                String value = param[1];
                for (int j = 2; j < param.length; j++) {
                    value += "=" + param[j];
                }
                try {
                    obj.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    /**
     * 简单判断是否为json格式 .
     * 判断规则：判断首尾字母是否为{}或[]，如果都不是则不是一个JSON格式的文本。
     * @author lipeng
     * @version 1.0
     * @date 2020/5/21 15:48
     *
     */
    public static boolean getJSONType(String str) {
        boolean result = false;
        str = str.trim();
        if (str.startsWith("{") && str.endsWith("}")) {
            result = true;
        } else if (str.startsWith("[") && str.endsWith("]")) {
            result = true;
        }
        return result;
    }

    /**
     * 转换成json
     * @param bean
     * @return
     */
    public static Object toJson(Object bean){
        return JSON.toJSON(bean);
    }

    /**
     * 转换成json字符串
     * @param bean
     * @return
     */
    public static String toJsonStr(Object bean){
        return JSON.toJSONString(bean);
    }

    /**
     * 将字符串转换成常见类型
     * @param str
     * @param parametertype
     * @return
     */
    public static Object toObject(String str, Class<?> parametertype){
        Object value = null;
        if (str != null){
            str = str.trim();
        }
        if(parametertype == String.class){
            value = str;
        }
        else if(parametertype == Integer.class || parametertype == int.class ){
            if(str == null || str.isEmpty()){
                return 0;
            }
            try {
                value = Integer.parseInt(str);
            }catch (Exception e){
                value = (int)Long.parseLong(str);
            }
        }
        else if(parametertype == Float.class || parametertype == float.class){
            if(str == null || str.isEmpty()){
                return 0;
            }
            value = Float.parseFloat(str);
        }
        else if(parametertype == Long.class || parametertype == long.class){
            if(str == null || str.isEmpty()){
                return 0;
            }
            value = Long.parseLong(str);
        }
        else if(parametertype == Short.class || parametertype == short.class){
            if(str == null || str.isEmpty()){
                return 0;
            }
            value = Short.parseShort(str);
        }
        else if(parametertype == Double.class || parametertype == double.class){
            if(str == null || str.isEmpty()){
                return 0;
            }
            value = Double.parseDouble(str);
        }
        else if(parametertype == Byte.class || parametertype == byte.class){
            if(str == null || str.isEmpty()){
                return 0;
            }
            value = Byte.parseByte(str);
        }
        else if(parametertype == Boolean.class || parametertype == boolean.class){
            if(str == null || str.isEmpty()){
                return false;
            }
            value = Boolean.parseBoolean(str);
        }
        else if (parametertype == Character.class) {
            if(str == null || str.isEmpty()){
                return null;
            }
            value = str.charAt(0);
        }
        // 其他复杂对象
        else{
            if(str == null || str.isEmpty()){
                return null;
            }
            value = JSON.parseObject(str, parametertype);
        }

        return value;
    }



    /**
     * 从字符串转换成List集合
     * @param str
     * @param pt
     * @return
     * @throws Exception
     */
    public static List toObjectList(String str, ParameterizedType pt) throws Exception {
        if (pt == null || !pt.getRawType().equals(List.class)) {
            throw new Exception(pt + "不是List类型");
        }

        // 判断泛型类的类型
        if (pt.getActualTypeArguments()[0].equals(String.class)) {
            return Arrays.asList(toObjects(str, String[].class));
        } else if (pt.getActualTypeArguments()[0].equals(int.class)
                || pt.getActualTypeArguments()[0].equals(Integer[].class)) {
            return Arrays.asList(toObjects(str, Integer[].class));
        } else if (pt.getActualTypeArguments()[0].equals(float.class)
                || pt.getActualTypeArguments()[0].equals(Float.class)) {
            return Arrays.asList(toObjects(str, Float[].class));
        }else if (pt.getActualTypeArguments()[0].equals(boolean.class)
                || pt.getActualTypeArguments()[0].equals(Boolean.class)) {
            return Arrays.asList(toObjects(str, Boolean[].class));
        }else if (pt.getActualTypeArguments()[0].equals(long.class)
                || pt.getActualTypeArguments()[0].equals(Long.class)) {
            return Arrays.asList(toObjects(str, Long[].class));
        }else if (pt.getActualTypeArguments()[0].equals(short.class)
                || pt.getActualTypeArguments()[0].equals(Short.class)) {
            return Arrays.asList(toObjects(str, Short[].class));
        }else if (pt.getActualTypeArguments()[0].equals(double.class)
                || pt.getActualTypeArguments()[0].equals(Double.class)) {
            return Arrays.asList(toObjects(str, Double[].class));
        }
        else{
            throw new Exception(pt + "不支持的List<T>类型:" + pt);
        }
    }

    /**
     * 从字符串转换成List集合
     * @param str      json
     * @param classes  集合 泛型
     * @return
     */
    public static List toObjectList(String str, Class classes)  {
        return JSONArray.parseArray(str, classes);
    }

    /**
     * 从字符串子节点转换成List集合
     * @param str      json
     * @return
     */
    public static Map toJsonMap(String str)  {
        return JSON.parseObject(str);
    }

    /**
     * 从字符串子节点转换成List集合
     * @param str      json
     * @param classes  集合 泛型
     * @return
     */
    public static List toObjectListByChildode(String str, String key, Class classes)  {
        JSONObject jsonObject = JSON.parseObject(str);
        Object data = jsonObject.get(key);
        if(data instanceof JSONArray){
            JSONArray jsonArray = (JSONArray)data;
            List<?> list = JSONObject.parseArray(jsonArray.toJSONString(), classes);
            return list;
        }
        return JSONArray.parseArray( data.toString(), classes);
    }


    /**
     * 从字符串转换成数组
     * @param str        json
     * @param classes   数组类型
     * @return
     */
    public static Object[] toObjects2(String str, Class classes)  {
        return JSONArray.parseArray(str, classes).toArray();
    }


    /**
     * 从字符串转换成数组
     * @param str
     * @param parametertype
     * @return
     */
    public static Object[] toObjects(String str, Class<?> parametertype){
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
        else{
            for(int i = 0; i < valueArreys.length; i++){
                value[i] = toObject(valueArreys[i], parametertype);
            }
        }

        return value;
    }


    // [{"uid":"1627466546679test3","type":1,"projectName":"test5555","projectid":18}]

    public static void main(String[] args) {
        String json = "[{\"uid\":\"1627466546679test3\",\"type\":1,\"projectName\":\"test5555\",\"projectid\":18}]";
        List list = new ArrayList();

        Object value = JSONObject.parseArray(json, String.class);

    }


}
 
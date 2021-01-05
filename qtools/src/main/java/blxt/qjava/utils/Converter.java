package blxt.qjava.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/***
 * 快速转换工具
 */
public class Converter {


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

        String[] params = paramStr.split("&");
        JSONObject obj = new JSONObject();
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].split("=");
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
     * 常见类型值转换
     * @param str          字符串
     * @param objectclass   要转换的类型,如 String.class Int.class boolean.class
     * @return
     */
    private Object toObject(String str, Class<?> objectclass){
        Object value = null;
        if(objectclass == String.class){
            value = str;
        }
        else if(objectclass == Integer.class || objectclass == int.class ){
            value = Integer.parseInt(str);
        }
        else if(objectclass == Float.class || objectclass == float.class){
            value = Float.parseFloat(str);
        }
        else if(objectclass == Boolean.class || objectclass == boolean.class){
            value = Boolean.parseBoolean(str);
        }
        else if(objectclass == Long.class || objectclass == long.class){
            value = Long.parseLong(str);
        }
        else if(objectclass == Short.class || objectclass == short.class){
            value = Short.parseShort(str);
        }
        else if(objectclass == Double.class || objectclass == double.class){
            value = Double.parseDouble(str);
        }
        return value;
    }

}
 
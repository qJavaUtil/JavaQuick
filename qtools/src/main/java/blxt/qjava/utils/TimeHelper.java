package blxt.qjava.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeHelper {
    public final static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /** 秒和时间的转换模 */
    public static final long nd = 1000 * 24 * 60 * 60;
    public static final long nh = 1000 * 60 * 60;
    public static final long nm = 1000 * 60;
    public static final long ns = 1000;

    public static Long getTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime().getTime();
    }

    public static String getTimeStr(String format) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getTimeStr(Long date, String format) {
        return getTimeStr(new Date(date), format);
    }

    public static String getTimeStr(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 将秒转换成时间描述
     */

    public static String getDatePoor(long diff) {
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
    }

}

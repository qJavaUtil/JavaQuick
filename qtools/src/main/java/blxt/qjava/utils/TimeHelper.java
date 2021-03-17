package blxt.qjava.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeHelper {
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
}

 package blxt.qjava.qtime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

 /**
  * 快速时间格式化工具
  * 迁移到 @links{blxt.qjava.utils.TimeHelper}
  * @author MI
  *
  */
 @Deprecated()
 public class QTime {

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

package blxt.qjava.utils;

import blxt.qjava.utils.check.CheckUtils;

import java.util.regex.Matcher;

public class PropertiesTools {

    public static boolean isKey1(String string){
        if (CheckUtils.isEmpty(string)){
            return false;
        }
        Matcher matcher = PatternPool.VALUE_STR_1.matcher(string);
        if(matcher.matches()){
            return true;
        }else{
            return false;
        }
    }

    public static String getKey1(String string){
        if (CheckUtils.isEmpty(string)){
            return null;
        }

        Matcher matcher = PatternPool.VALUE_STR_1.matcher(string);
        StringBuilder sql = new StringBuilder();
        while(matcher.find()) {
            sql.append(matcher.group(1)+",");
        }
        if (sql.length() > 0) {
            sql.deleteCharAt(sql.length() - 1);
        }
        return sql.toString();
    }

}

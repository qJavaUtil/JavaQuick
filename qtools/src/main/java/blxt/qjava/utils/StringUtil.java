package blxt.qjava.utils;

/**
 * @author OpenJialei
 * @date 2021年12月22日 20:49
 */
public class StringUtil {

    /**
     * 首字母大写
     * @param name
     * @return
     */
    public static String toUpperCaseFirst(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);

    }
}

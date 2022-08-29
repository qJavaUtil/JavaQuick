package blxt.qjava.utils.system;

import java.util.Locale;

/**
 *  操作系统 类型.
 * @author OpenJialei
 * @date 2022年04月11日 15:37
 */
public enum SystemosEnum {
    /** Windows. */
    Windows,
    /** Linux. */
    Linux;

    /**
     * 获取当前系统类型.
     * @return ExecuterType
     */
    public static SystemosEnum getOs(){
        String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.US).toUpperCase(Locale.ROOT);
        if(OS_NAME.startsWith("LINUX")){
            return Linux;
        }
        else{
            return Windows;
        }
    }

}

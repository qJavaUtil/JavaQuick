package blxt.qjava.qexecute;

import java.util.Locale;

/**
 *  Executer 类型.
 * @author OpenJialei
 * @date 2022年04月11日 15:37
 */
public enum ExecuterType {
    /** Windows. */
    Windows,
    /** Linux. */
    Linux;

    /**
     * 获取当前系统类型.
     * @return ExecuterType
     */
    public static ExecuterType getExecuterType(){
        String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.US).toUpperCase(Locale.ROOT);
        if(OS_NAME.startsWith("LINUX")){
            return Linux;
        }
        else{
            return Windows;
        }
    }

}

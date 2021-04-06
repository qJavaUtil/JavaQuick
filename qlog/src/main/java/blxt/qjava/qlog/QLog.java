package blxt.qjava.qlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/26 11:15
 */
public class QLog {
    public static Logger logger = null;

    static {
        init("system");
    }

    public static void init(String name){
        logger = LoggerFactory.getLogger(name);
    }

//    /**
//     * 指定配置文件,默认路径 src/main/resources下
//     * @param file  文件路径
//     */
//    public static void loadConfigure(File file){
//        PropertyConfigurator.configure(file.getPath());
//    }

}

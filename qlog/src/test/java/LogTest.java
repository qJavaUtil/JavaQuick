import com.qjava.qlog.QLog;
import org.junit.Test;

import java.io.File;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/26 11:09
 */
public class LogTest {

    @Test
    public void testSlf4j() {
        QLog.loadConfigure(new File("E:/Documents/workspace/java/JavaQuick/Qlog/logconfgDemo/log4j.properties"));
        fun();
    }

    public void fun(){
        QLog.logger.trace("123");
        QLog.logger.debug("123");
        QLog.logger.info("123");
        QLog.logger.warn("123");
        QLog.logger.error("123");
    }
}

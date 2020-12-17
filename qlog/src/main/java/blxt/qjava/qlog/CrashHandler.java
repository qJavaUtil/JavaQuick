package blxt.qjava.qlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认异常拦截器
 * @Author: Zhang.Jialei
 * @Date: 2020/12/9 23:09
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    Logger log = LoggerFactory.getLogger("System.Crash");

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        log.error("===========默认异常拦截器===========");
        log.error("系统异常拦截. {}", throwable.getMessage());
        log.error("=================================");
    }

}

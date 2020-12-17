package blxt.qjava.autovalue.inter;

import java.lang.annotation.*;

/**
 * 自动注入后, 自动运行方法标记
 * 1. 需要自动run的类,需要添加@Component注解
 * 2. @run的方法,必须要是public的
 * 3. 在ComponentScan和ConfigurationScan之后运行,保证配置文件和自动注入加载完成
 * 4. demo:  @Run("value1str=hellow, valve2=123"), 用','隔开
 * @Author: Zhang.Jialei
 * @Date: 2020/12/4 12:37
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Run {
    /** 字符串入参 */
    String value() default "";
    /** 睡眠运行,毫秒 */
    int sleepTime() default 0;
}

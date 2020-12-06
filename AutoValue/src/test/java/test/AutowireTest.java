package test;

import blxt.qjava.autovalue.AutoObject;
import blxt.qjava.autovalue.AutoValue;
import blxt.qjava.autovalue.ObjectPool;
import blxt.qjava.autovalue.QJavaApplication;
import test.util.AutowireEntry;

import java.io.IOException;


/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/5 22:41
 */
public class AutowireTest {

    public static void main(String[] args) throws  IllegalAccessException, Exception {

        // Configuration扫描, 实现@Value
        AutoValue.init(test.class);
        AutoValue.scan(test.class.getPackage().getName());
        // Component扫描,实现 @Autowired
        AutoObject.autoWiredScan("test");


        // 测试效果
        AutowireEntry autowireEntry = (AutowireEntry)ObjectPool.getObject(AutowireEntry.class);

        AutoValue.autoVariable(autowireEntry.appConfiguration);

        System.out.println(autowireEntry.toString());
    }
}

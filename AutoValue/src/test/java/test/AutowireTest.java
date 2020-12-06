package test;

import blxt.qjava.autovalue.AutoValue;
import blxt.qjava.autovalue.ObjectPool;
import blxt.qjava.autovalue.QJavaApplication;
import blxt.qjava.autovalue.inter.ComponentScan;
import blxt.qjava.autovalue.inter.ConfigurationScan;
import test.util.AutowireEntry;



/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/5 22:41
 */
@ConfigurationScan("test.util")
@ComponentScan("test.util")
public class AutowireTest {

    public static void main(String[] args) throws Exception {

        QJavaApplication.run(AutowireTest.class);

        // Configuration扫描, 实现@Value
        //AutoValue.init(test.class);
        //AutoValue.scan(test.class.getPackage().getName());
        // Component扫描,实现 @Autowired
        //AutoObject.autoWiredScan("test");

        // 测试效果
        AutowireEntry autowireEntry = (AutowireEntry)ObjectPool.getObject(AutowireEntry.class);

        System.out.println(autowireEntry.toString());
    }
}

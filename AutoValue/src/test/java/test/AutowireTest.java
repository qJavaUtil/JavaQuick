package test;

import blxt.qjava.autovalue.QJavaApplication;
import blxt.qjava.autovalue.autoload.AutoValue;
import blxt.qjava.autovalue.inter.ComponentScan;
import blxt.qjava.autovalue.inter.ConfigurationScan;
import blxt.qjava.autovalue.util.ObjectPool;
import test.util.AutowireEntry;

import java.io.File;
import java.io.FileInputStream;


/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/5 22:41
 */
@ConfigurationScan("test")
@ComponentScan("test")
public class AutowireTest {

    public static void main(String[] args) throws Exception {


        // 在android中,可以使用指定的配置文件 ,使用文件流, 便于适配不同文件系统
        //FileInputStream inputStream = new FileInputStream(new File("E:\\Documents\\workspace\\java\\Stpringcloud\\" +
        //              "JavaQuick\\AutoValue\\src\\main\\resources/application2.properties"));
        //AutoValue.setPropertiesFile(inputStream), "utf-8");

        QJavaApplication.run(AutowireTest.class);

        // 测试效果
        AutowireEntry autowireEntry = (AutowireEntry)ObjectPool.getObject(AutowireEntry.class);

        System.out.println(autowireEntry.toString());
    }
}

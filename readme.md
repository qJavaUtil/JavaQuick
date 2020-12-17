
## quickJava 注解框架使用说明 

> quick注解框架起源于一些项目需求, 无法使用springboot注解, 于是在非工作时间，抽空动撸了这个注解框架, 实现@Value和@AutoWire。
> 由于本人还是一个android爱好者，所以对android做了简单适配。本项目可以用户android。
> 1年java小白，欢迎指正和批评。

## 功能 
* 支持 @Component()
* 支持 @Configuration()
* 支持 @ConfigurationProperties() 
* 支持 @PropertySource()
* 支持 @Value()
* 支持 @Autowired() 
* 支持 @Run() 自动运行方法 

基本用法和spring-boot里一样,具体使用见下文的使用步骤,
更详细的,可以看test源码。

## 运行步骤 

1. 先扫描@Configuration, 装载配置, @Value()
2. 再扫描@Component(), 实现@Autowired(), 装载实体类 
3. 再重复扫描@Component(), 实现@Run(), 运行初始方法 

## 使用步骤 

1. 启动类 
```java

@ConfigurationScan("test.util") // 配置文件扫描包地址 
@ComponentScan("test.util") // 自动注入扫描包地址，支持多个包，用逗号隔开 
public class AutowireTest{

    public static void main(String[] args) throws Exception {

        // 方式1 由框架自动加载
        QJavaApplication.run(AutowireTest.class);

        // 方式2 手动实现扫描 
        // //Configuration扫描, 实现@Value
        // AutoValue.init(test.class);
        // AutoValue.scan(test.class.getPackage().getName());
        // // Component扫描,实现 @Autowired
        // AutoObject.autoWiredScan("test");
 
        // 获取自动加载的类, 并检验注解效果 
        AutowireEntry autowireEntry = (AutowireEntry)ObjectPool.getObject(AutowireEntry.class);
         
        System.out.println(autowireEntry.toString());
    }
}
```

2. 配置文件,实现@Value 
如果需要实现自动扫描, 需要天机@Configuration注解, 可以自定义properties文件路径,支持相对路径和绝对路径 
```java

package test.util;

import blxt.qjava.blxt.qjava.autovalue.inter.*;

/***
 * 插件通用配置
 * @author 张家磊
 */
//@Configuration("../src/test.Test1/resources/application.properties")
@Configuration
@Component()
public class AppConfiguration   {

    @Value("test.string_t")
    private String string_t = "Default";

    @Value("test.int_t" )
    private int int_t;

    @Value("test.float_t" )
    private float float_t ;
    @Value("test.double_t" )
    private double double_t ;
    @Value("test.boolean_t" )
    private boolean boolean_t ;
    @Value("test.long_t" )
    private long long_t ;

    @Autowired
    private Bean1 bean1;

    @Run("value1str=hellow, value2=123")
    public void init(@AliasFor(value="value1str")String value1, @AliasFor(value="value2") int value2, Bean1 bean1){
        System.out.println("@Run 自动运行");
    }


    @Override
    public String toString() {
        return "test.Test1.util.AppConfiguration{" +
                "string_t='" + string_t + '\'' +
                ", int_t=" + int_t +
                ", float_t=" + float_t +
                ", double_t=" + double_t +
                ", boolean_t=" + boolean_t +
                ", long_t=" + long_t +
                ", bean1=" + (bean1 == null ? "" : bean1.toString()) +
                '}';
    }
}
 
```

```java
package test.util;

import blxt.qjava.blxt.qjava.autovalue.inter.Autowired;
import blxt.qjava.blxt.qjava.autovalue.inter.Component;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/6 15:59
 */
@Component()
public class Bean1 {
    String name = "Bean1";

    @Autowired
    private Bean2 bean2;

    @Override
    public String toString() {
        return "Bean1{" +
                "name='" + name + '\'' +
                ", bean2=" + bean2.toString() +
                '}';
    }
}
 
```

```java
package test.util;
 
import blxt.qjava.blxt.qjava.autovalue.inter.Component;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/6 15:59
 */
@Component()
public class Bean2 {
    String name = "Bean2";

    @Override
    public String toString() {
        return "Bean1{" +
                "name='" + name + '\'' +
                '}';
    }
}

```

3. 实现@Autowired 
如果需要实现@Autowired,需要实现@Component()。对需要自动注入的类,添加@Autowired,对有@Autowired的类,需要实现@Component。  
```java
package test.util;

import blxt.qjava.blxt.qjava.autovalue.inter.Autowired;
import blxt.qjava.blxt.qjava.autovalue.inter.Component;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/5 22:41
 */
@Component()
public class AutowireEntry {

    @Autowired
    public AppConfiguration appConfiguration;

    @Override
    public String toString() {
        return "AutowireEntry{" +
                "appConfiguration=" + appConfiguration +
                '}';
    }
}

```
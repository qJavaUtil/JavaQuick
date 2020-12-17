package test.util;

import blxt.qjava.autovalue.inter.*;

/***
 * 插件通用配置
 * @author 张家磊
 */
@Component()
@Configuration()
@ConfigurationProperties(prefix = "test", ignoreUnknownFields=true) // 预指定路径,忽略没有配置的属性
@PropertySource(encoding = "utf-8", value="./resources/application2.properties") // 指定properties文件路径和编码
public class AppConfiguration2 {

    private String string_t = "Default";

    private int int_t;

    private float float_t ;
    private double double_t ;
    private boolean boolean_t ;
    @Value("long_t")
    private long long_t ;

    @Autowired
    private Bean1 bean1;

    private long long_t2 ;

    @Override
    public String toString() {
        return "test.test.util.AppConfiguration{" +
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

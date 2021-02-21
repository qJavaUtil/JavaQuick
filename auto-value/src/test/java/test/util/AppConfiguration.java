package test.util;

import blxt.qjava.autovalue.inter.*;

/***
 * 插件通用配置
 * @author 张家磊
 */
//@Configuration("../src/test.test/resources/application.properties")
@Component()
@Configuration()
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

    @Value("test.arrys")
    String arrys[];

    @Autowired
    private Bean1 bean1;

    @Run("value1str=hellow, value2=123")
    public void init(@AliasFor(value="value1str")String value1, @AliasFor(value="value2") int value2, Bean1 bean1){
        System.out.println("@Run 自动运行1" + value1 + "--" +  bean1);
    }

    @Run()
    public void init(){
        System.out.println("@Run 自动运行2");
    }


    @Override
    public String toString() {
        return "test.test.util.AppConfiguration{" +
                "string_t='" + string_t + '\'' +
                ", int_t=" + int_t +
                ", float_t=" + float_t +
                ", arrys=" + arrys +
                ", double_t=" + double_t +
                ", boolean_t=" + boolean_t +
                ", long_t=" + long_t +
                ", bean1=" + (bean1 == null ? "null" : bean1.toString()) +
                '}';
    }
}

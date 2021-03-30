import blxt.qjava.utils.Converter;
import blxt.qjava.utils.PropertiesTools;
import blxt.qjava.utils.check.CheckUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/25 9:41
 */
public class ConvertTest {

    @Test
    public void test() throws IOException {

        String str = "{\n" +
                "   \"mediaServerId\" : \"your_server_id\",\n" +
                "   \"app\" : \"live\",\n" +
                "   \"id\" : \"140186529001776\",\n" +
                "   \"ip\" : \"10.0.17.132\",\n" +
                "   \"params\" : \"token=1677193e-1244-49f2-8868-13b3fcc31b17\",\n" +
                "   \"port\" : 65284,\n" +
                "   \"schema\" : \"rtmp\",\n" +
                "   \"stream\" : \"obs\",\n" +
                "   \"vhost\" : \"__defaultVhost__\"\n" +
                "}";
        String[] param = str.split("=|:");
        param = null;
    }
}

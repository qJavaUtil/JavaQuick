import blxt.qjava.autovalue.inter.*;
import blxt.qjava.qsql.influxdb.InfluxConnection;
import blxt.qjava.qsql.influxdb.InfluxConnectionPool;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户端监听
 */
@Component
@Controller
@RequestMapping("/register")
public class UserClientInfoListener {

    public static final String DATABASE_NAME = "system.statistics.server";
    public static final String DATA_TABLE_NAME = "/sys/device/online";

    @RequestMapping(value = "/mqtt", method = RequestMethod.POST)
    public String Register(HttpExchange httpExchange, String appTage, String hostname, String ip){

       // System.out.println(httpExchange.getRemoteAddress().getAddress());

        Headers responseHeaders = httpExchange.getResponseHeaders();
//        HttpServletRequest requet=ServletActionContext.getRequest();
//        String ipAddress = httpExchange.getPrincipal().getHeader("X-FORWARDED-FOR");
//
//        if (ipAddress == null) {
//            ipAddress = request.getRemoteAddr();
//        }

        InfluxConnection connection = InfluxConnectionPool
                .getInstance()
                .getInfluxConnection(DATABASE_NAME, true);
        java.util.Map<String, String> tags = new HashMap<>();
        Map<String, Object> fields= new HashMap<>();

        tags.put("app", appTage);
        tags.put("remote1", httpExchange.getRemoteAddress().getAddress().toString());
        tags.put("remote2", httpExchange.getRemoteAddress().getAddress().toString());
        fields.put("hostname", hostname);
        fields.put("ip", ip);

        connection.insert(DATA_TABLE_NAME, tags, fields, 0, null);

        return "sucess";
    }

}

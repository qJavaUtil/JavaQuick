package blxt.qjava.httpserver;

import com.alibaba.fastjson.JSONObject;
import blxt.qjava.httpserver.util.ControllerMap;
import blxt.qjava.utils.check.CheckUtils;
import blxt.qjava.utils.Converter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;


/**
 * http的路由分配
 * @Author: Zhang.Jialei
 * @Date: 2020/9/24 11:07
 */
public class QHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        /* 路径分配 */
        URI url =  httpExchange.getRequestURI();
        String urlPath = url.getPath();
        /* 路由查询 */
        ControllerMap controllerMap = AutoControllerRegistry.getInstance().getUrlMap().get(urlPath);
        CheckUtils.objectCheckNull(controllerMap, "没有找到路径", "1001", "");

        /* 实例化对象 */
        Class objClass = controllerMap.getController();
        Object object = null;
        String creator = controllerMap.getCreator();
        try {
            if(creator != null){
                /* 通过指定方法,实例化对象 */
                Method m2 = objClass.getDeclaredMethod(creator);
                object =  m2.invoke(objClass);
            }
            else{
                /* 通过构造方法,实例化对象 */
                Constructor constructor = objClass.getConstructor();
                object = constructor.newInstance();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        /* 获取参数 */
        String s1 = url.getQuery();
        JSONObject jsonObject = Converter.urlParam2Json(s1);
        Map<String, Object> map = Converter.toMap(jsonObject);

        /* 反射方法,并执行(带参) */
        String methodName = controllerMap.getMethodName();
        String response = "";
        try {
            Method m2 = objClass.getDeclaredMethod(methodName, Map.class);
            response = (String) m2.invoke(object, map);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        /* 结果返回 */
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }


}

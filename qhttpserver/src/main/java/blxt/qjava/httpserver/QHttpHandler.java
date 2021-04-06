package blxt.qjava.httpserver;

import blxt.qjava.autovalue.autoload.AutoRestController;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.util.ObjectValue;
import com.alibaba.fastjson.JSONObject;
import blxt.qjava.httpserver.util.ControllerMap;
import blxt.qjava.utils.check.CheckUtils;
import blxt.qjava.utils.Converter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * http的路由分配
 * @Author: Zhang.Jialei
 * @Date: 2020/9/24 11:07
 */
public class QHttpHandler implements HttpHandler {
    static Logger logger = LoggerFactory.getLogger(QHttpHandler.class);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        int error = 0;
        /* 路径分配 */
        URI url =  httpExchange.getRequestURI();
        String urlPath = url.getPath();
        /* 路由查询 */
        ControllerMap controllerMap = AutoRestController.urlMap.get(urlPath);
        if(CheckUtils.isEmpty(controllerMap)){
            // 没有找到路径
            error = 1;
        }

        /* 实例化对象 */
        Class objClass = controllerMap.getController();
        Object bean = ObjectPool.getObject(objClass);

        /* 参数类型 */
        Class<?> parameterTypes[] = controllerMap.getParameterTypes();
        /* 参数默认值 */
        String[] defaultValue = controllerMap.getDefaultValue();
        /* 入参值 */
        Object args[] = new Object[0];
        try {
            args = getValue(httpExchange, bean, url, controllerMap.getParams(), defaultValue, parameterTypes);
        } catch (Exception exception) {
            error = 2;
            exception.printStackTrace();
        }

        String response = "";
        switch (error){
            case 0: /* 反射方法,并执行(带参) */
                String methodName = controllerMap.getMethodName();
                try {
                    Method method = objClass.getDeclaredMethod(methodName, parameterTypes);
                    response = (String) runMethod(bean, method, args);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                break;
            case 1: response = "404";break;
            case 2: response = "405";break;
            default:
        }

        /* 结果返回 */
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    /**
     * 获取函数入参变量值
     * @param httpExchange
     * @param bean            执行对象
     * @param url             url
     * @param params          参数名称
     * @param defaultValue    默认值
     * @param parameterTypes  参数类型
     * @return
     * @throws Exception
     */
    private Object[] getValue(HttpExchange httpExchange,Object bean, URI url, String[] params, String[] defaultValue,Class<?> parameterTypes[]) throws Exception {
        Object args[] = new Object[params.length];
        Map<String, Object> map = new HashMap<>();

        // 从url获取数据
        if(url.getQuery() != null){
            JSONObject jsonObject = Converter.urlParam2Json(url.getQuery());
            map.putAll(Converter.toMap(jsonObject));
        }

        // post,从表单获取数据
        if(httpExchange.getRequestMethod().equals("POST")){
            InputStream inputStream = httpExchange.getRequestBody();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String httpBody = result.toString("UTF-8");
            JSONObject jsonObject = Converter.urlParam2Json(httpBody);
            map.putAll(Converter.toMap(jsonObject));
        }

        // 解析入参
        int i = 0;
        int j = 0;
        for (Class<?>  param : parameterTypes){
            if(param == HttpExchange.class ){
                args[i] = httpExchange;
                map.put("__httpExchange__", httpExchange);
                j++;
            }
            else if(param == Map.class){
                args[i] = map;
                break;
            }
            else if(j >= map.size()){
                continue;
            }
            else{
                if(param.equals(map.get(params[j]).getClass())){
                    args[i] = map.get(params[j]);
                }
                else if(map.get(params[j]) != null){
                    Object object = map.get(params[j]);
                    args[i] = Converter.toObject(object.toString(), param);
                }
                j++;
            }

            if(args[i] == null && defaultValue[i]!= null){
                /* 以$开头的,则从类的属性里面获取.属性可以加@value,实现配置动态加载 */
                if(defaultValue[i].startsWith("$")){
                    String key = defaultValue[i].substring(1);
                    args[i] = ObjectValue.getObjectValue(bean, key, param);
                }
                else{
                    args[i] = Converter.toObject(defaultValue[i], param);
                }
            }
            if(args[i]== null){
                logger.warn("空的URL参数:{}, 参数名:{}", url, params[j - 1]);
            }
            i++;
        }

        return args;
    }

    /**
     * 运行方法
     * @param bean     类
     * @param method  方法
     * @param args    入参参数值
     */
    private Object runMethod(Object bean, Method method, Object args[]){
        try {
            if(CheckUtils.isEmpty(args)){
                return method.invoke(bean);
            }
            else{
                return method.invoke(bean, args);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从表单获取map
     * @param bodyStr
     * @return
     */
    private HashMap<String, String> getMap4body(String bodyStr){
        HashMap<String, String> map = new HashMap<String, String> ();
        String reg = "name\\s*\\=\\s*[\\w\'\"&&[^\\>]]+|value\\s*\\=\\s*[\\w\'\"\\s&&[^\\>]]+";
        Pattern pattern = Pattern.compile (reg);
        Matcher matcher = pattern.matcher (bodyStr);
        while (matcher.find ())
        {
            String group = matcher.group ();
            System.out.println (group);
            String value = group.split ("\\=")[1];
            if (group.startsWith ("name"))
            {
                map.put ("name", value);
            }
            else if (group.startsWith ("value"))
            {
                map.put ("value", value);
            }
        }

        return map;
    }

}

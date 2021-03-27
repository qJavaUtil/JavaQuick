package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.*;
import blxt.qjava.autovalue.reflect.PackageUtil;
import blxt.qjava.utils.check.CheckUtils;
import com.sun.net.httpserver.HttpExchange;

import blxt.qjava.autovalue.inter.RequestMethod;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.httpserver.util.ControllerMap;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * 路由注册
 */
@AutoLoadFactory(name="AutoRestController", annotation = RequestMapping.class, priority = 30)
public class AutoRestController extends AutoLoadBase{

    static String appPath="";
    public final static String separator = "/";
    public static Map<String, ControllerMap> urlMap = new HashMap<String, ControllerMap>();

    @Override
    public <T> T inject(Class<?> object) throws Exception {
        String modelPath = "";
        RequestMapping requestMapping = object.getAnnotation(RequestMapping.class);
        if(requestMapping != null){
            modelPath = getPath(requestMapping.value());
        }

        modelPath = appPath + modelPath;
        T bean = ObjectPool.getObject(object);

        //java反射机制获取所有方法名
        Method[] declaredMethods = object.getDeclaredMethods();
        //遍历循环方法并获取对应的注解名称
        for (Method declaredMethod : declaredMethods) {

            PostMapping methodAnno = declaredMethod.getAnnotation(PostMapping.class);
            GetMapping methodAnno2 = declaredMethod.getAnnotation(GetMapping.class);
            RequestMapping methodAnno3 = declaredMethod.getAnnotation(RequestMapping.class);
            String path = null;
            RequestMethod method = RequestMethod.GET;
            if(methodAnno != null){
                path = getPath(methodAnno.value());
                method = RequestMethod.POST;
            }
            else if(methodAnno2 != null){
                path = getPath(methodAnno2.value());
                method = RequestMethod.GET;
            }
            else if(methodAnno3 != null){
                path = getPath(methodAnno3.value());
                method = methodAnno3.method();
            }

            if (path == null){
                continue;
            }

            String methodPath = modelPath + path;
            // 方法参数类型
            Parameter[] params = declaredMethod.getParameters();
            // 入参变量名称
            String[] realParamNames = PackageUtil.getRealParamNames(declaredMethod);
            String[] defaultValue = new String[realParamNames.length];

            // 遍历解析参数,进行注入
            int i = 0;
            for (Parameter parameter : params) {
                if(parameter.getType() == HttpExchange.class){
                    defaultValue[i] = "%HttpExchange";
                    i++;
                    continue;
                }
                // 获取参数名
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                if(requestParam == null){
                    i++;
                    continue;
                }
                // 绑定默认变量和内部参数
                if(!CheckUtils.isEmpty(requestParam.value())){
                    realParamNames[i] = requestParam.value();
                }
                if(!requestParam.required()){
                    defaultValue[i] = requestParam.defaultValue();
                }
                i++;
            }

            ControllerMap controllerMap = new ControllerMap(object, declaredMethod.getName(), params, realParamNames, defaultValue, method);
            // 注册路由
            urlMap.put( methodPath , controllerMap);
            // 根据对象获取注解值
            System.out.println("Controller, URL: " + methodPath + ",方法:" + declaredMethod.getName() + "," + controllerMap.toString() );
        }

        return bean;
    }


    /**
     * 路径处理
     *
     * @param path
     * @return
     */
    private static String getPath(String path) {

        if(path == null){
            path = "";
        }

        if (!path.startsWith(separator)) {
            path = separator + path;
        }
        if (separator.equals(path)) {
            path = separator;
        }
        return path;
    }
}

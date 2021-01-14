package blxt.qjava.httpserver;

import blxt.qjava.httpserver.inter.*;
import blxt.qjava.httpserver.util.ControllerMap;
import blxt.qjava.utils.check.CheckUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 路由注册器
 * @Author: Zhang.Jialei
 * @Date: 2020/9/29 9:11
 */
public class AutoControllerRegistry {

    /** 单例方法名称 */
    public final static String INSTANCE_METHOD_NAME = "getInstance";
    public final static String separator = "/";

    static AutoControllerRegistry instance;

    static String appPath="";
    /** Controller层路由表 */
    static Map<String, ControllerMap> urlMap = new HashMap<String, ControllerMap>();

    static {
        instance = new AutoControllerRegistry();
    }

    public static AutoControllerRegistry getInstance(){
        return instance;
    }

    public static void init(String appPath){
        AutoControllerRegistry.appPath = getPath(appPath);
    }

    public static void registry(Class<?> bean){
        String modelPath = "";
        // 从类注解RequestMapping中获取值,判断是否自定义模块根路径
        RequestMapping anno =  bean.getAnnotation(RequestMapping.class);
        Controller anno2 = bean.getAnnotation(Controller.class);
        if(anno != null){
            modelPath = getPath(anno.value());
        }else if(anno2 != null){
            modelPath = getPath(anno2.value());
        }

        modelPath = appPath + modelPath;

        // 判断是否是单例类
        String creator = null;
        InstanceMap instanceMap = bean.getAnnotation(InstanceMap.class);
        if(instanceMap != null){
            // 获取标注解的单例方法名称
            String instanceMapValue = instanceMap.value().trim();
            if(CheckUtils.isNotEmpty(instanceMap)){
                creator = instanceMapValue;
            }
        }
        try {
            // 如果没有标记单例,就使用默认的单例
            String srtTmp = creator == null ? INSTANCE_METHOD_NAME : creator;
            bean.getMethod(srtTmp);
            creator = srtTmp;
        } catch (NoSuchMethodException e) {
            creator = null;
        }

        //java反射机制获取所有方法名
        Method[] declaredMethods = bean.getDeclaredMethods();
        //遍历循环方法并获取对应的注解名称
        for (Method declaredMethod : declaredMethods) {

            PostMapping methodAnno = declaredMethod.getAnnotation(PostMapping.class);
            GetMapping methodAnno2 = declaredMethod.getAnnotation(GetMapping.class);
            String path = null;
            if(methodAnno != null){
                path = getPath(methodAnno.value());
            }
            else if(methodAnno2 != null){
                path = getPath(methodAnno2.value());
            }

            if (path == null){
                continue;
            }

            ControllerMap controllerMap = new ControllerMap(bean, declaredMethod.getName(), new String[]{}, creator);
            // 注册路由
            urlMap.put( modelPath + path , controllerMap);
            // 根据对象获取注解值
            //logger.debug("Controller, URL:{}{}, class;{}",  modelPath , path , controllerMap.toString());

        }
    }

    private AutoControllerRegistry(){};

    /**
     * 获取路由注册器
     * @return
     */
    public Map<String, ControllerMap> getUrlMap() {
        return urlMap;
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

package blxt.qjava.autovalue;

import blxt.qjava.autovalue.inter.*;
import blxt.qjava.autovalue.interfaces.AutoLoadBase;
import blxt.qjava.autovalue.util.ConvertTool;
import blxt.qjava.autovalue.util.ObjectPool;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;

import static blxt.qjava.autovalue.util.PackageUtil.getClassName;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/8 13:37
 */
public class AutoMethod extends AutoLoadBase {

    @Override
    public void init(Class<?> rootClass) throws Exception {

    }

    /**
     * 包扫描, 对有Component注释的类, 自动实现 autoWired注解
     * @param packageName  要扫描的包名
     */
    @Override
    public void scan(String packageName) throws Exception {
        List<String> classNames = getClassName(packageName, true);
        if (classNames != null) {
            for (String className : classNames) {
                // 过滤测试类
                if(className.indexOf("test-classes") > 0){
                    className = className.substring(className.indexOf("test-classes") + 13);
                }

                Class<?>  objClass = Class.forName(className);
                Annotation classAnnotation = objClass.getAnnotation(Component.class);
                if(classAnnotation == null){
                    continue;
                }
                inject(objClass);
            }
        }
    }

    /**
     * 注入
     * @param object
     * @throws Exception
     */
    @Override
    public Object inject(Class<?> object) throws Exception {

        Object bean = ObjectPool.getObject(object);

        // 获取f对象对应类中的所有属性域
        Method[] methods = bean.getClass().getMethods();
        for(Method method : methods){
            Run valuename = method.getAnnotation(Run.class);
            if(valuename == null){
                continue;
            }

            // 方法参数类型
            Parameter[] params = method.getParameters();
            // 方法执行入参
            Object args[] = new Object[params.length];

            // 用户输入的参数map
            HashMap<String, String> argsMap = createArgsMap(valuename.value());

            int argsIndex = 0;
            // 遍历解析参数,进行注入
            for (Parameter parameter : params) {
                Class<?> parametertype = parameter.getType();
                Object value = null;

                // 参数别名,从参数池中,获取参数
                AliasFor aliasFor = parameter.getAnnotation(AliasFor.class);
                if(aliasFor != null){
                    String alias = aliasFor.value().trim();
                    String paramValueStr = null;
                    if(!alias.isEmpty()){
                        paramValueStr = argsMap.get(aliasFor.value());
                    }
                    // 这里处理常见类型,由字符串转换int,float等类型
                    if(paramValueStr == null){
                       throw new Exception("拥有@Run注解的方法,方法入参不能为空.可以通过@AliasFor注解添加,或者添加@Component,自动注入. " +
                                           "方法:" + method.toString() + "别名:" + aliasFor.value());
                    }
                    value = ConvertTool.convert(paramValueStr, parametertype);
                }

                // 如果不是常见类型,则从@links{ObjectPool}中获取
                if(value == null){
                    if(ObjectPool.isEmpty(parametertype)){
                        throw new Exception("拥有@Run注解的方法,方法入参不能为空.非基础类型,需要添加@Component,才可以自动注入. " +
                                "方法:" + method.toString() + "别名:" + aliasFor.value());
                    }
                    value = ObjectPool.putObject(parametertype);
                }
                args[argsIndex++] = value;
            }

            // 执行方法
            if(params.length == 0){
                method.invoke(bean);
            }else{
                method.invoke(bean, args);
            }
        }

        return bean;
    }

    /**
     * 获取参数Map
     * @param args
     * @return
     */
    private HashMap<String, String> createArgsMap(String args){
        HashMap<String, String> map = new HashMap<>();

        if(args != null && !args.trim().isEmpty()){
            String[] values = args.split(",");
            for(String v : values){
                if(v == null || v.trim().isEmpty()){
                    continue;
                }
                String vs[] = v.split("=");
                map.put(vs[0].trim(), vs[1].trim());
            }
        }
        return map;
    }

    @Override
    public String getScanPackageName(Class<?> objClass) {
        ComponentScan annotation = objClass.getAnnotation(ComponentScan.class);
        if (annotation == null) {
            return null;
        }
        return annotation.value();
    }
//
//    public static void eachCfg(Class Initclass,String taskType){
//
//        Field[] fields = Initclass.getDeclaredFields();   //获取属性
//        try {
//            for (Field field : fields) {
//                field.setAccessible(true);
//                if(field.getType().toString().endsWith("java.lang.String") && Modifier.isStatic(field.getModifiers())){}
//             if(field.get(TicketGetKeyPojo.class) == null){
//              String attrname = field.getName();
//              attrname = attrname.toLowerCase();
//              Object [] paras =  {attrname,taskType};
//          //调用getDeclaredMethod方法时
//          //参数1：调用改类的方法名称
//          //参数2：参数列表1中的参数类型
//          //参数3：参数列表中2的参数类型
//          //getSimpleName 方法获得不带路径的类名称
//              Method method = Initclass.getDeclaredMethod("check"+Initclass.getSimpleName(),String.class,String.class);
//             //invoke方法
//          //参数1：类的实例方法
//          //参数2：调用上面的方法的参数值（注意顺序）
//          method.invoke(Initclass.newInstance(),paras);
//          }
//      }
//        }
//    } catch (Exception e) {
//        e.printStackTrace( );
//    }
//}

}

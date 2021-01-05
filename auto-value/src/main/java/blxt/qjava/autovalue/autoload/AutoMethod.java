package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.AliasFor;
import blxt.qjava.autovalue.inter.Component;
import blxt.qjava.autovalue.inter.ComponentScan;
import blxt.qjava.autovalue.inter.Run;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ConvertTool;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.util.QThreadpool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/8 13:37
 */
@AutoLoadFactory(name="AutoMethod", annotation = Component.class, priority = 5)
public class AutoMethod extends AutoLoadBase {


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
                // 参数类型
                Class<?> parametertype = parameter.getType();
                // 获取参数值
                Object value = getParameterValue(bean, method, parameter, argsMap, parametertype);
                args[argsIndex++] = value;
            }

            // 放在线程中运行
            if(valuename.sleepTime() > 0){
                QThreadpool.getInstance().schedule(new Runnable(){
                    @Override
                    public void run() {
                        runMethod(bean, method, params, args);
                    }
                }
                , valuename.sleepTime(), TimeUnit.MILLISECONDS);
            }
            else{
                runMethod(bean, method, params, args);
            }

        }

        return bean;
    }

    /**
     * 获取入参变量值
     * 支持常见类型,自定义类和内部变量
     * @param bean
     * @param method
     * @param parameter
     * @param argsMap
     * @param parametertype
     * @return
     * @throws Exception
     */
    public Object getParameterValue(Object bean, Method method,Parameter parameter,
                                    HashMap<String, String> argsMap, Class<?> parametertype ) throws Exception {
        Object value = null;
        // 参数别名,从参数池中,获取参数
        AliasFor aliasFor = parameter.getAnnotation(AliasFor.class);
        if(aliasFor != null){
            String alias = aliasFor.value().trim();
            String paramValueStr = null;
            if(!alias.isEmpty()){
                // 以$开头的,则从类的属性里面获取.属性可以加@value,实现配置动态加载
                if(alias.startsWith("$"))
                {
                    alias = alias.substring(1);
                    Field[] fields = bean.getClass().getDeclaredFields();
                    for(Field field : fields){
                        if(field.getType() != parametertype){
                            throw new Exception(String.format("@AliasFor注解的变量:($%s)和内部参数类型不一致,请检查. 方法:%s, 变量别名:%s",
                                    alias, method.toString(), aliasFor.value()));
                        }
                        if(alias.equals(field.getName())){
                            //打开私有访问
                            boolean accessFlag = field.isAccessible();
                            field.setAccessible(true);
                            //获取属性值
                            value = field.get(bean);
                            field.setAccessible(accessFlag);
                            break;
                        }
                    }
                }
                // 从变量集合里面获取
                else{
                    paramValueStr = argsMap.get(aliasFor.value());
                }
            }
            if(value == null){
                // 这里处理常见类型,由字符串转换int,float等类型
                if(paramValueStr == null){
                    throw new Exception("拥有@Run注解的方法,方法入参不能为空.可以通过@AliasFor注解添加,或者添加@Component,自动注入. " +
                            "方法:" + method.toString() + "别名:" + aliasFor.value());
                }
                value = ConvertTool.convert(paramValueStr, parametertype);
            }
        }

        // 如果不是常见类型,则从@links{ObjectPool}中获取
        if(value == null){
            if(ObjectPool.isEmpty(parametertype)){
                throw new Exception("拥有@Run注解的方法,方法入参不为空的.非基础类型,需要添加@AliasFor,才可以自动绑定参数. " +
                        "方法:" + method.toString());
            }
            value = ObjectPool.getObject(parametertype);
        }

        return value;
    }


    /**
     * 运行方法
     * @param bean     类
     * @param method  方法
     * @param params  参数类型
     * @param args    参数值
     */
    private void runMethod( Object bean, Method method, Parameter[] params, Object args[]){
        try {
            if(params.length == 0){
                method.invoke(bean);
            }else{
                method.invoke(bean, args);
            }
        }catch (Exception e){

        }
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


}

package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.Bean;
import blxt.qjava.autovalue.inter.Component;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ObjectPool;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

/**
 * 自动装载对象
 * @Author: Zhang.Jialei
 * @Date: 2020/12/4 12:34
 */
@AutoLoadFactory(name="AutoBean", annotation = Component.class, priority = 2)
public class AutoBean extends AutoMethod {

    /**
     * Autowired 注解注册
     *
     * @param object  class class的构造函数必须是public的
     * @return 初始化后的实例对象
     */
    @Override
    public <T> T  inject(Class<?> object) throws Exception {
        T bean = ObjectPool.getObject(object);

        // 获取f对象对应类中的所有属性域
        Method[] methods = bean.getClass().getMethods();
        for(Method method : methods){
            Bean valuename = method.getAnnotation(Bean.class);
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

            // 运行
            Class classz = method.getReturnType();
            Object retuenobj = runMethod(bean, method, params, args);
            if(!classz.equals(Void.class)){
                ObjectPool.putObject(method.getReturnType(), retuenobj);
            }
        }
        ObjectPool.upObject(object, bean);
        return bean;
    }


}

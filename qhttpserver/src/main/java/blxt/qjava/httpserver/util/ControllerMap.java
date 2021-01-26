package blxt.qjava.httpserver.util;

import blxt.qjava.autovalue.inter.RequestMethod;

import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/29 9:15
 */
public class ControllerMap {
    /** 类 */
    Class<?> controller;
    /** 方法 */
    String methodName;
    /** 参数类型 */
    Class<?> parameterTypes[];
    /** 参数名 */
    String[] params;
    /** 默认值 */
    String[] defaultValue;
    /** http接口类型 */
    RequestMethod method;

    public ControllerMap(Class<?> controller, String methodName, Parameter[] parameters, String[] paramsNames,String[] defaultValue, RequestMethod method) {
        this.controller = controller;
        this.methodName = methodName;
        this.method = method;
        this.params = paramsNames;
        this.defaultValue = defaultValue;

        parameterTypes = new Class<?>[parameters.length];
        int i = 0;
        /* 参数变量名称 */
        for (Parameter  parameter : parameters){
            parameterTypes[i++] = parameter.getType();
        }

    }

    public Class<?> getController() {
        return controller;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public String[] getParams() {
        return params;
    }

    public String[] getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return "ControllerMap{" +
                "methodName='" + methodName + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}

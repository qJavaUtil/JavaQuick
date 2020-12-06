package blxt.qjava.httpserver.util;

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
    /** 参数 */
    String[] params;

    /** 实例化方法名,如果null,就使用默认构造方法 */
    String creator;

    public ControllerMap(Class<?> controller, String methodName, String[] params, String instance) {
        this.controller = controller;
        this.methodName = methodName;
        this.params = params;
        this.creator = instance;
    }

    public Class<?> getController() {
        return controller;
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getParams() {
        return params;
    }

    public String getCreator() {
        return creator;
    }

    @Override
    public String toString() {
        return "ControllerMap{" +
                "methodName='" + methodName + '\'' +
                ", params=" + Arrays.toString(params) +
                ", creator='" + creator + '\'' +
                '}';
    }
}

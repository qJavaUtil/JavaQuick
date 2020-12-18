package blxt.qjava.autovalue.interfaces;

/**
 * 注解框架接口
 * @Author: Zhang.Jialei
 * @Date: 2020/12/7 11:57
 */
public interface AutoLoad {

    /**
     * 注解包扫描
     * @param packageName  扫描包路径
     * @throws Exception
     */
    void scan(String packageName) throws Exception;

    /**
     * 注入实现
     * @param object
     * @throws Exception
     */
    Object inject(Class<?> object) throws Exception;
}

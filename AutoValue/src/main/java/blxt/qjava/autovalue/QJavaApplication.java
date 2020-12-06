package blxt.qjava.autovalue;

import blxt.qjava.autovalue.inter.ComponentScan;
import blxt.qjava.autovalue.inter.Configuration;
import blxt.qjava.autovalue.inter.ConfigurationScan;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import static blxt.qjava.autovalue.PackageUtil.getClassName;

/**
 * qjava框架注释自动加载启动类
 *
 * @Author: Zhang.Jialei
 * @Date: 2020/12/6 16:32
 */
public class QJavaApplication {

    /**
     * 开始实现注解
     *
     * @param object 启动类,建议启动类在根包路径下,否则需要手动添加扫描路径
     * @throws Exception
     */
    public static void run(Class<?> object) throws Exception {

        if (object.getPackage() == null) {
            throw new Exception("启动类必须拥有包名");
        }

        // Configuration扫描, 实现@Value
        ConfigurationScan(object);
        // Component扫描,实现 @Autowired
        autoWiredScan(object);

    }

    /**
     * Configuration扫描, 实现@Value
     *
     * @param object object     启动类,建议启动类在根包路径下,否则需要手动添加扫描路径
     * @throws Exception
     */
    private static void ConfigurationScan(Class<?> object) throws Exception {
        AutoValue.init(object);
        String path = getConfigurationScanPackageName(object);
        if (path != null) {
            /* 指定包扫描 */
            String[] paths = path.split(",");
            for (String p : paths) {
                if (p == null || p.trim().isEmpty()) {
                    continue;
                }
                AutoValue.scan(path);
            }
            return;
        }
        /* 默认包扫描 */
        AutoValue.scan(object.getPackage().getName());
    }

    /**
     * Component扫描,实现 @Autowired
     *
     * @param object object     启动类,建议启动类在根包路径下,否则需要手动添加扫描路径
     * @throws Exception
     */
    private static void autoWiredScan(Class<?> object) throws Exception {
        String path = getComponentScanPackageName(object);
        if (path != null) {
            /* 指定包扫描 */
            String[] paths = path.split(",");
            for (String p : paths) {
                if (p == null || p.trim().isEmpty()) {
                    continue;
                }
                AutoObject.autoWiredScan(p);
            }
            return;
        }
        /* 默认包扫描 */
        AutoObject.autoWiredScan(object.getPackage().getName());
    }

    /**
     * 获取启动类的ConfigurationScan注解路径
     *
     * @param objClass 要扫描的包路径
     * @throws Exception
     */
    private static String getConfigurationScanPackageName(Class<?> objClass) {
        ConfigurationScan annotation = objClass.getAnnotation(ConfigurationScan.class);
        if (annotation == null) {
            return null;
        }
        return annotation.value();
    }

    /**
     * 获取启动类的ComponentScan注解路径
     *
     * @param objClass 要扫描的包路径
     * @throws Exception
     */
    private static String getComponentScanPackageName(Class<?> objClass) {
        ComponentScan annotation = objClass.getAnnotation(ComponentScan.class);
        if (annotation == null) {
            return null;
        }
        return annotation.value();
    }


}

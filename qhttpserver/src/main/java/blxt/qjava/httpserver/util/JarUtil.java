package blxt.qjava.httpserver.util;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/29 16:26
 */
public class JarUtil {

    /**
     * 获取类的真实路径
     * @return
     */
    public static String getPathReal(Class<?> rootClass)
    {
        String path = rootClass.getProtectionDomain().getCodeSource().getLocation().getPath();
        if(System.getProperty("os.name").contains("dows"))
        {
            path = path.substring(1,path.length());
        }
        if(path.contains("jar"))
        {
            path = path.substring(0,path.lastIndexOf("."));
            return path.substring(0,path.lastIndexOf("/"));
        }
        return path;
    }

}

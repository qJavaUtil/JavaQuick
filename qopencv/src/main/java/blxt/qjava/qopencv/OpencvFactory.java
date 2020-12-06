package blxt.qjava.qopencv;


/**
 * opencv 工厂类
 * @Author: Zhang.Jialei
 * @Date: 2020/12/3 15:19
 */
public class OpencvFactory {

    /**
     * 加载dll
     * @param dllPath  opencv的dll库路径
     */
    public static void load(String dllPath){
         System.load(dllPath);
    }

}

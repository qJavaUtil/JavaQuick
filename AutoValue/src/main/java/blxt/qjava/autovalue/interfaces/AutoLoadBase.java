package blxt.qjava.autovalue.interfaces;


import java.util.ArrayList;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/7 13:53
 */
public abstract class AutoLoadBase implements AutoLoad{

    /**
     * 获取启动类的ConfigurationScan注解路径
     *
     * @param objClass 要扫描的包路径
     * @throws Exception
     */
    public abstract String getScanPackageName(Class<?> objClass);


    /**
     * Component扫描,实现 @Autowired
     * @param autoLoad          自动注解接口
     * @param object object     启动类,建议启动类在根包路径下,否则需要手动添加扫描路径
     * @throws Exception
     */
    public void packageScan(AutoLoad autoLoad, Class<?> object) throws Exception {
        String path = getScanPackageName(object);
        autoLoad.init(object);
        if (path != null) {
            /* 指定包扫描 */
            ArrayList<String> arrayList = getValuesSplit(path, ",");
            for (String p : arrayList) {
                autoLoad.scan(p);
            }
            return;
        }
        /* 默认包扫描 */
        autoLoad.scan(object.getPackage().getName());
    }


    /**
     * 获取注解中的参数列表
     * @param str
     * @param var1
     * @return
     */
    private ArrayList<String> getValuesSplit(String str, String var1){
        String[] paths = str.split(var1);
        ArrayList<String> arrayList = new ArrayList();
        for (String p : paths) {
            if (p == null || p.trim().isEmpty()) {
                continue;
            }
            arrayList.add(p);
        }
        return arrayList;
    }
}

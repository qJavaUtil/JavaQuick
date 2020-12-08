package blxt.qjava.qthread;

import blxt.qjava.properties.PropertiesReader;

import java.io.File;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置工厂
 * @Author: Zhang.Jialei
 * @Date: 2020/9/17 13:40
 */
public class QTreadFactory extends PropertiesReader {

    /**
     * 默认的配置读取工具
     */
   // static PropertiesFactory propertiesFactory;

    /** 核心线程池数量 */
    private static final String THREAD_COREPOOLSIZE = "thread.corePoolSize";
    /** 最大线程池数量 */
    private static final String THREAD_MAXIMUMPOOLSIZE = "thread.maximumPoolSize";
    /** 线程最大的空闲存活时间 */
    private static final String THREAD_KEEPALIVETIME = "thread.keepAliveTime";
    /** 拒绝策略 */
    private static final String THREAD_REJECTED = "thread.rejected";

    final static String ABORT = "ABORT";
    final static String CALLERRUNS = "CALLERRUNS";
    final static String DISCARD = "DISCARD";
    final static String DISCARDOLD = "DISCARDOLD";

    /** 默认配置文件 */
    private static String filePath[] = new String[]{
            "resources/config/thread-dfault.properties",
            "resources/thread-dfault.properties",
            "thread-dfault.properties",
            "../thread-dfault.properties",
            "../../thread-dfault.properties",
            "../../../thread-dfault.properties"};


    /**
     * 默认加载
     */
    public static void creatDefaultPool(){
        String webRootPath = null;
        // 从根开始计算相对路径
        webRootPath = getPath(QTreadFactory.class);
        // 依次找到默认配置文件
        QTreadFactory qTreadFactory = null;
        for(String path : filePath){
            File file = new File(webRootPath + File.separator + path);
            if (file.exists()){
                qTreadFactory = new QTreadFactory(file.getParentFile());
                break;
            }
        }

        if(qTreadFactory == null){
            System.out.println("默认线城池配置文件不存在:" + webRootPath);
           return;
        }
        if(!qTreadFactory.check()){
            System.out.println("默认线城池配置文件错误:" + webRootPath);
            return;
        }
        //System.out.println("默认线城池配置文件成功");
        QThreadpool.newInstance(qTreadFactory.getCorePoolSize(),
                qTreadFactory.getMaximumPoolSize(),
                qTreadFactory.getKeepAliveTime(),
                qTreadFactory.getRejected(),
                false);
    }

    /**
     * 用户通过配置文件初始化线程池方法
     * @param configFilePath        配置文件的文件夹路径
     */
    public static QThreadpool createThreadPool(File configFilePath){
        QTreadFactory qTreadFactory = new QTreadFactory(configFilePath);

        if(!qTreadFactory.check()){
            return null;
        }

        return QThreadpool.newInstance(qTreadFactory.getCorePoolSize(),
               qTreadFactory.getMaximumPoolSize(),
                qTreadFactory.getKeepAliveTime(),
                qTreadFactory.getRejected(),
               true);
    }

    public QTreadFactory(File configFilePath) {
        super(configFilePath);
    }


    @Override
    public String getFilename() {
        return "thread-dfault.properties";
    }


    /**
     * 检查配置缺失
     * @return
     */
    public boolean check(){

        if (!readPropertiesFromFile()) {
            return false;
        }

        if (!validateConfiguration()) {
            return false;
        }
        return true;
    }

    /**
     * 合法性检查
     * @return
     */
    public boolean validateConfiguration() {
        int countError = 0;

        countError += checkMandatoryProperty(THREAD_COREPOOLSIZE);
        countError += checkMandatoryProperty(THREAD_MAXIMUMPOOLSIZE);

        if (countError != 0){
            return false;
        }

        return countError == 0;
    }

    /**
     * 获取核心线程数
     * @return
     */
    public int getCorePoolSize(){
        int defaultValue = Runtime.getRuntime().availableProcessors();
        return validateIntProperty(THREAD_COREPOOLSIZE, defaultValue, false, false);
    }

    /**
     * 获取最大线程数
     * @return
     */
    public int getMaximumPoolSize(){
        int defaultValue = Runtime.getRuntime().availableProcessors() * 2;
        return validateIntProperty(THREAD_MAXIMUMPOOLSIZE, defaultValue, false, false);
    }

    /**
     * 线程最大的空闲存活时间
     * @return
     */
    public int getKeepAliveTime(){
        int defaultValue = 0;
        return validateIntProperty(THREAD_KEEPALIVETIME, defaultValue, true, false);
    }

    /**
     * 获取拒绝策略
     * @return
     */
    public RejectedExecutionHandler getRejected(){
        String key = validateStringProperty("THREAD_REJECTED", ABORT);
        return getRejected(key);
    }


    /**
     * 获取拒绝策略
     * @param key
     * @return
     */
    public static RejectedExecutionHandler getRejected(String key){
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        key = key.toUpperCase();

        if(ABORT.equals(key)){
            handler = new ThreadPoolExecutor.AbortPolicy();
        }else if(CALLERRUNS.equals(key)){
            handler = new ThreadPoolExecutor.CallerRunsPolicy();
        }else if(DISCARD.equals(key)){
            handler = new ThreadPoolExecutor.DiscardPolicy();
        }else if(DISCARDOLD.equals(key)){
            handler = new ThreadPoolExecutor.DiscardOldestPolicy();
        }
        return handler;
    }

    /**
     * 获取jar运行路径
     *
     * @return
     */
    public static String getPath(Class<?> objClass) {
        String path = objClass.getClassLoader().getResource("").getPath();
        if (System.getProperty("os.name").contains("dows")) {
            path = path.substring(1);
        }
        if (path.contains("jar")) {
            path = path.substring(0, path.lastIndexOf("."));
            return path.substring(0, path.lastIndexOf("/"));
        }
        return path.replace("/classes/", "")
                .replace("/test-classes/", "")
                .replace("/target", "/src/test");
    }


}

package blxt.qjava.qexecute;

import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * cmd 命令执行工具
 * 需要先初始化
 * @author ZhangJieLei
 */
@Data
public class Executer {
    String tag = "defult";
    Process process = null;
    PrintWriter writer = null;
    String CODE = "gbk";
    int cacheSize = 1024;
    String basepath;

    InputStreamThread insR;
    InputStreamThread onsR;

    /** 长时间没有数据交互时, 自动断开链接 */
    boolean autoClose = false;
    /** 交互超时 30 s */
    long timeFreeMax = 30000 * 1000;


    /**
     * 初始化工作目录
     * @param basePath  处室目录
     * @return
     */
    public boolean init(String basePath){
        this.basepath = basePath;
        try {
            process = Runtime.getRuntime().exec("cmd", null, new File(basePath));
        } catch (IOException e) {
            process = null;
            e.printStackTrace();
            return false;
        }
        if(process != null) {
            writer = new PrintWriter(process.getOutputStream());
        }
        return true;
    }

    /**
     * 添加接收器
     * @param callBack  控制台监听回调
     */
    public void addReaderStream(InputStreamThread.CallBack callBack){
        InputStreamThread insR = new InputStreamThread(process.getInputStream(), callBack);
        InputStreamThread onsR = new InputStreamThread(process.getErrorStream(), callBack);
        insR.setTag(tag);
        onsR.setTag(tag);
        insR.setCacheSize(cacheSize);
        insR.setCODE(CODE);
        onsR.setCacheSize(cacheSize);
        onsR.setCODE(CODE);

        insR.setAutoClose(autoClose);
        insR.setTimeFreeMax(timeFreeMax);

        new Thread(insR).start();
        new Thread(onsR).start();
    }


    /**
     * 执行命令
     * @param cmd
     * @return
     */
    public boolean exec(String cmd){
        if(process == null) {
            return false;
        }
        writer = new PrintWriter(process.getOutputStream());
        writer.println(cmd);
        writer.flush();
       // writer.close();
        return true;
    }


    /**
     * 关闭
     * @return
     */
    public boolean close(){

        if(insR != null){
            insR.close();
        }
        if(onsR != null){
            onsR.close();;
        }
        if(process != null) {
            process.destroy();
        }
        if(writer != null) {
            writer.close();
        }

        return true;
    }

    public static boolean exec(String cmd, String basePath){
        try {
            Runtime.getRuntime().exec(cmd, null, new File(basePath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}

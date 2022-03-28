package blxt.qjava.qexecute;

import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * cmd 命令执行工具
 * 建议使用: @Link{ExecuterFactory}
 * @author ZhangJieLei
 */
@Data
@Deprecated
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
     * 设置工作目录 和启动指令
     * 用于执行第三方程序, 关闭进程时, 子进程也会关闭
     * @param directory
     * @param commands
     * @return
     */
    public boolean init(File directory, List<String> commands){
        ProcessBuilder pb =new ProcessBuilder(commands);
        pb.directory(directory);

        try {
            process = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        writer = new PrintWriter(process.getOutputStream());
        return true;
    }
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
        insR = new InputStreamThread(process.getInputStream(), callBack);
        onsR = new InputStreamThread(process.getErrorStream(), callBack);
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
     * 设置异常数据回调
     * @param callBack
     */
    public void setErrorCallBack(InputStreamThread.CallBack callBack){
        onsR.setCallBack(callBack);
    }


    /**
     * 执行
     * @return
     */
    public boolean exec(){
        writer.flush();
        return true;
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
        return true;
    }

//    /**
//     * 等待结果
//     * @return
//     */
//    public int wait(){
//        try {
//            return process.waitFor();
//        } catch (InterruptedException e) {
//            return -1;
//        }
//    }

    public boolean exec(byte[] cmd){
        if(process == null) {
            return false;
        }
        writer = new PrintWriter(process.getOutputStream());
        writer.println(cmd);
        writer.flush();
        return true;
    }

    public boolean exec(int cmd){
        if(process == null) {
            return false;
        }
        writer = new PrintWriter(process.getOutputStream());
        writer.println(cmd);
        writer.flush();
        return true;
    }

    public boolean exec(char cmd){
        if(process == null) {
            return false;
        }
        writer = new PrintWriter(process.getOutputStream());
        writer.println(cmd);
        writer.flush();
        return true;
    }

    public boolean write(int cmd){
        if(process == null) {
            return false;
        }
        writer = new PrintWriter(process.getOutputStream());
        writer.write(cmd);
        writer.flush();
        return true;
    }

    public boolean write(char cmd){
        if(process == null) {
            return false;
        }
        writer = new PrintWriter(process.getOutputStream());
        writer.write(cmd);
        writer.flush();
        return true;
    }

    public boolean write(char[] cmd){
        if(process == null) {
            return false;
        }
        writer = new PrintWriter(process.getOutputStream());
        writer.write(cmd);
        writer.flush();
        return true;
    }

    public boolean write(String cmd){
        if(process == null) {
            return false;
        }
        writer = new PrintWriter(process.getOutputStream());
        writer.write(cmd);
        writer.flush();
        return true;
    }

    /**
     * 关闭
     * @return
     */
    public boolean close(){

        if(process != null){
            process.destroyForcibly();
            process.destroy();
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

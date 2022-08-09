package blxt.qjava.qexecute;


import java.io.IOException;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * java调用cmd指令工具工厂
 *
 * @author ZhangJieLei
 */
public class ExecuterFactory {
    /**
     * 页面编码
     */
    private static final String DEFAULT_LANGUAGE_CODE = "936";

    /**
     * window系统默认语言:GBK
     */
    private static final String DEFAULT_LANGUAGE = "GBK";

    /** Process 构建方式 */
    ProcessEumu processEumu = ProcessEumu.ProcessBuilder;
    /** 执行线程 */
    Process process = null;
    /** 指令输入对象 */
    PrintWriter writer = null;
    /** 启用输出流监听 */
    Boolean isInputStream = true;

    /** 输出 编码 */
    String code = getsystemLanguage();
    /** 出书语言码 */
    String languageCode = DEFAULT_LANGUAGE_CODE;

    /** 工作路径 */
    File workPath = null;
    /** 工作标记 */
    String tag = "defult";
    Boolean onWait = true;
    /** 回调监听 */
    InputStreamThread.CallBack callBack = null;
    /** 输出文件保存 和 isInputStream 互斥*/
    File redirectOutput = null;

    /** 系统类型. */
    ExecuterType executerType;

    public ExecuterFactory(){
        executerType = ExecuterType.getExecuterType();
    }

    /**
     * 设置系统类型.
     * @param executerType ExecuterType
     * @return ExecuterFactory
     */
    public ExecuterFactory executerType(ExecuterType executerType) {
        this.executerType = executerType;
        return this;
    }

    /**
     * 设置默认工作路径.
     * @param workPath  工作路径
     * @return ExecuterFactory
     */
    public ExecuterFactory workPath(File workPath) {
        this.workPath = workPath;
        return this;
    }

    /**
     * 设置默认工作路径.
     * @param workPath  工作路径
     * @return ExecuterFactory
     */
    public ExecuterFactory workPath(String workPath) {
        return workPath(new File(workPath));
    }

    /***
     * 设置输出记录
     * @param redirectOutput
     * @return
     */
    public ExecuterFactory redirectOutput(String redirectOutput){
        return redirectOutput(new File(redirectOutput));
    }

    /**
     * 设置输出记录
     * @param redirectOutput
     * @return
     */
    public ExecuterFactory redirectOutput(File redirectOutput){
        this.redirectOutput = redirectOutput;
        return this;
    }


    /**
     * 设置回显监听
     * @param callBack 监听接口
     * @return ExecuterFactory
     */
    public ExecuterFactory callBack(InputStreamThread.CallBack callBack){
        this.callBack = callBack;
        return this;
    }

    /**
     * 设置编码
     * @param code 编码GBK, UTF-8
     * @return
     */
    public ExecuterFactory code(String code) {
        this.code = code;
        return this;
    }

    /**
     * 设置chcp 编码
     * @param languageCode 默认 GBK:936
     * @return
     */
    public ExecuterFactory languageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    /**
     * 设置执行器标签
     * @param tag
     * @return
     */
    public ExecuterFactory tag(String tag) {
        this.tag = tag;
        return this;
    }

    /**
     * 执行cmd指令
     * @param cmd 执行指令
     */
    public ExecuterFactory build(String cmd) {
        build(languageCode, false, cmd);
        return this;
    }

    /**
     * 构建可持续输入执行器
     */
    public ExecuterFactory build() {
        build(languageCode, false, "");
        return this;
    }

    /**
     * cmd交互处理窗口
     *
     * @param languageCode 系统语言编码
     * @param isOneRun 只执行cmd指令, 执行后立即关闭
     * @param cmd 执行的指令
     * @see .在中文windows系统中，根据编码需要设置编码 chcp 65001 就是换成UTF-8代码页<br>
     *      chcp 936 可以换回默认的GBK<br>
     *      chcp 437 是美国英语 <br>
     */
    public void build(String languageCode, boolean isOneRun, String cmd) {
        try {
            process = buildProcess(isOneRun, cmd);

            writer = new PrintWriter(process.getOutputStream());

            // 初始化编码
            if(executerType.equals(ExecuterType.Windows)){
                writer.println("chcp " + languageCode);
                writer.flush();
            }

            // 打开输入和异常流
            if(isInputStream){
                ProcessInputStreamThread inputThread = new ProcessInputStreamThread(process.getInputStream());
                ProcessInputStreamThread errorThread = new ProcessInputStreamThread(process.getErrorStream());
                inputThread.setName("InputStreamThread");
                inputThread.start();
                errorThread.setName("ErrorStreamThread");
                errorThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建 Process
     * @param isOneRun  是否执行一次后关闭
     * @param cmd       初始指令
     * @return
     */
    public Process buildProcess(boolean isOneRun, String cmd){
        List<String> cmdBins = new ArrayList<>();
        String cmdBin = "";
        // Windows下Cmd执行指令初始化
        if(executerType.equals(ExecuterType.Windows)){
            cmdBin = "cmd";
            cmdBins.add("cmd");
            if (isOneRun) {
                // 执行完后立即关闭
                cmdBin = "cmd /c ";
                cmdBins.add("/c");
                // 等待关闭
                if(onWait){
                    cmdBin += "start /wait ";
                    cmdBins.add("start");
                    cmdBins.add("/wait");
                }
            }
        }
        else{
            cmdBins.add("pwd");
        }


        try {
            if(cmd != null && !cmd.isEmpty()) {
                cmdBins.add(cmd);
            }
            if(processEumu.equals(ProcessEumu.ProcessBuilder)){
                ProcessBuilder builder = new ProcessBuilder();
                builder = builder.command(cmdBins);
                // 使用输出持久化到本地
                if(redirectOutput != null){
                    builder.redirectError(redirectOutput);
                    builder.redirectOutput(redirectOutput);
                }
                return builder.start();
            }
            else{
                return Runtime.getRuntime().exec(cmdBin + cmd, null,  workPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 执行指令
     * @param cmd 指令
     */
    public void execute(String cmd){
        writer.println(cmd);
        writer.flush();
    }

    /***
     * 执行指令
     * @param cmd 指令
     */
    public void exec(byte[] cmd){
        writer.println(cmd);
        writer.flush();
    }

    /***
     * 执行指令
     * @param cmd 指令
     */
    public void exec(char[] cmd){
        writer.println(cmd);
        writer.flush();
    }

    /**
     * 执行指令
     * @param
     */
    public void execute(){
        writer.flush();
    }


    /**
     * 等待执行完成
     */
    public int waitFor(){
        try {
           return process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 等待执行完成
     * @param time        等待时间
     * @param timeUnit    时间单位
     * @return
     */
    public boolean waitFor(long time, TimeUnit timeUnit){
        try {
            return process.waitFor(time, timeUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭
     */
    public void close(){
        if (null != writer) {
            writer.close();
        }
    }

    /**
     * 独立的执行一个指令.
     * @param cmd      cmd
     * @param basePath 工作路径
     * @param withReturn 是否需要返回结果
     * @return 回显
     */
    public String execStandalone(String cmd, String basePath, Boolean ... withReturn){
        try {
            Process proc = Runtime.getRuntime().exec(cmd, null, basePath == null ? null : new File(basePath));
            if(withReturn == null || withReturn.length == 0 || !withReturn[0]){
                // 等待执行完成
                proc.waitFor();
                return "success";
            }
            InputStream stdIn =  proc.getInputStream();
            InputStream stdOrr =  proc.getErrorStream();
            InputStreamReader isi = new InputStreamReader(stdIn, code);
            InputStreamReader isr = new InputStreamReader(stdOrr, code);
            BufferedReader br = new BufferedReader(isi);
            BufferedReader be = new BufferedReader(isr);
            String line="";
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
                if(callBack!= null){
                    callBack.onReceiver(tag, line);
                }
            }
            while ((line = be.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
                if(callBack!= null){
                    callBack.onReceiver(tag, line);
                }
            }
            return sb.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 输入线程
     * */
    class ProcessInputStreamThread extends Thread {

        InputStream input;
        BufferedReader breader = null;

        ProcessInputStreamThread(InputStream input) {
            this.input = input;
            // 避免出现乱码问题,直接使用系统默认的编码格式
            breader = new BufferedReader(new InputStreamReader(input, Charset.forName(code)));
        }

        @Override
        public void run() {
            try {
                String str = null;
                while ((str = breader.readLine()) != null) {
                    if(callBack != null){
                        callBack.onReceiver(tag, str);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != input) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != breader) {
                    try {
                        breader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(callBack != null){
                    callBack.onOvertime(tag);
                }
            }
        }
    }


    /**
     * 获取操作系统默认语言
     *
     * @return String
     * @see .java虚拟机启动默认的编码(.一般和java文件设置格式一致)
     *      System.out.println(Charset.defaultCharset());<br>
     *      查看预置的变量信息:System.getProperties().list(System.out);<br>
     *      属性:<br>
     *      文件编码:file.encoding<br>
     *      系统默认编码sun.jnu.encoding
     */
    public static String getsystemLanguage() {
        return null == System.getProperty("sun.jnu.encoding") ? DEFAULT_LANGUAGE
            : System.getProperty("sun.jnu.encoding");
    }


    /** Process 构建方式 */
    public enum ProcessEumu{
        /** ProcessBuilder 方式构建 */
        ProcessBuilder,
        /** Runtime 方式构建 */
        Runtime
    }

}


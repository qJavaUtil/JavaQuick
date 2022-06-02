package blxt.qjava.qtelnet;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * telnet客户端
 * @author ZhangJieLei
 */
@Slf4j
@Data
public class QTelnetClient extends TelnetClient {
    public static String LoginMark = "login:";
    public static String PasswdMark = "password:";
    public static String LoginFailedMark = "Login Failed";

    final Object lock = new Object();
    String tag = "default";
    String hostIp = "127.0.0.1";
    int port = 22;
    String username;
    String password;
    boolean isLogin = false;

    /** 结束标识字符串,Windows中是>,Linux中是# */
    private String prompt = "]$";
    /** 结束标识字符 */
    private char promptChar = '$';
    /** 输入流,接收返回信息 */
    private InputStream in;
    /** 向服务器写入 命令 */
    private PrintStream out;
    /**  协议类型 .VT100、VT52、VT220、VTNT、ANSI */
    String termtype = "VT100";
    /** 隐藏控制台颜色 */
    boolean hideColor = false;

    /** 编码转换 */
    private String ORIG_CODEC = "UTF-8";
    private String TRANSLATE_CODEC = "UTF-8";
    boolean isChangeCode = false;

    /** 异步读取线程 */
    ReadThread2 readThread2 = null;
    boolean readThreadRun = true;
    OnTelnetClientListener onTelnetClientListener = null;

    public QTelnetClient() {
        super();
    }

    public QTelnetClient(String termtype) {
        super(termtype);
    }

    /**
     * 连接
     * 某些系统,connect后需要sleep 1秒才能登录
     * @return
     */
    public boolean connect() {
        try {
            super.connect(hostIp, port);
        } catch (IOException e) {
            isLogin = false;
            return false;
        }
        if (onTelnetClientListener != null) {
            onTelnetClientListener.onConnect(tag, true);
        }
        return true;
    }


    /**
     * 关闭连接
     */
    public boolean distinct() {
        // 退出指令
        if(isConnected()){
            write("exit");
        }
        if(readThread2 != null){
            readThreadRun = false;
            readThread2.stop();
            readThread2 = null;
        }
        try {
            if (isConnected()) {
                in.close();
                out.close();
               // disconnect();
                isLogin = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (onTelnetClientListener != null) {
            onTelnetClientListener.onDisConnect(tag, true);
        }
        return true;
    }

    /**
     * 设置结束字符串
     *
     * @param prompt
     */
    public void setPrompt(String prompt) {
        this.prompt = "]" + prompt;
        this.promptChar = prompt.charAt(prompt.length() - 1);
    }


    /**
     * 登录到目标主机
     */
    public boolean login() {

        in = getInputStream();
        out = new PrintStream(getOutputStream());

        // 登录
        if(username != null){
            readUntil(LoginMark);
            write(username);
            readUntil(PasswdMark);
            write(password);
        }

        String rs = readUntil(prompt);
        if (rs.contains(LoginFailedMark)) {
            isLogin = false;
        }else{
            isLogin = true;
        }
        if (onTelnetClientListener != null) {
            onTelnetClientListener.onLogin(tag, isLogin);
        }
        return isLogin;
    }

    /**
     * 发送命令,返回执行结果
     *
     * @param command
     * @return
     */
    public String sendCommandWithReply(String command) {
        try {
            write(command);
            return readUntil(prompt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 发送命令, 没有读数据
     *
     * @param value
     */
    public boolean write(String value) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 发送命令, 没有读数据
     * @param value
     */
    public boolean write(byte[] value) {
        try {
            out.write(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 读取结果.
     * @param pattern pattern
     * @return
     */
    private String readUntil(String pattern) {
        return readUntil(pattern, onTelnetClientListener);
    }

    /**
     * 读取分析结果
     *
     * @param pattern 匹配到该字符串时返回结果
     * @return
     */
    private String readUntil(String pattern, OnTelnetClientListener onTelnetClientListener) {
        StringBuilder sb = new StringBuilder();
        try {
            char lastChar = (char) -1;
            boolean flag = pattern != null && pattern.length() > 0;
            if (flag) {
                lastChar = pattern.charAt(pattern.length() - 1);
            }
            char ch;
            int code = -1;
            while (isConnected() && (code = in.read()) != -1) {
                ch = (char) code;
                sb.append(ch);
                //匹配到结束标识时返回结果
                if (flag) {
                    if (ch == lastChar && sb.toString().endsWith(pattern)) {
                        break;
                    }
                } else {
                    //如果没指定结束标识,匹配到默认结束标识字符时返回结果
                    if (ch == promptChar) {
                        break;
                    }
                }
                //登录失败时返回结果
                if (!isLogin && sb.toString().contains(LoginFailedMark)) {
                    break;
                }
            }
        } catch (Exception e) {
            // 断开连接后, 读取可能会报错
            if(!isConnected()){
                e.printStackTrace();
            }
        }
        // 回复数据
        revertDate(sb, onTelnetClientListener);
        return sb.toString();
    }

    /**
     * 回复数据
     * @param stringBuilder
     */
    private void revertDate(StringBuilder stringBuilder, OnTelnetClientListener onTelnetClientListener){
        if(stringBuilder.length() == 0){
            return;
        }
        if (onTelnetClientListener != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (isChangeCode) {
                        try {
                            onTelnetClientListener.onReceiver(tag, new String(stringBuilder.toString()
                                    .getBytes(ORIG_CODEC), TRANSLATE_CODEC));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        onTelnetClientListener.onReceiver(tag, stringBuilder.toString());
                    }
                }
            }).start();
        }
        else{
            log.debug("{} \n {}", tag, stringBuilder.toString());
        }
    }

    /**
     * 回复数据
     * @param stringBuilder
     */
    private void revertDate(String stringBuilder){
        // 放在线程里面去通知
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (onTelnetClientListener != null) {
                    onTelnetClientListener.onReceiver(tag, stringBuilder);
                }
            }
        }).start();
    }

    /**
     * 回复数据
     * @param c
     */
    private void revertDate(char c){
        // 放在线程里面去通知
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (onTelnetClientListener != null) {
                    onTelnetClientListener.onGetDate(tag, new byte[]{(byte)c});
                }
            }
        }).start();
    }

    /**
     * 设置监听器
     * @param onTelnetClientListener
     */
    public void setOnTelnetClientListener(OnTelnetClientListener onTelnetClientListener) {
        this.onTelnetClientListener = onTelnetClientListener;
    }

    /**
     * 启动读取线程
     */
    public void onReadThread(){
        if(readThread2 == null){
            readThreadRun = true;
            readThread2 = new ReadThread2();
            Thread thread = new Thread(readThread2);
            thread.start();
        }
    }

    /**
     * Telnet监听
     */
    public interface OnTelnetClientListener   {
        void onConnect(final String tag, final boolean isconnect);

        void onDisConnect(final String tag, final boolean isconnect);

        void onLogin(final String tag, final boolean isLogin);

        void onGetDate(final String tag, final byte[] data);

        void onReceiver(final String tag, final String msg);
    }

    class ReadThread2 extends Thread {
        @Override
        public void run() {
            synchronized (lock) {//只能一个读取
                SubReadThread sub = new SubReadThread();
                sub.start();
                int last = sub.count;
                while (readThreadRun && isConnected()) {
                    sub.sleep(50);
                    if (last == sub.count) {
                        // 回复数据
                        String data = sub.getDate();
                        if(data!= null){
                            revertDate(data);
                        }
                    } else {
                        last = sub.count;
                    }
                }
            }
        }

    }

    /**
     * 读取子线程，完成实际读取
     * @author chruan
     *
     */
    class SubReadThread extends Thread {
        int count = 0;
        StringBuilder sb = new StringBuilder();
        /** 缓存锁 */
        boolean lock = false;
        /** 强制输出标记 */
        boolean out = false;

        public void read() {
            char c;
            int code = -1;
            // 强制传输的时候,不进行读取
            while (!out && (code = (inRead())) != -1) {
                // 锁
                lock();
                lock = true;
                count++;
                c = (char) code;
                // 颜色
                if (hideColor && c == '\033') {
                    int code2 = inRead();
                    char cc = (char) code2;
                    count++;
                    if (cc == '[' || cc == '(') {
                    }
                }
                // 控制码
                else if(code < 0x20){
                }
                else{
                    // 字符回显
                    sb.append(c);
                    revertDate(c);
                }
                // 解锁
                lock = false;
                // 遇到换行, 强制传输
                if(c == '\n'){
                    out = true;
                }
            }

        }

        @Override
        public void run() {
            while(readThreadRun){
                read();
            }
            log.warn("停止读取");
        }

        /** 读取缓存, 要做锁 */
        public String getDate(){
            if(sb.length() == 0){
                return null;
            }
            // 锁
            lock();
            lock = true;
            // 读取
            String date = sb.toString();
            // 清除
            sb.delete(0, sb.length());
            count = 0;
            lock = false;
            out = false;
            return date;
        }

        // 锁
        public void lock(){
            while(lock){
                sleep(10);
            }
        }

        private int inRead(){
            try {
                return in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }

        public void sleep(int time){
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}

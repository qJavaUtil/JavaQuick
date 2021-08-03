package blxt.qjava.qtelnet;

import blxt.qjava.autovalue.util.QThreadpool;
import lombok.Data;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * telnet客户端
 * @author ZhangJieLei
 */
@Data
public class QTelnetClient extends TelnetClient {
    public static String LoginMark = "login:";
    public static String PasswdMark = "password:";
    public static String LoginFailedMark = "Login Failed";

    Object lock = new Object();
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
        try {
            if (isConnected()) {
                disconnect();
                isLogin = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (onTelnetClientListener != null) {
            onTelnetClientListener.onDisConnect(tag, true);
        }
        if(readThread2 != null){
            readThread2.stop();
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

        readUntil(LoginMark);
        write(username);

        readUntil(PasswdMark);
        write(password);

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
     * 读取分析结果
     *
     * @param pattern 匹配到该字符串时返回结果
     * @return
     */
    private String readUntil(String pattern) {
        StringBuilder sb = new StringBuilder();
        try {
            char lastChar = (char) -1;
            boolean flag = pattern != null && pattern.length() > 0;
            if (flag) {
                lastChar = pattern.charAt(pattern.length() - 1);
            }
            char ch;
            int code = -1;
            while ((code = in.read()) != -1) {
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
            e.printStackTrace();
        }
        // 回复数据
        revertDate(sb);
        return sb.toString();
    }

    /**
     * 回复数据
     * @param stringBuilder
     */
    private void revertDate(StringBuilder stringBuilder){
        if (onTelnetClientListener != null) {
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
    }

    private void revertDate(String stringBuilder){
        if (onTelnetClientListener != null) {
            onTelnetClientListener.onReceiver(tag, stringBuilder);
        }
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
            readThread2 = new ReadThread2();
            QThreadpool.getInstance().execute(readThread2);
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
                while (true) {
                    sub.sleep(100);
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

        boolean lock = false;

        public void read() {
            try {
                char c;
                int code = -1;
                while ((code = (in.read())) != -1) {
                    // 锁
                    lock();
                    lock = true;
                    count++;
                    c = (char) code;
                    // 颜色
                    if (hideColor && c == '\033') {
                        int code2 = in.read();
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
                    }
                    // 解锁
                    lock = false;
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void run() {
            read();
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
            return date;
        }

        // 锁
        public void lock(){
            while(lock){
                sleep(100);
            }
        }

        public void sleep(int time){
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
            }
        }

    }

}

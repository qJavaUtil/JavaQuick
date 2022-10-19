package com.demo.telnet;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * telnet客户端
 *
 * @author ZhangJieLei
 */
@Slf4j
@Data
public class QTelnetClient2 extends TelnetClient {
    public static String LoginMark = "login:";
    public static String PasswdMark = "password:";
    public static String LOGINFAILEDMARK = "Login Failed";

    final Object lock = new Object();
    String tag = "default";
    String hostIp = "127.0.0.1";
    int port = 23;
    String username;
    String password;
    boolean isLogin = false;

    /** 过滤控制码 */
    boolean isFilterAnsi = false;
    /**
     * 过滤控制颜色
     */
    boolean isFilterColor = false;
    /** 登录输入间隔(极少部分设备需要sleep).*/
    private String loginFailedMark = LOGINFAILEDMARK;
    private int loginSleep = 100;

    /**
     * 结束标识字符串,Windows中是>,Linux中是#
     */
    private String prompt = "]$";
    /**
     * 结束标识字符
     */
    private char promptChar = '$';
    /**
     * 输入流,接收返回信息
     */
    private InputStream in;
    /**
     * 向服务器写入 命令
     */
    private PrintStream out;
    /**
     * 协议类型 .VT100、VT52、VT220、VTNT、ANSI
     */
    String termtype = "VT100";

    /**
     * 使用线程返回
     */
    boolean threadReturn = false;

    /**
     * 编码转换
     */
    private String ORIG_CODEC = "UTF-8";
    private String TRANSLATE_CODEC = "UTF-8";
    boolean isChangeCode = false;

    /**
     * 异步读取线程
     */
    ReadThread readThread = null;
    boolean readThreadRun = true;
    OnTelnetClientListener onTelnetClientListener = null;

    public QTelnetClient2() {
        super();
    }

    public QTelnetClient2(final String termtype) {
        super(termtype);
    }

    /**
     * 连接
     * 某些系统,connect后需要sleep 1秒才能登录
     *
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
        if (isConnected()) {
            write("exit");
        }
        if (readThread != null) {
            readThreadRun = false;
            readThread.stop();
            readThread = null;
        }
        try {
            if (isConnected()) {
                in.close();
                out.close();
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
    public void setPrompt(final String prompt) {
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
        if (username != null) {
            readUntil(LoginMark);
            write(username);
            sleep(loginSleep);
            readUntil(PasswdMark);
            sleep(loginSleep);
            write(password);
            sleep(loginSleep);
        }

        String rs = readUntil(prompt);
        if (rs.toLowerCase(Locale.ROOT).contains(loginFailedMark.toLowerCase(Locale.ROOT))) {
            isLogin = false;
        } else {
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
    public String sendCommandWithReply(final String command) {
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
    public boolean write(final String value) {
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
     *
     * @param value
     */
    public boolean write(final byte[] value) {
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
     *
     * @param pattern pattern
     * @return
     */
    private String readUntil(final String pattern) {
        return readUntil(pattern, onTelnetClientListener);
    }

    /**
     * 读取分析结果
     *
     * @param pattern 匹配到该字符串时返回结果
     * @return
     */
    private String readUntil(final String pattern,final  OnTelnetClientListener onTelnetClientListener) {
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
                if(isFilterAnsi){ // 对回显进行过滤
                    // 颜色
                    if (isFilterColor && ch == '\033') {
                        int code2 = in.read();
                        char cc = (char) code2;
                        if (cc == '[' || cc == '(') {
                            continue;
                        }
                    }
                    // 控制码
                    else if (code < 0x20) {
                        continue;
                    }
                }

                sb.append(ch);
                revertDate(ch);
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
                if (!isLogin && sb.toString().contains(loginFailedMark)) {
                    break;
                }
            }
        } catch (Exception e) {
            // 断开连接后, 读取可能会报错
            if (!isConnected()) {
                e.printStackTrace();
            }
        }
        // 回复数据
        revertDate(sb, onTelnetClientListener);
        return sb.toString();
    }

    /**
     * 回复数据
     *
     * @param stringBuilder
     */
    private void revertDate(final StringBuilder stringBuilder,final  OnTelnetClientListener onTelnetClientListener) {
        if (stringBuilder.length() == 0) {
            return;
        }
        if (onTelnetClientListener == null) {
            // System.out.print(stringBuilder.toString());
            log.debug("{} \n {}", tag, stringBuilder.toString());
            return;
        }
        if (!threadReturn) {
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
            return;
        }

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

    /**
     * 回复数据
     *
     * @param stringBuilder
     */
    private void revertDate(final String stringBuilder) {
        if (onTelnetClientListener == null) {
            return;
        }
        if (!threadReturn) {
            onTelnetClientListener.onReceiver(tag, stringBuilder);
            return;
        }
        // 放在线程里面去通知
        new Thread(new Runnable() {
            @Override
            public void run() {
                onTelnetClientListener.onReceiver(tag, stringBuilder);
            }
        }).start();
    }

    /**
     * 回复数据
     *
     * @param c
     */
    private boolean revertDate(final char c) {
        if (onTelnetClientListener == null) {
            //  System.out.print(c);
            log.debug("{} \n {}", tag, c);
            return false;
        }
        if (!threadReturn) {
            onTelnetClientListener.onGetDate(tag, new byte[]{(byte) c});
            return false;
        }
        // 放在线程里面去通知
        new Thread(new Runnable() {
            @Override
            public void run() {
                onTelnetClientListener.onGetDate(tag, new byte[]{(byte) c});
            }
        }).start();
        return true;
    }

    /**
     * 设置监听器
     *
     * @param onTelnetClientListener
     */
    public void setOnTelnetClientListener(final OnTelnetClientListener onTelnetClientListener) {
        this.onTelnetClientListener = onTelnetClientListener;
    }

    /**
     * 启动读取线程
     */
    public void onReadThread() {
        if (readThread == null) {
            readThreadRun = true;
            readThread = new ReadThread(in);
            Thread thread = new Thread(readThread);
            thread.start();
        }
    }

    /**
     * Telnet监听
     */
    public interface OnTelnetClientListener {
        void onConnect(final String tag, final boolean isconnect);

        void onDisConnect(final String tag, final boolean isconnect);

        void onLogin(final String tag, final boolean isLogin);

        void onGetDate(final String tag, final byte[] data);

        void onReceiver(final String tag, final String msg);
    }

    /**
     * 读取线程.
     */
    class ReadThread extends Thread {
        /**
         * 输入流,接收返回信息
         */
        private InputStream in;

        public ReadThread(InputStream in){
            this.in = in;
        }

        @Override
        public void run() {
            synchronized (lock) {//只能一个读取
                while (readThreadRun) {
                    read();
                }
                log.warn("停止读取");
            }
        }


        /**
         * 读取字符.
         */
        public void read() {
            char c;
            int code = -1;
            // 强制传输的时候,不进行读取
            while ((code = (inRead())) != -1) {
                // 锁
                c = (char) code;
                if(!isFilterAnsi){
                    // 及时透传
                    revertDate(c);
                }
                else{ // 对回显进行过滤
                    // 颜色
                    if (isFilterColor && c == '\033') {
                        int code2 = inRead();
                        char cc = (char) code2;
                        if (cc == '[' || cc == '(') {
                        }
                    }
                    // 控制码
                    else if (code < 0x20) {
                        // 字符回显
                    } else {
                        // 字符回显
                        revertDate(c);
                    }
                }
            }

        }

        /**
         * 输入流读取.
         * @return 读取字符
         */
        private int inRead() {
            try {
                return in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }

    }

    public static void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }

}

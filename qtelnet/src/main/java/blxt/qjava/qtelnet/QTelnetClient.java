package blxt.qjava.qtelnet;

import lombok.Data;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * telnet客户端
 */
@Data
public class QTelnetClient extends TelnetClient {
    Object lock = new Object();
    String hostIp = "127.0.0.1";
    int port = 22;
    String username;
    String password;
    boolean isLogin = false;

    private String prompt = "$";    //结束标识字符串,Windows中是>,Linux中是#
    private char promptChar = '$';    //结束标识字符
    private InputStream in;        // 输入流,接收返回信息
    private PrintStream out;    // 向服务器写入 命令
    String termtype = "VT100"; // 协议类型 .VT100、VT52、VT220、VTNT、ANSI

    // 编码转换
    private String ORIG_CODEC = "ISO8859-1";
    private String TRANSLATE_CODEC = "GBK";
    boolean isChangeCode = false;


    OnTelnetClientListener onTelnetClientListener = null;

    public QTelnetClient() {
        super();
    }

    public QTelnetClient(String termtype) {
        super(termtype);
    }

    /**
     * 连接
     *
     * @return
     */
    public boolean connect() {
        try {
            super.connect(hostIp, port);
        } catch (IOException e) {
            isLogin = false;
            e.printStackTrace();
            return false;
        }
        if (onTelnetClientListener != null) {
            onTelnetClientListener.onConnect(true);
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
            onTelnetClientListener.onDisConnect(true);
        }
        return true;
    }

    /**
     * 设置结束字符串
     *
     * @param prompt
     */
    public void setPrompt(String prompt) {
        if (prompt != null) {
            this.prompt = prompt;
            this.promptChar = prompt.charAt(prompt.length() - 1);
        }
    }


    /**
     * 登录到目标主机
     */
    public boolean login() {
        in = getInputStream();
        out = new PrintStream(getOutputStream());
        readUntil("login:");
        write(username);
        // 如果是linux,那么就设置用户识别符是#
        if (username.equals("root") && prompt.equals("$")) {
            setPrompt("#");
        }
        readUntil("password:");
        write(password);
        String rs = readUntil(null);
        if (rs.contains("Login Failed")) {
            isLogin = false;
        }else{
            isLogin = true;
        }
        if (onTelnetClientListener != null) {
            onTelnetClientListener.onLogin(isLogin);
        }
        return isLogin;
    }


    /**
     * 发送命令,返回执行结果
     *
     * @param command
     * @return
     */
    public String sendCommand(String command) {
        try {
            write(command);
            return readUntil(prompt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送命令, 线程
     * @param command
     */
    public boolean sendCommandThread(final String command){
        if(!isLogin()){
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendCommand(command);
                ReadThread readThread = new ReadThread();
                readThread.start();
                try {
                    readThread.join();
                } catch (Exception e) {
                }
                readThread = null;
            }
        }).start();
        return true;
    }


    /**
     * 发送命令
     *
     * @param value
     */
    public void write(String value) {
        try {
            out.write(value.getBytes());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            if (flag)
                lastChar = pattern.charAt(pattern.length() - 1);
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
                    if (ch == promptChar)
                        break;
                }
                //登录失败时返回结果
                if (!isLogin && sb.toString().contains("Login Failed")) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (onTelnetClientListener != null) {
            if (isChangeCode) {
                try {
                    onTelnetClientListener.onGetDate(new String(sb.toString().getBytes(ORIG_CODEC), TRANSLATE_CODEC));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                onTelnetClientListener.onGetDate(sb.toString());
            }
        }
        return sb.toString();
    }

    /**
     * Telnet监听
     */
    public interface OnTelnetClientListener {
        void onConnect(final boolean isconnect);

        void onDisConnect(final boolean isconnect);

        void onLogin(final boolean isLogin);

        void onGetDate(final String data);

        void onGetDate(final byte[] data);
    }



    /**
     * 读取主线程，负责管理子线程。防止读取时不动了，这时就抛弃读取子线程
     * @author chruan
     *
     */
    class ReadThread extends Thread {
        public void run() {
            synchronized (lock) {//只能一个读取
                SubReadThread sub = new SubReadThread();
                sub.start();
                int last = sub.count;
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    if (last == sub.count) {
                        sub.stop();
                        break;
                    } else {
                        last = sub.count;
                    }
                }
                String s = sub.sb.toString();
                try {
                    System.out.println("结果2:" + new String(s.getBytes(ORIG_CODEC),
                            TRANSLATE_CODEC));
                } catch (UnsupportedEncodingException e) {
                    System.out.println(s);
                }
                sub = null;
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
        StringBuffer sb = new StringBuffer();

        public void read() {
            try {
                char c;
                int code = -1;
                boolean ansiControl = false;
                boolean start = true;
                while ((code = (in.read())) != -1) {
                    count++;
                    c = (char) code;
                    if (c == '\033') {
                        ansiControl = true;
                        int code2 = in.read();
                        char cc = (char) code2;
                        count++;
                        if (cc == '[' || cc == '(') {
                        }
                    }
                    if (!ansiControl) {
                        if (c == '\r') {
                            String olds = new String(sb.toString().getBytes(
                                    ORIG_CODEC), TRANSLATE_CODEC);
                            System.out.println(olds);
                            sb.delete(0, sb.length());
                        } else if (c == '\n')
                            ;
                        else
                            sb.append(c);
                    }

                    if (ansiControl) {
                        if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')
                                || c == '"') {
                            ansiControl = false;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        public void run() {
            read();
        }
    }

}

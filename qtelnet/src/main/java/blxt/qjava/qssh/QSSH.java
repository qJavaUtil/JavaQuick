package blxt.qjava.qssh;

import blxt.qjava.qtelnet.QTelnetClient;
import com.jcraft.jsch.*;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

/**
 * ssh 链接工具
 */
@Data
public class QSSH extends JSch {
    final static Logger log = LoggerFactory.getLogger(QSSH.class);

    String tag = "default";
    String host = "127.0.0.1";
    int port = 22;
    String userName = "root";
    String password = "root";
    String encoding = "UTF-8";
    String channelType = "shell";
    /** 超时时间 */
    int connectTimeout = 30000;
    /** ssh链接*/
    Session session;
    private Channel channel;
    Properties config = new Properties();
    /** 监听 */
    QTelnetClient.OnTelnetClientListener onTelnetClientListener;
    /** 回显线程 **/
    Thread read;

    public QSSH(){
        config.put("StrictHostKeyChecking", "no");
    }

    /**
     * 构建.
     * @return this
     */
    public QSSH build(){
        JSch jsch = new JSch();
        try {
            session=jsch.getSession(userName,host, port);
        } catch (JSchException e) {
            e.printStackTrace();
            return null;
        }
        session.setConfig(config);
        //设置密码
        session.setPassword(password);
        return this;
    }


    /**
     * 连接.
     */
    public boolean connect(){
        boolean openFal = false;
        try {
            //连接  超时时间30s
            session.connect(connectTimeout);
            //开启shell通道
            this.channel = session.openChannel(channelType);
            //通道连接 超时时间3s
            channel.connect(connectTimeout);
            //读取终端返回的信息流
            read = new Thread(new ReadRunable());
            read.start();
            openFal = true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            if(onTelnetClientListener != null){
                onTelnetClientListener.onConnect(tag, openFal);
            }
        }
        return true;
    }

    /**
     * 关闭.
     */
    public void close( ) {
        if (this.channel != null) {
            //断开连接
            this.channel.disconnect();
        }
    }


    /**
     * 将消息转发到终端.
     * @param command     指令
     * @return boolean
     */
    public boolean send(String command) {
        return send(command.getBytes());
    }

    /**
     * 将消息转发到终端.
     * @param command 指令
     * @return boolean
     */
    public boolean send(byte[] ... command) {
        OutputStream outputStream = null;
        try {
            outputStream = channel.getOutputStream();
            for (byte[] bytes : command) {
                outputStream.write(bytes);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * push 回车
     * @return
     */
    public boolean push() {
        return send(new byte[]{13});
    }

    /**
     * 发送指令, 并回车
     * @param command 指令
     * @return
     */
    public boolean sendPush(String command){
        return send(command.getBytes(), new byte[]{13});
    }


    /**
     * 单独执行
     * @param command  指令
     * @return  string
     */
    public String sendStandalone(String command) {
        try {
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            InputStream in = channelExec.getInputStream();
            channelExec.setCommand(command);
            channelExec.setErrStream(System.err);
            channelExec.connect();
            String out = IOUtils.toString(in, encoding);
            channelExec.disconnect();
            return out;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public QSSH setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public QSSH setHost(String host) {
        this.host = host;
        return this;
    }

    public QSSH setPort(int port) {
        this.port = port;
        return this;
    }

    public QSSH setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public QSSH setPassword(String password) {
        this.password = password;
        return this;
    }

    public QSSH setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public QSSH setChannelType(String channelType) {
        this.channelType = channelType;
        return this;
    }

    public QSSH setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public QSSH setConfig(Properties config) {
        this.config = config;
        return this;
    }

    public QSSH setOnTelnetClientListener(QTelnetClient.OnTelnetClientListener onTelnetClientListener) {
        this.onTelnetClientListener = onTelnetClientListener;
        return this;
    }


    /**
     * 读取线程
     */
    class ReadRunable implements Runnable{

        @Override
        public void run() {
            //读取终端返回的信息流
            InputStream inputStream = null;
            try {
                inputStream = channel.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                //循环读取
                byte[] buffer = new byte[1024];
                int i = 0;
                //如果没有数据来，线程会一直阻塞在这个地方等待数据。
                while (true) {
                    try {
                        if ((i = inputStream.read(buffer)) == -1) {
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byte[] tmp = Arrays.copyOfRange(buffer, 0, i);
                    if(onTelnetClientListener != null){
                        onTelnetClientListener.onGetDate(tag, tmp);
                    }
                }

            } finally {
                //断开连接后关闭会话
                session.disconnect();
                channel.disconnect();
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(onTelnetClientListener != null){
                    onTelnetClientListener.onDisConnect(tag, false);
                }
            }
        }
    }

}

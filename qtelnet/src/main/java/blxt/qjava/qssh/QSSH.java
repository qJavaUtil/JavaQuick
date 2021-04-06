package blxt.qjava.qssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * ssh 链接工具
 */
@Data
public class QSSH extends JSch {
    final static Logger log = LoggerFactory.getLogger(QSSH.class);

    String tag = "default";
    String host = "127.0.0.1";
    int port = 22;
    String user = "root";
    String password = "root";
    String encoding = "UTF-8";
    /** ssh链接*/
    Session session;
    /** 监听 */
    OnTelnetClientListener onTelnetClientListener;

    public QSSH(){
 
    }

    /**
     * 链接
     * @return
     */
    public boolean connect(){
        boolean fal = true;
        try {
            session = getSession(user, host, port);
        } catch (JSchException e) {
            fal = false;
            log.error("获取session失败:{}", e.getMessage());
            e.printStackTrace();
        }
        if(fal){
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            try {
                session.connect();
            } catch (JSchException e) {
                fal = false;
                log.error("连接失败:{}", e.getMessage());
                e.printStackTrace();
            }
        }

        if(onTelnetClientListener != null){
            onTelnetClientListener.onConnect(tag, fal);
        }
        return fal;
    }


    /**
     * 执行
     * @param command
     * @return
     * @throws Exception
     */
    public String exeCommand(String command) throws Exception {
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        String out = IOUtils.toString(in, encoding);

        channelExec.disconnect();
        if(onTelnetClientListener != null){
            onTelnetClientListener.onGetDate(tag, out);
        }
        return out;
    }

    /**
     * 发送指令
     * @param command
     */
    public synchronized void exeCommandThread(final String command){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    exeCommand(command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 关闭
     */
    public void close(){
        if(session != null) {
            session.disconnect();
        }
        if(onTelnetClientListener != null){
            onTelnetClientListener.onClose(tag,true);
        }
    }

    /**
     * SSH监听
     */
    public interface OnTelnetClientListener {
        void onConnect(final String tag, final boolean isconnect);

        void onGetDate(final String tag, final String data);

        void onClose(final String tag, final boolean data);
    }


}

package blxt.qjava.qexecute;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * cmd信息反馈线程
 */
@Data
public class InputStreamThread implements Runnable{
    /** cmd控制台编码 */
    public String CODE = "gbk";
    final static Logger logger = LoggerFactory.getLogger(InputStreamThread.class);
    String tag = "default";
    int cacheSize = 1024;
    /** 输入流 */
    private InputStream ins = null;
    /** websocket */
    CallBack callBack;

    public InputStreamThread(InputStream ins, CallBack callBack){
        this.ins = ins;
        this.callBack = callBack;
    }

    /**
     * 将输入流,同步到socket
     */
    @Override
    public void run() {
        String line = null;
        byte[] b = new byte[cacheSize];
        int num = 0;
        try {
            while((num=ins.read(b))!=-1){
                String msg = new String(b, CODE);
                if(callBack != null){
                    callBack.onReceiver(tag, msg.trim());
                }
                else
                {
                    logger.debug("tag:\r\n{}", msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface CallBack{
        void onReceiver(String tag, String msg);
    }



}

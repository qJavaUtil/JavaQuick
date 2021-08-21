package blxt.qjava.qexecute;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * cmd信息反馈线程
 * @author ZhangJieLei
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
    /** 输出空闲时间 */
    long timeFree = 0;

    /** 长时间没有数据交互时, 自动断开链接 */
    boolean autoClose = false;
    /** 交互超时 30 s */
    long timeFreeMax = 30000 * 1000;

    /** 主线程运行标记 */
    boolean run = true;

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
        timeFree = System.currentTimeMillis();
        try {
            while(run && (num=ins.read(b))!=-1){
                String msg = new String(b, CODE);
                // 超时停止
                if(autoClose && timeFreeMax > System.currentTimeMillis() - timeFree){
                    run = false;
                    if(callBack != null){
                        callBack.onOvertime(tag);
                    }
                    else{
                        return;
                    }
                }
                timeFree = System.currentTimeMillis();
                if(callBack != null){
                    callBack.onReceiver(tag, msg.trim());
                }
                else
                {
                    logger.debug("tag:\r\n{}", msg);
                }
                b = new byte[cacheSize];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        run = false;
        try {
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public interface CallBack{
        void onReceiver(String tag, String msg);
        void onOvertime(String tag);
    }


}

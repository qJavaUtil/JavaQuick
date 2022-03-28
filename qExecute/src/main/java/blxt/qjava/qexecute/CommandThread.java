package blxt.qjava.qexecute;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author OpenJialei
 * @date 2022年03月28日 11:42
 */
public class CommandThread extends Thread {
    ExecuterFactory executeUtil;
    BufferedReader br = null;

    public CommandThread(ExecuterFactory executeUtil) {
        this.executeUtil = executeUtil;
        // 避免出现乱码问题,直接使用系统默认的编码格式
        br = new BufferedReader(new InputStreamReader(System.in, Charset.forName(ExecuterFactory.getsystemLanguage())));
        this.setDaemon(true);
    }

    @Override
    public void run() {
        try {
            String cmd = null;
            while ((cmd = br.readLine()) != null) {
                executeUtil.execute(cmd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executeUtil.close();
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

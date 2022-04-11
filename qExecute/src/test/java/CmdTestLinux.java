import blxt.qjava.qexecute.ExecuterFactory;
import blxt.qjava.qexecute.InputStreamThread;

import java.io.File;

public class CmdTestLinux {

    public static void main(String[] args) {
        InputStreamThread.CallBack callBack =new InputStreamThread.CallBack() {
            @Override
            public void onReceiver(String tag, String msg) {
                System.out.println(msg);
            }

            @Override
            public void onOvertime(String tag) {
                System.out.println(tag + "关闭");
            }
        };

        // 连续执行
        ExecuterFactory executeUtil =
            new ExecuterFactory()
                .workPath(new File("/"))
                .callBack(callBack)
                // redirectOutput 和 callBack 互斥
                //.redirectOutput("redirectOutput.txt")
                .build();
        executeUtil.execute("cd /etc/");
        executeUtil.execute("ls");

        // 使用控制台输入
//        blxt.qjava.qexecute.CommandThread commandThread = new blxt.qjava.qexecute.CommandThread(executeUtil);
//        commandThread.setName("ExecuteCmdThread");
//        commandThread.start();
   //     executeUtil.close();
        executeUtil.waitFor();
        System.out.println("2结束");

    }

}



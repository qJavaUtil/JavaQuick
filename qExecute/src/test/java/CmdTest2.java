import blxt.qjava.qexecute.ExecuterFactory;
import blxt.qjava.qexecute.InputStreamThread;

import java.io.File;

public class CmdTest2 {

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

        // 单个执行
//        ExecuterFactory executeUtil2 =
//            new ExecuterFactory()
//                .workPath(new File("E:\\ShiTou\\Downloads\\软件\\m3u8DL"))
//                .callBack(callBack)
//                .build("cp M3U8.zip M3U8.back");
//        executeUtil2.waitFor();
//        System.out.println("1结束");

        // 连续执行
        ExecuterFactory executeUtil =
            new ExecuterFactory()
                .workPath(new File("E:\\ZhangJieLei"))
                .callBack(callBack)
                // redirectOutput 和 callBack 互斥
                .redirectOutput("redirectOutput.txt")
                .build();
        executeUtil.execute("cd E:\\ZhangJieLei\\Pictures");
        executeUtil.execute("dir");
        executeUtil.execute("E:\\ShiTou\\Downloads\\N_m3u8DL-CLI_v2.9.9.exe");
        executeUtil.execute("https://vod3.jializyzm3u8.com/20211021/0KTHQN9F/index.m3u8");

        // 使用控制台输入
        blxt.qjava.qexecute.CommandThread commandThread = new blxt.qjava.qexecute.CommandThread(executeUtil);
        commandThread.setName("ExecuteCmdThread");
        commandThread.start();
   //     executeUtil.close();

        executeUtil.waitFor();
        System.out.println("2结束");

    }

}



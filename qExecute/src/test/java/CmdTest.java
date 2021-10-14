import blxt.qjava.qexecute.Executer;
import blxt.qjava.qexecute.InputStreamThread;
import blxt.qjava.utils.ExecuteUtil;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CmdTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        String path = "E:\\ZhangJieLei\\Documents\\workspace\\workProject\\IotDoc\\RealEvo-Simulator";

        List<String> commands = new ArrayList<>();
        commands.add("E:\\ZhangJieLei\\Documents\\workspace\\workProject\\IotDoc\\RealEvo-Simulator\\qemu\\qemu-system-mini2440.exe");
        commands.add("-M");
        commands.add("mini2440");
        commands.add("-kernel");
        commands.add("./newvm\\mini2440_new\\mini2440_new.bin");
        commands.add("-mtdblock");
        commands.add("./newvm\\mini2440_new\\mini2440_new.img");
        commands.add("-net");
        commands.add("nic");
        commands.add("-net");
        commands.add("tap,ifname=Net2");
        commands.add("-serial");
        commands.add("telnet:127.0.0.1:11736,server");
        commands.add("-serial");
        commands.add("tcp:127.0.0.1:25930,server,nowait");
        commands.add("-vnc");
        commands.add("127.0.0.1:124");
        commands.add("-L");
        commands.add("qemu");

        Executer executer = new Executer();
        executer.init(new File(path), commands);
        executer.addReaderStream(new InputStreamThread.CallBack() {
            @Override
            public void onReceiver(String tag, String msg) {
                System.out.println(msg);
            }

            @Override
            public void onOvertime(String tag) {

            }
        });

        executer.close();
//
//        Scanner sc = new Scanner(System.in);
//        while(sc.hasNext())
//        {
//            String s = sc.nextLine();
//            executer.exec(s);
//        }




//        ProcessBuilder pb =new ProcessBuilder(commands);
//        pb.directory(new File("E:\\ZhangJieLei\\Documents\\workspace\\workProject\\IotDoc\\RealEvo-Simulator"));//设置工作目录
//
//        Process process = pb.start();
//
//        process.destroyForcibly();
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        String line = null;
//
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//        }
//        Thread.sleep(1000);
//
//        BufferedReader br2 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//        while ((line = br2.readLine()) != null) {
//            System.out.println(line);
//        }
//        Thread.sleep(1000);
//        process.destroyForcibly();
//        int status = process.waitFor();
//    }

//    public static boolean runCMD(String cmd) throws IOException, InterruptedException {
//        final String METHOD_NAME = "runCMD";
//        Process p = Runtime.getRuntime().exec(cmd);
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//            String readLine = br.readLine();
//            StringBuilder builder = new StringBuilder();
//            while (readLine != null) {
//                readLine = br.readLine();
//                builder.append(readLine);
//            }
//            logger.debug(METHOD_NAME + "#readLine: " + builder.toString());
//
//            p.waitFor();
//            int i = p.exitValue();
//            logger.info(METHOD_NAME + "#exitValue = " + i);
//            if (i == 0) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (IOException e) {
//            logger.error(METHOD_NAME + "#ErrMsg=" + e.getMessage());
//            e.printStackTrace();
//            throw e;
//        } finally {
//            if (br != null) {
//                br.close();
//            }
//        }
    }

}



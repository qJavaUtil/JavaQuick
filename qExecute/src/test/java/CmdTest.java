import blxt.qjava.qexecute.Executer;
import blxt.qjava.qexecute.InputStreamThread;
import blxt.qjava.utils.ExecuteUtil;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CmdTest {

    public static void main(String[] args) {
        String path = "E:\\ZhangJieLei\\Documents\\workspace\\workProject\\IotDoc\\IDEH_HOME";

        Executer executer = new Executer();
        executer.init(path);
        executer.addReaderStream(new InputStreamThread.CallBack() {
            @Override
            public void onReceiver(String tag, String msg) {
               // System.out.println(msg);
            }

            @Override
            public void onOvertime(String tag) {

            }
        });

        Scanner sc = new Scanner(System.in);
        while(sc.hasNext())
        {
            String s = sc.nextLine();
            executer.exec(s);
        }


    }

}



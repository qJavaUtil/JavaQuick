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
        String path = "E:\\ShiTou\\Downloads\\软件\\m3u8DL";

        List<String> commands = new ArrayList<>();
       // commands.add("E:");
        commands.add("cp");
        commands.add("E:\\ShiTou\\Downloads\\软件\\m3u8DL");

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

      //  executer.close();

    }

}



package com.example.appqjava;

import blxt.qjava.qexecute.ExecuterFactory;
import blxt.qjava.qexecute.ExecuterType;
import blxt.qjava.qexecute.InputStreamThread;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

@SpringBootApplication
public class AppQjavaApplication {

    public static void main(String[] args) throws IOException, InterruptedException {

//
//        DocHelperTest docHelperTest = new DocHelperTest();
//        docHelperTest.test1();

//        String filepath = "./test1.docx";
//        String outpath = "./test4.pdf";
//
//        InputStream source = new FileInputStream(filepath);
//        OutputStream target = new FileOutputStream(outpath);
//        PdfOptions options = PdfOptions.create();
//        XWPFDocument doc = new XWPFDocument(source);
//        PdfConverter.getInstance().convert(doc, target, options);

//        DocConverter docConverter = new DocConverter();
//        docConverter.setInput(new File("./test1.docx"));
//        docConverter.setOutput(new File("./test1.pdf"));
//        docConverter.docx2Pdf();

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

        String path = "E:\\ZhangJieLei\\Documents\\workspace\\workProject\\MyProject\\webdav-aliyundriver\\target\\";


        System.out.println("执行1:" + new ExecuterFactory().code("utf-8").execStandalone("cmd.exe /c run.bat", path));

//        // 连续执行
//        ExecuterFactory executeUtil =
//            new ExecuterFactory()
//                .executerType(ExecuterType.Linux)
//                .workPath(new File("/"))
//                .callBack(callBack)
//                // redirectOutput 和 callBack 互斥
//                //.redirectOutput("redirectOutput.txt")
//                .build();
////        executeUtil.execute("ls \n");
////        executeUtil.execute("cd /etc \n");
////        executeUtil.execute("ls \n");
//
//
//        // 使用控制台输入
////        blxt.qjava.qexecute.CommandThread commandThread = new blxt.qjava.qexecute.CommandThread(executeUtil);
////        commandThread.setName("ExecuteCmdThread");
////        commandThread.start();
//        //     executeUtil.close();
//        Thread.sleep(5000);
//        // 一般不用这个
//        executeUtil.waitFor();
//
//        System.out.println("2结束" + new ExecuterFactory().code("utf-8").execStandalone("ls", "/etc"));
//        System.out.println("2结束" + new ExecuterFactory().code("gbk").execStandalone("ls", "/"));


    }

}

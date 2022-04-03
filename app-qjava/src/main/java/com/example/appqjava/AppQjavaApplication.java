package com.example.appqjava;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

@SpringBootApplication
public class AppQjavaApplication {

    public static void main(String[] args) throws IOException {

//
//        DocHelperTest docHelperTest = new DocHelperTest();
//        docHelperTest.test1();

        String filepath = "./test1.docx";
        String outpath = "./test4.pdf";

        InputStream source = new FileInputStream(filepath);
        OutputStream target = new FileOutputStream(outpath);
        PdfOptions options = PdfOptions.create();
        XWPFDocument doc = new XWPFDocument(source);
        PdfConverter.getInstance().convert(doc, target, options);

//        DocConverter docConverter = new DocConverter();
//        docConverter.setInput(new File("./test1.docx"));
//        docConverter.setOutput(new File("./test1.pdf"));
//        docConverter.docx2Pdf();
//

    }

}

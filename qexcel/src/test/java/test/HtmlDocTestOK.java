package test;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import lombok.SneakyThrows;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;

public class HtmlDocTestOK {

    @SneakyThrows
    public static void main(String[] args) {
        boolean w = false;
        HtmlDocTestOK test = new HtmlDocTestOK();
        String path = "E:/Desktop/2020pdf/data/A11148-4876432.html";
        String out = "E:/Desktop/2020pdf/data/A11148-4876432.docx";
        String outPdf = "E:/Desktop/2020pdf/data/A11148-4876432.pdf";

        test.wordToPdf(new File(out), new File(outPdf));

    }



    private static boolean license = false;

    public void wordToPdf(File in, File out) throws Exception {
        FileOutputStream os = null;
        try {
            //凭证 不然切换后有水印
            InputStream is = HtmlDocTestOK.class.getClassLoader().getResourceAsStream("license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            license = true;
            if (!license) {
                System.out.println("License验证不通过...");
                return ;
            }
            //生成一个空的PDF文件
            os = new FileOutputStream(out);
            //要转换的word文件
            Document doc = new Document(in.getAbsolutePath());
            doc.save(os, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

package test;

import blxt.qjava.excel.DocHelper;
import blxt.qjava.file.QFile;
import lombok.SneakyThrows;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;


import java.io.*;
import java.nio.charset.StandardCharsets;

public class HtmlDocTest {

    @SneakyThrows
    public static void main(String[] args) {
        boolean w = false;
        HtmlDocTest test = new HtmlDocTest();
        String path = "E:/Desktop/2020pdf/data/A11148-4876432.html";
        String out = "E:/Desktop/2020pdf/data/A11148-4876432.docx";
        String outPdf = "E:/Desktop/2020pdf/data/A11148-4876432.pdf";

//        String content = QFile.Read.getStr(new File(path));
//        InputStream is = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
//        OutputStream os = new FileOutputStream(out);
//        test.inputStreamToWord(is, os);
//
//        DocHelper docHelper = new DocHelper(new File(out));
//        docHelper.write(new File(outPdf));
    }

    /**
     * 把is写入到对应的word输出流os中
     * 不考虑异常的捕获，直接抛出
     * @param is
     * @param os
     * @throws IOException
     */
    private void inputStreamToWord(InputStream is, OutputStream os) throws IOException {
        POIFSFileSystem fs = new POIFSFileSystem();
        //对应于org.apache.poi.hdf.extractor.WordDocument
        fs.createDocument(is, "WordDocument");
        fs.writeFilesystem(os);
        os.close();
        is.close();
    }


}

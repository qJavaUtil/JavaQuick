package blxt.qjava.excel;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import lombok.Data;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;

/**
 * 文档转换
 * @author OpenJialei
 * @date 2021年08月30日 22:51
 */
@Data
public class DocConverter {
    /**  源文件 */
    File input = null;
    /** 新文件 */
    File output = null;

    InputStream inputStream = null;
    OutputStream outputStream = null;

    public DocConverter() {

    }

    public DocConverter(File input, File output) {
        this.input = input;
        this.output = output;
    }

    /**
     * docx 转换成pdf
     * @return
     */
    public boolean docx2Pdf(){
        try  {
            // 初始化流
            if(outputStream == null){
                outputStream = new FileOutputStream(output);
            }
            // 转换
            docx2Pdf(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 输出到流
     * @param outputStream
     * @return
     */
    public boolean docx2Pdf(OutputStream outputStream ){
        try  {
            if(inputStream == null){
                inputStream = new FileInputStream(input);
            }
            IConverter converter = LocalConverter.builder().build();
            converter.convert(inputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * 将word文档， 转换成pdf, 中间替换掉变量
     * @param source 源为word文档， 必须为docx文档
     * @param target 目标输出
     * @param options PdfOptions.create().fontEncoding( "windows-1250" ) 或者其他
     * @throws Exception
     */
    public static void wordConverterToPdf(InputStream source, OutputStream target,
                                          PdfOptions options) throws Exception {
        XWPFDocument doc = new XWPFDocument(source);

        PdfConverter.getInstance().convert(doc, target, options);

    }

}

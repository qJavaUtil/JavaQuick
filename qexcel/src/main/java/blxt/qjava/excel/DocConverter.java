package blxt.qjava.excel;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import lombok.Data;

import java.io.*;

/**
 * 文档转换
 * @author OpenJialei
 * @date 2021年08月30日 22:51
 */
@Data
public class DocConverter {
    File input = null;
    File output = null;

    InputStream inputStream = null;
    OutputStream outputStream = null;

    /**
     * docx 转换成pdf
     * @return
     */
    public boolean docx2Pdf(){
        try  {
            if(inputStream == null){
                inputStream = new FileInputStream(input);
            }
            if(outputStream == null){
                outputStream = new FileOutputStream(output);
            }
            IConverter converter = LocalConverter.builder().build();
            converter.convert(inputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}

package blxt.qjava.excel;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author OpenJialei
 * @date 2021年06月14日 11:51
 */
public class CsvUtils {

    public static <T> List<T> getCsvData(File file, Class<T> clazz ){
        return getCsvData(file, clazz, "UTF-8");
    }

    public static <T> List<T> getCsvData(File file, Class<T> clazz, String charsetName) {
        InputStreamReader in = null;
        try {
            // UTF-8为解析时用的编码格式，csv文件也需要定义为UTF-8编码格式，解析格式根据csv文件的编码格式而定，csv默认编码格式为GBK
            in = new InputStreamReader(new FileInputStream(file), charsetName);
        } catch (IOException e) {
            return null;
        }
        return getCsvData(in, clazz);
    }

    public static <T> List<T> getCsvData(InputStreamReader inputStreamReader, Class<T> clazz) {

        HeaderColumnNameMappingStrategy<T> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
        mappingStrategy.setType(clazz);
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(inputStreamReader)
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .withMappingStrategy(mappingStrategy)
                .build();
        return csvToBean.parse();
    }
}

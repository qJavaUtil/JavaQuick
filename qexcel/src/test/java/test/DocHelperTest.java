package test;

import blxt.qjava.excel.DocHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author OpenJialei
 * @date 2021年08月22日 23:10
 */
public class DocHelperTest {

    public void test1(){

        File file = new File("./tmple.docx");
        DocHelper docHelper = new DocHelper(file);

        //传入模板的参数
        Map<String,Object> params=new HashMap<>();

        // 一个单元格内一行字, key可是中文
        params.put("版本","V2.0");
        params.put("auth","张三");
        params.put("data","2021/01/01");

        //一个单元格内多行字
        List<String> hobby=new ArrayList<>();
        hobby.add("1、打篮球");
        hobby.add("2、打羽毛球");
        hobby.add("3、游泳");
        params.put("docabout", hobby);

        try {
            // 普通段落内容替换
            docHelper.replaceWord(params);
            // 表格内容替换
            docHelper.replaceTable(params);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }

        docHelper.write(new File("./test1.docx"));

    }


    public static void main(String[] args) {
        DocHelperTest docHelperTest = new DocHelperTest();
        docHelperTest.test1();
    }
}

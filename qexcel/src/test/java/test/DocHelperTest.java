package test;

import blxt.qjava.excel.DocHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author OpenJialei
 * @date 2021年08月22日 23:10
 */
public class DocHelperTest {
        public void test2() {
            File file = new File("./associate.docx");
            DocHelper docHelper = new DocHelper(file);

            //传入模板的参数
            Map<String, Object> params = new HashMap<>();

            // 一个单元格内一行字, ${key}可是中文
            params.put("pureAttention", "qwrwer");
            params.put("purePersonnel", "张三");
            params.put("date", "2021/01/01");
            try {
                // 普通段落内容替换
                docHelper.replaceWord(params);
                // 表格内容替换
                docHelper.replaceTable(params);

            } catch (Exception e) {
                e.printStackTrace();
            }

            docHelper.write(new File("./test2.docx"));
        }

        public void test1() {

        File file = new File("./tmple.docx");
        DocHelper docHelper = new DocHelper(file);

        //传入模板的参数
        Map<String, Object> params = new HashMap<>();


        // 图片, 图片是 @{key}
        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("imgpath", "E:\\ZhangJieLei\\Pictures\\Saved Pictures\\log\\henenglog.png");

        //一个单元格内多行字
        List<String> hobby = new ArrayList<>();
        hobby.add("1、打篮球");
        hobby.add("2、打羽毛球");
        hobby.add("3、游泳");
        params.put("docabout", hobby);

        // 一个单元格内一行字, ${key}可是中文
        params.put("版本", "V2.0");
        params.put("auth", "张三");
        params.put("data", "2021/01/01");
        params.put("图片", imageMap);


        params.put("单位", "和能银行");
        params.put("称呼", "马总");
        params.put("说明", "这是一个测试理由");


        // 表格中要添加的行
        List<String[]> tableNewData = new ArrayList<>();
        tableNewData.add(new String[]{"1", "2", "3", "4"});
        tableNewData.add(new String[]{"1", "21234", "3", "4123"});

        try {
            // 普通段落内容替换
            docHelper.replaceWord(params);
            // 表格内容替换
            docHelper.replaceTable(params);
            // 指定表格添加行
            docHelper.addTable(0, 2, tableNewData);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }

        // 写出到流
        //OutputStream stream = null;
        //docHelper.write(stream);
        // 写出到文件
          docHelper.write(new File("./test1.docx"));

        // 保存到pdf
        //docHelper.write2Pdf(new File("./test2.pdf"));
            //  docHelper.toPdf(new File("./test3.pdf"));
    }


    public static void main(String[] args) {
        DocHelperTest docHelperTest = new DocHelperTest();
        docHelperTest.test2();
      //  docHelperTest.test1();
    }
}

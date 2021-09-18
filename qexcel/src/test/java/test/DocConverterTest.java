package test;

import blxt.qjava.excel.DocConverter;

import java.io.File;

/**
 * 文档转换工具
 * @author OpenJialei
 * @date 2021年09月18日 14:57
 */
public class DocConverterTest {


    /**
     * 没有授权, 有水印
     */
    public void toPdf(){
        //加载word示例文档
//        Document document = new Document();
//        document.loadFromFile("./test1.docx");
//        //保存结果文件
//        document.saveToFile("./toPDF.pdf", FileFormat.PDF);
    }


    public void toPdf2(){
        DocConverter docConverter = new DocConverter();
        docConverter.setInput(new File("./test1.docx"));
        docConverter.setOutput(new File("./test1.pdf"));
        docConverter.docx2Pdf();
    }


    public static void main(String[] args) {
        DocConverterTest docHelperTest = new DocConverterTest();
        docHelperTest.toPdf2();
    }

}

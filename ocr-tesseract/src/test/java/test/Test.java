package test;

import com.qocr.tesseract.OCRHelper;
import lombok.SneakyThrows;

import java.io.File;


/**
 * @author OpenJialei
 * @date 2021年09月22日 20:28
 */
public class Test {
    static String path = "E:/ZhangJieLei/Documents/workspace/workProject/ocr/image2.png";
    static String path2 = "E:/ZhangJieLei/Documents/workspace/workJava/JavaQuick/JavaQuick/image2.jpg";
    static File file = new File(path);


    @SneakyThrows
    public static void main(String[] args) {

        try {
            //图片文件：此图片是需要被识别的图片
            File file = new File("./颜色替换-降噪.jpg");
            String recognizeText =
                new OCRHelper()
                    .setTessPath("C://Program Files (x86)//Tesseract-OCR/tesseract")
                    .setLanguage("eng")
                    .setSuffix(OCRHelper.SUFFIX_ONLY_NUMBER)
                    .recognizeText(file);
            System.out.print(recognizeText + "\t");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

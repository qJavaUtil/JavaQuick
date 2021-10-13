package com.qocr.tesseract;

import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 图文识别帮助类
 *
 * @author Felix Li
 * @create 2017-12-19-9:12
 */
@Data
public class OCRHelper {

    /** 语言模型后缀, 仅识别数组和符号 */
    public static final String SUFFIX_ONLY_NUMBER = "digits";

    private final String LANG_OPTION = "-l";
    private final String EOL = System.getProperty("line.separator");

    /**
     *  Tesseract-OCR的安装路径
     */
    private String tessPath = "C://Program Files (x86)//Tesseract-OCR/tesseract";

    /** 语言模型 */
    private String language = "eng";
    /** 语言模型后缀 digits - 只识别字符和数字*/
    private String suffix = "";
    /** 其他参数 */
    private String psm = "";

    /**
     * @param imageFile   传入的图像文件
     * @return 识别后的字符串
     */
    public String recognizeText(File imageFile) throws Exception {
        /**
         * 设置输出文件的保存的文件目录
         */
        File outputFile = new File(imageFile.getParentFile(), imageFile.getName() + System.currentTimeMillis());

        // 命令参数
        List<String> cmd = new ArrayList<String>();
        cmd.add(tessPath);
        cmd.add("");
        cmd.add(outputFile.getName());
        cmd.add(LANG_OPTION);
        cmd.add(language);
        cmd.add(suffix);

        // CMD 执行器
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(imageFile.getParentFile());
        cmd.set(1, imageFile.getName());
        pb.command(cmd);
        pb.redirectErrorStream(true);

        Process process = pb.start();
        int w = process.waitFor();

        StringBuffer strB = new StringBuffer();

        // 结果解析
        // 0代表正常退出
        if (w == 0)
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                                                   new FileInputStream(outputFile.getAbsolutePath() + ".txt"),
                                                   StandardCharsets.UTF_8));
            String str;
            while ((str = in.readLine()) != null) {
                strB.append(str).append(EOL);
            }
            in.close();

        } else {
            String msg;
            switch (w) {
                case 1:
                    msg = "Errors accessing files. There may be spaces in your image's filename.";
                    break;
                case 29:
                    msg = "Cannot recognize the image or its selected region.";
                    break;
                case 31:
                    msg = "Unsupported image format.";
                    break;
                default:
                    msg = "Errors occurred.";
            }
            throw new RuntimeException(msg);
        }
        new File(outputFile.getAbsolutePath() + ".txt").delete();
        return strB.toString().replaceAll("\\s*", "");
    }

    public OCRHelper setTessPath(String tessPath) {
        this.tessPath = tessPath;
        return this;
    }

    public OCRHelper setLanguage(String language) {
        this.language = language;
        return this;
    }

    public OCRHelper setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public OCRHelper setPsm(String psm) {
        this.psm = psm;
        return this;
    }
}
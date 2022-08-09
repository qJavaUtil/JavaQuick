package blxt.qjava.file.change;


import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import blxt.qjava.file.EncodingDetect;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 文本格式转换
 * 逐个文件判断，非utf-8才转
 */
public class TxtConvert {

    /**
     * 转换文件编码格式
     *
     * @param path        需要转换的文件或文件夹路径
     * @param fromCharset 原编码格式
     * @param toCharset   目标编码格式
     * @param expansion   需要转换的文件扩展名,如需全部转换则传 null
     */
    public static void convertCharset(String path, Charset fromCharset, Charset toCharset, String expansion) {
        if (StrUtil.isBlank(path)) {
            return;
        }
        File file = FileUtil.file(path);
        FileUtil.convertCharset(file, fromCharset, toCharset);
    }

    public static void main(String[] args) {
        String file = "E:\\Documents\\workspace\\workProject\\Ideh-local\\base2\\dsohandle.c";
        String fileEncode = EncodingDetect.getJavaEncode(file);
        System.out.println("编码:" + fileEncode);
        TxtConvert.convertCharset(file, Charset.forName(fileEncode), StandardCharsets.UTF_8, null);
    }
}
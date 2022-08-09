import blxt.qjava.file.EncodingDetect;
import blxt.qjava.file.callback.FileScanCallback;
import blxt.qjava.file.change.TxtConvert;
import blxt.qjava.file.search.FileSearchHelper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CodeChange {
    static FileSearchHelper fileSearchHelper = new FileSearchHelper();

    public static void main(String[] args) {
//      文件所在路径
      //  String inPath = "E:\\Documents\\workspace\\workProject\\Ideh-local\\hwstudio_workspace-饶-20210917";

     //   String inPath = "E:\\Documents\\workspace\\workProject\\Ideh-local\\src\\template";
        String inPath = "E:\\Documents\\workspace\\workProject\\Ideh-local\\base";

        fileSearchHelper.setCallback(new MyFileScanCallback(false));
        fileSearchHelper.setCode(StandardCharsets.UTF_8);
        fileSearchHelper.getFileList(new File(inPath), "Makefile", null);
        fileSearchHelper.getFileList(new File(inPath), ".mk", null);
//        fileSearchHelper.getFileList(new File(inPath), ".c", null);
//        fileSearchHelper.getFileList(new File(inPath), ".cpp", null);
        fileSearchHelper.getFileList(new File(inPath), ".h", null);
        fileSearchHelper.getFileList(new File(inPath), ".c", null);
        System.out.println("转换完成");

    }

    static class MyFileScanCallback implements FileScanCallback {

        boolean falDelete = true;

        public MyFileScanCallback(boolean falDelete) {
            this.falDelete = falDelete;
        }

        @Override
        public boolean onFile(File file) {
            if (falDelete) {
                file.delete();
                return false;
            }

            String fileEncode=null;
            //   获得文件编码
            if (file.length() < 10){
                return false;
            }
            fileEncode = EncodingDetect.getJavaEncode(file.getAbsolutePath());
            //  判断编码格式是否为GB2312（即ANSI），是则转码
            if ("UTF-8".equals(fileEncode)) {
                return false;
            }
            System.out.println("转换:" + fileEncode + " -> " + file.getAbsolutePath());
            TxtConvert.convertCharset(file.getAbsolutePath(), Charset.forName(fileEncode), StandardCharsets.UTF_8, null);
            return false;
        }

        @Override
        public boolean onDir(File file) {
            if (file.listFiles() == null || file.listFiles().length == 0) {
            //    file.delete();
            }
            return false;
        }
    }

}

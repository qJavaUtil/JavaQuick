import blxt.qjava.file.search.FileSearchHelper;
import blxt.qjava.file.search.SearchRes;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileSearchHelperReplace2Test {

    public static void main2(String[] args) {
        File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\Eclipse2022-4\\plugins2\\org.eclipse.cdt.managedbuilder.ui_9.3.200.202204200013");
        File fileTmp = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\Eclipse2022-4\\tmp");
        File filejar = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\Eclipse2022-4\\plugins\\org.eclipse.cdt.managedbuilder.ui_9.3.200.202204200013.jar");
        File filejar2 = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\Eclipse2022-4\\tmp\\new.jar");
        FileSearchHelper fileSearchHelper = new FileSearchHelper();
        fileSearchHelper.setCode(StandardCharsets.UTF_8);

        Map<String, String> replaceMap = new LinkedHashMap<>();
        String s1 = "Create a new Makefile project in a directory containing existing code";
        replaceMap.put(s1, utf8ToUnicode("创建一个MakeFile项目"));
        String s2 = "Managed Build System - per project scanner info profile";
        replaceMap.put(s2, utf8ToUnicode("构建器管理-项目信息配置文件"));

        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        List<SearchRes> res = fileSearchHelper.replace(file, null, replaceMap, false, false);
        for (SearchRes searchRes : res){
            System.out.println("匹配:"+ searchRes.getAbsolutePath(file.getAbsolutePath()));
            try {
             //   Runtime.getRuntime().exec("explorer.exe /select," + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 替换 java
            //ZipUtil.upZip(filejar, filejar2, fileTmp, new File(searchRes.getAbsolutePath(file.getAbsolutePath())));
        }
        System.out.println("替换完成:" +  res != null ? res.size() : 0);
    }


    /**
     * 编码转换
     * @param inStr
     * @return
     */
    public static String utf8ToUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inStr.length(); i++) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(myBuffer[i]);
            if (ub == Character.UnicodeBlock.BASIC_LATIN) {
                //英文及数字等
                sb.append(myBuffer[i]);
            } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                //全角半角字符
                int j = (int) myBuffer[i] - 65248;
                sb.append((char) j);
            } else {
                //汉字
                short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s).replace("ffff", "");
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String str = "修改性质是一种高级的操作, 如果没有最近的项目备份,请不要尝试。";
        System.out.println("转码:"  +FileSearchHelperReplace2Test.utf8ToUnicode(str));
    }

}

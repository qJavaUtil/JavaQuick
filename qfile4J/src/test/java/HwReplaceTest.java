import blxt.qjava.file.search.FileSearchHelper;
import blxt.qjava.file.search.SearchRes;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HwReplaceTest {

    public static void main(String[] args) {
        //File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\doc\\hwhelp\\resources\\app\\build3");
        File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\src\\base");
        FileSearchHelper fileSearchHelper = new FileSearchHelper();
        fileSearchHelper.setCode(StandardCharsets.UTF_8);
        fileSearchHelper.ignoreMap.put(".", 1);
        fileSearchHelper.ignoreMap.put(".o", 2);
        fileSearchHelper.ignoreMap.put(".so", 2);
        fileSearchHelper.ignoreMap.put(".a", 2);
        fileSearchHelper.ignoreMap.put(".ko", 2);
   //     fileSearchHelper.ignoreMap.put(".h", 2);
        fileSearchHelper.ignoreMap.put("BOOT", 3);
        fileSearchHelper.ignoreMap.put("DIRECTORY", 3);
        fileSearchHelper.ignoreMap.put("CPLUSPLUS", 3);
        fileSearchHelper.ignoreMap.put("MEM_MANAGEMENT", 3);
        fileSearchHelper.ignoreMap.put("MODULE", 3);
        fileSearchHelper.ignoreMap.put("STARTUP", 3);
        fileSearchHelper.ignoreMap.put("SYMBOL", 3);
       // fileSearchHelper.ignoreMap.put("LICENSE", 3);

        Map<String, String> replaceMap = new LinkedHashMap<>();
        replaceMap.put("<a class=\"reference internal\" href=\"../00操作系统简介/toc.html\">海鹰翼辉嵌入式操作系统</a>", "<a class=\"reference internal\" href=\"../00操作系统简介/toc.html\" style=\"display: none;\">海鹰翼辉嵌入式操作系统</a>");
        replaceMap.put("海鹰翼辉嵌入式操作系统", "通用验证平台专用操作系统");
        replaceMap.put("海鹰工坊", "专用开发工具");
        replaceMap.put("HIWING RTOS", "通用验证平台");
        replaceMap.put("欢迎这是我们的首页", "欢迎使用通用验证平台");
        replaceMap.put("海鹰工业", "通用验证平台");
        replaceMap.put("HWIIoTStudio", "专用开发工具");
        replaceMap.put("SYLIXOS_BASE_PATH", "HWOS_BASE_PATH");
        replaceMap.put("arm-sylixos-eabi-", "arm-hwiiot-eabi-");
        replaceMap.put("SylixOS Base Project path", "Base Project path");
        replaceMap.put("RealEvo-IDE", "HWIIot Studio");
       // replaceMap.put("2/**", "/**");

        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        List<SearchRes> res = fileSearchHelper.replace(file, null, replaceMap, false, false);

        for (SearchRes searchRes : res){
            System.out.println("匹配:"+ searchRes.getAbsolutePath(file.getParent()));
        }
        System.out.println("替换完成:" +  res != null ? res.size() : 0);
    }


}

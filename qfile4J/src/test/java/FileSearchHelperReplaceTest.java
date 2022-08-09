import blxt.qjava.file.search.FileSearchHelper;
import blxt.qjava.file.search.SearchRes;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileSearchHelperReplaceTest {

    public static void main(String[] args) {
        File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\Eclipse2022-4\\plugins - 副本");
        FileSearchHelper fileSearchHelper = new FileSearchHelper();
        fileSearchHelper.setCode(StandardCharsets.UTF_8);
        fileSearchHelper.ignoreMap.put(".", 1);

        //   fileSearchHelper.ignoreMap.put("Debug", 3);

        Map<String, String> replaceMap = new LinkedHashMap<>();
        replaceMap.put("   中国软件开源组织",
                "中国航天科工集团三院三部");
        replaceMap.put("       嵌入式实时操作系统",
                "海鹰工坊-海鹰工业物联网操作系统");
        replaceMap.put("SylixOS(TM)  LW : long wing", "");
//        replaceMap.put("Copyright All Rights Reserved", "");
        replaceMap.put("RealEvo-IDE", "HWIIoT");
        replaceMap.put("SYLIXOS_BASE_PATH", "HWOS_BASE_PATH");
        replaceMap.put("SylixOSBaseProject", "HwOSBaseProject");
//        replaceMap.put("海鹰高性能操作系统通用处理器版", "海鹰高可靠操作系统");


        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        List<SearchRes> res = fileSearchHelper.replace(file, null, replaceMap, false, false);

        for (SearchRes searchRes : res){
            System.out.println("匹配:"+ searchRes.getAbsolutePath(file.getParent()));
            //  File file1 = new File(searchRes.getAbsolutePath(file.getParent()));
            //  file1.delete();
        }
        System.out.println("替换完成:" +  res != null ? res.size() : 0);
    }


}

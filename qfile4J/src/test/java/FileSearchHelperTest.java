import blxt.qjava.file.callback.FileScanCallback;
import blxt.qjava.file.search.FileSearchHelper;
import blxt.qjava.file.search.SearchRes;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FileSearchHelperTest {
    static FileSearchHelper fileSearchHelper = new FileSearchHelper();
    public static Map<String, Integer> ignoreMap = new HashMap<>();

    static {
        ignoreMap.put("htm", 2);
        ignoreMap.put("html", 2);
        ignoreMap.put("class", 2);
        ignoreMap.put("fdt", 2);
        ignoreMap.put("SF", 2);
        ignoreMap.put("MF", 2);
        ignoreMap.put("txt", 2);
        ignoreMap.put(".jar", 2);
        ignoreMap.put(".sym", 2);
        ignoreMap.put(".png", 2);
        ignoreMap.put(".gif", 2);
        ignoreMap.put(".zip", 2);
        ignoreMap.put(".bmp", 2);
        ignoreMap.put("modules", 2);
        ignoreMap.put(".dll", 2);
        ignoreMap.put(".lib", 2);
        ignoreMap.put(".res", 2);
        ignoreMap.put(".dict", 2);
        ignoreMap.put(".brk", 2);
        ignoreMap.put(".RSA", 2);
        ignoreMap.put(".NOTICE", 2);
        ignoreMap.put(".sample", 2);
        ignoreMap.put(".index", 2);
        ignoreMap.put(".HEAD", 2);
        ignoreMap.put(".master", 2);
        ignoreMap.put(".git", 3);
        ignoreMap.put(".rng", 2);
    //    ignoreMap.put(".xml", 2);
        ignoreMap.put(".xsd", 2);
        ignoreMap.put(".LICENSE", 2);
        ignoreMap.put(".ant", 2);
        ignoreMap.put(".cmd", 2);
//        ignoreMap.put(".properties", 5);
//        ignoreMap.put(".java", 5);
    }


    public static void main(String[] args) {
        //  build();
        //  delete();
        //fand();
          fand();
    }

    public static void build() {
        File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\Eclipse2022-5\\plugins2\\org.eclipse.ui.ide_3.19.0.v20220511-1638");
        fileSearchHelper.setCallback(new MyFileScanCallback(false));
        fileSearchHelper.setCode(StandardCharsets.UTF_8);
        fileSearchHelper.getFileList(file, ".class", null);
        System.out.println("编译完成");
    }

    public static void delete() {
        File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\src\\base");
        //  File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\素材\\搜索结果\\汉化包\\");
        fileSearchHelper.setCallback(new MyFileScanCallback(true));
        fileSearchHelper.setCode(StandardCharsets.UTF_8);
       // fileSearchHelper.getFileList(file, ".class", null);
        fileSearchHelper.getFileList(file, ".c", null);
        //fileSearchHelper.getFileList(file, ".java", null);
        System.out.println("清理完成");
    }

    @SneakyThrows
    public static void fand() {

         File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\Eclipse2022-5\\plugins2");
        //   File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\EclipseNeoncdt64\\plugins4");
        //fileSearchHelper.setCallback(new MyFileScanCallback());
        fileSearchHelper.setCode(StandardCharsets.UTF_8);

        fileSearchHelper.setIgnoreMap(ignoreMap);

        fileSearchHelper.setCallback(null);

        Map<String, String> keys = new HashMap<>();
//        keys.put("Create a new Makefile project", "创建一个MakeFile项目");
//        keys.put("Create a new C or C++ project", "创建一个C/C++项目");
//        keys.put("Makefile Project with Existing Code", "现有MakeFile项目");
//        keys.put("=C/C++ Project", "C/C++项目");
//        keys.put("Large File Associations", "文件关联");
//        keys.put("Default limit", "默认的限制");
//        keys.put("For not listed types", "对于未列出的类型，如果大小超过，则提示编辑器:");
//        keys.put("Core Build Toolchains", "核心编译工具链");
//        keys.put("Toolchains for use with CMake", "工具链使用CMake,Qt，和核心构建Makefile项目");
//        keys.put("Available Toolchains", "可用的工具链");
//        keys.put("User Defined Toolchains", "用户定义的工具链");
//        keys.put("=Refactor", "重构");
//        keys.put("=Navigate", "导航");
//        keys.put("org.eclipse.ui.category.navigate", "null");
//        keys.put("Previous Edit Location", "null");
//        keys.put("org.eclipse.jface.action.IMenuManager", "null");
//        keys.put("菜单可见性", "null");
//        keys.put("Menu Visibility", "null");
//        keys.put("HideMenuItems_menuItemsTab", "null");
//        keys.put("Select the type of target to create.", "null");
//        keys.put("@", "null");
//        keys.put("Text file encoding", "null");
//        keys.put("org.eclipse.ui.ide.workbench", "null");
        keys.put("productName", "null");
        keys.put("appName", "null");
        keys.put("启动程序", "null");

        String ss[] = keys.keySet().toArray(new String[0]);
        String ss2[] =keys.values().toArray(new String[0]);
 
        for (int i = 0; i < ss.length; i++) {
            System.out.println("转码:" + FileSearchHelperReplace2Test.utf8ToUnicode(ss2[i]));
        }

        // 去重
        List<SearchRes> res = fileSearchHelper.search(file, null, null, ss, false, false);
        res = method(res);
        
        for (SearchRes searchRes : res) {
            File f = new File(searchRes.getAbsolutePath(file.getAbsolutePath()));
            System.out.println("匹配:" + searchRes.getKey() + "->" + f.getAbsolutePath());
            Runtime.getRuntime().exec("cmd /c \"D:\\\\Program Files\\\\Notepad++\\\\notepad++.exe\" " + f.getAbsolutePath());
        }
        System.out.println("搜索完成:" + (res != null ? res.size() : 0));
    }

    /**
     * 自定义去重
     * @param list
     */
    public static List<SearchRes> method(List<SearchRes> list) {
        // 新集合
        List<SearchRes> newList = new ArrayList<>(list.size());
        list.forEach(i -> {
            if (!newList.contains(i)) { // 如果新集合中不存在则插入
                newList.add(i);
            }
        });
        return newList;
    }

    static class MyFileScanCallback implements FileScanCallback {

        boolean falDelete = false;

        public MyFileScanCallback(boolean falDelete) {
            this.falDelete = falDelete;
        }

        @Override
        public boolean onFile(int dirDeep,File file) {
            if (falDelete) {
                System.err.println("删除:" + file.delete() + "--" + file.getAbsolutePath());
                return false;
            }
            try {
                String newName = file.getAbsolutePath().replace(".class", ".java");
                newName = newName.replace("plugins2", "plugins4");
                File file1 = new File(newName);
                if(!file1.getParentFile().exists()){
                    if(!file1.getParentFile().mkdirs()){
                        System.err.println("文件夹新建失败:" + file1.getParentFile().getAbsolutePath());
                    }
                }
                Process proc = Runtime.getRuntime().exec(String.format("cmd /c E:\\Desktop\\to_zjl\\jad.exe -p %s>%s", file.getAbsolutePath(), newName), null, null);
                if (file1.exists()) {
                  //  file.delete();
                } else {
                   // file1.delete();
                    System.err.println("反编译失败:" + file.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean onDir(int dirDeep,File file) {
            if (file.listFiles() == null || file.listFiles().length == 0) {
                file.delete();
            }
            return false;
        }
    }


}

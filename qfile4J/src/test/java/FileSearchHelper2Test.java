import blxt.qjava.file.callback.FileScanCallback;
import blxt.qjava.file.search.FileSearchHelper;
import blxt.qjava.file.search.SearchRes;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FileSearchHelper2Test {
    static FileSearchHelper fileSearchHelper = new FileSearchHelper();


    public static void main(String[] args) {
        // delete();
        //    build();
         fand();
    }

    public static void build() {
        File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\EclipseNeoncdt64\\plugins2");
        fileSearchHelper.setCallback(new MyFileScanCallback(false));
        fileSearchHelper.setCode(StandardCharsets.UTF_8);
        fileSearchHelper.getFileList(file, ".class", null);
        System.out.println("编译完成");
    }

    public static void delete() {
        File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\Eclipse2022-4\\plugins3");
        //  File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\素材\\搜索结果\\汉化包\\");
        fileSearchHelper.setCallback(new MyFileScanCallback(true));
        fileSearchHelper.setCode(StandardCharsets.UTF_8);
        fileSearchHelper.getFileList(file, ".class", null);
        //fileSearchHelper.getFileList(file, ".java", null);
        System.out.println("清理完成");
    }

    @SneakyThrows
    public static void fand() {

      File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\hwstudio3\\plugins2");
        //      File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\IdeaLocalSrc2\\.metadata\\.plugins");
        //fileSearchHelper.setCallback(new MyFileScanCallback());
        fileSearchHelper.setCode(StandardCharsets.UTF_8);

        fileSearchHelper.setIgnoreMap(FileSearchHelperTest.ignoreMap);

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
//          keys.put("org.eclipse.ui.category.navigate", "null");
//        keys.put("Previous Edit Location", "null");
//        keys.put("正在启动服务器", "null");
//        keys.put("Build Targets", "构建目标");
//        keys.put("Enable the Launch Bar", "null");
//        keys.put("There are no projects in your workspace", "您的工作空间中没有项目。添加一个项目:");
//        keys.put("Perform Setup Tasks...", "null");
//        keys.put("Manage configurations for the current project", "null");
//        keys.put("Build the active configurations of selected projects", "null");
//        keys.put("Large File Associations", "大文件关联");
//        keys.put("Default limit", "默认限制");
//        keys.put("Link Handlers", "链接处理程序");
 //       keys.put("Link handlers define how your application deals with hyperlinks using a given scheme.", "链接处理程序定义应用程序如何使用给定的模式处理超链接.");
//        keys.put("Install/Update", "安装/更新");
//        keys.put("UI Freeze Monitoring", "UI冻结监听");
//        keys.put("Detect periods of unresponsive UI", "检测UI无响应的周期");
//        keys.put("Log UI freezes to Eclipse error log", "日志UI冻结到Eclipse错误日志");
//        keys.put("Exclude a non-UI thread from the logged message if all its stack frames match the filter", "如果UI线程的堆栈跟踪包含至少一个匹配过滤器的帧，则忽略UI冻结:");
  //      keys.put("Automatic Updates", "自动更新");
 //        keys.put("For not listed types,prompt for editor if size exceeds", "对于未列出的类型，如果大小超过则提示编辑器");
   //     keys.put("Linux Tools Path", "Linux工具路径");
   //     keys.put("Use the System Environment PATH", "使用系统环境变量路径");
   //     keys.put("Projects may have natures assigned to enable additional functionalities", "项目可能具有允许附加功能的特性");
        keys.put("PlaceHolderLaunchConfigurationTabGroup", "null");

        String ss[] = keys.keySet().toArray(new String[0]);
        String ss2[] =keys.values().toArray(new String[0]);
 
        for (int i = 0; i < ss.length; i++) {
            System.out.println("转码:" + ss2[i] + " --> " +FileSearchHelperReplace2Test.utf8ToUnicode(ss2[i]));
        }

        // 去重
        List<SearchRes> res = fileSearchHelper.search(file, null, null, ss, false, false);
        res = method(res);
        
        for (SearchRes searchRes : res) {
            File f = new File(searchRes.getAbsolutePath(file.getAbsolutePath()));
            System.out.println("匹配:" + searchRes.getKey() + "->" + f.getAbsolutePath());
         //   Runtime.getRuntime().exec("explorer.exe /select," + f.getAbsolutePath());
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

        boolean falDelete = true;

        public MyFileScanCallback(boolean falDelete) {
            this.falDelete = falDelete;
        }

        @Override
        public boolean onFile(int dirDeep, File file) {
            if (falDelete) {
                file.delete();
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
        public boolean onDir(int dirDeep, File file) {
            if (file.listFiles() == null || file.listFiles().length == 0) {
                file.delete();
            }
            return false;
        }
    }


}

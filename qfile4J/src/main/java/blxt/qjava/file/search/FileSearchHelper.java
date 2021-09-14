package blxt.qjava.file.search;

import lombok.Data;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 文件搜索工具
 *
 * @author ZhangJieLei
 */
@Data
public class FileSearchHelper {
    /** 系统换行符 **/
    public static String lineSeparator = System.getProperty("line.separator");
    Charset code = StandardCharsets.UTF_8;


    public static void main(String[] args) {
        File file = new File("E:\\ZhangJieLei\\Documents\\workspace\\workProject\\HWIIoTStudio\\src\\base\\HIWING_BASE_1.1.7_For_Intelligent");
        FileSearchHelper fileSearchHelper = new FileSearchHelper();
        fileSearchHelper.setCode(Charset.forName("gbk"));
        fileSearchHelper.ignoreMap.put(".", 1);
        //   fileSearchHelper.ignoreMap.put("Debug", 3);

        Map<String, String> replaceMap = new LinkedHashMap<>();
//        replaceMap.put("   中国软件开源组织",
//            "中国航天科工集团三院三部");
//        replaceMap.put("       嵌入式实时操作系统",
//            "海鹰工坊-海鹰工业物联网操作系统");
//        replaceMap.put("SylixOS(TM)  LW : long wing", "");
////        replaceMap.put("Copyright All Rights Reserved", "");
//        replaceMap.put("RealEvo-IDE", "HWIIoT");
        replaceMap.put("SYLIXOS_BASE_PATH", "HWOS_BASE_PATH");
        replaceMap.put("SylixOSBaseProject", "HwOSBaseProject");
//        replaceMap.put("海鹰高性能操作系统通用处理器版", "海鹰高可靠操作系统");


        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        // List<SearchRes> res = fileSearchHelper.search(file, null, "海鹰翼辉", false, false);

        List<SearchRes> res = fileSearchHelper.replace(file, null, replaceMap, false, false);

        System.out.println("替换完成:" +  res != null ? res.size() : 0);
        for (SearchRes searchRes : res){
            System.out.println("匹配:"+ searchRes);
        }
    }


    private List<String> pathlist = new ArrayList<>(200);
    /** 忽略列表 */
    public Map<String, Integer> ignoreMap = new HashMap<>();


    public FileSearchHelper() {

    }

    public FileSearchHelper(Integer leng) {
        pathlist = new ArrayList<>(leng);
    }


    public List<SearchRes> search(File dir, String suffix, String searchStr, boolean ingronCase, boolean regular) {
        List<String> pathlist = getFileList(dir, suffix);
        if(pathlist == null){
            return new ArrayList<>(0);
        }
        int lengpath = dir.getParent().length() + 1;
        List<SearchRes> result = new ArrayList<>(100);
        for (int k = 0; k < pathlist.size(); k++) {
            File file = new File(pathlist.get(k));
            if (file.exists()) {
                /* 读取数据 */
                try {
                    BufferedReader br = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file.getAbsolutePath()), StandardCharsets.UTF_8));
                    // 文件行内容
                    String lineTxt = null;
                    // 行号
                    int second_count = 0;
                    while ((lineTxt = br.readLine()) != null) {
                        second_count++;
                        if (lineTxt.length() < searchStr.length()) {
                            // 跳过空行
                            continue;
                        }
                        // 判断是否存在, 大小写处理
                        boolean  isFind = ingronCase ?
                            lineTxt.toLowerCase().contains((searchStr))
                            : lineTxt.contains(searchStr);
                        if (isFind) {
                            SearchRes searchRes = new SearchRes();
                            searchRes.setIndex(FileSearchHelper.getIndexOf(lineTxt, searchStr) + 1);
                            searchRes.setFileName(file.getName());
                            searchRes.setLine(second_count);
                            searchRes.setKey(searchStr);
                            searchRes.setPath(file.getParent().substring(lengpath));
                            searchRes.setContent(lineTxt);
                            result.add(searchRes);
                        }
                    }
                    br.close();
                } catch (Exception e) {
                    System.err.println("搜索错误:" + e.getMessage());
                }
            }
        }
        return result;
    }

    /**
     * 文件内容替换
     * @param dir           文件夹
     * @param suffix        文件后缀
     * @param replaceMap    要替换的 Map<String, String>
     * @param ingronCase    是否忽略大小写
     * @param regular       使用正则
     * @return
     */
    public List<SearchRes> replace(File dir, String suffix, Map<String, String> replaceMap,
                                   boolean ingronCase,  boolean regular) {
        List<String> pathlist = getFileList(dir, suffix);
        if (pathlist == null) {
            return null;
        }
        List<SearchRes> result = new ArrayList<>();
        for (int k = 0; k < pathlist.size(); k++) {
            File file = new File(pathlist.get(k));
            if (!file.exists()) {
                continue;
            }
            // 自动识别编码
            try {
                code = Charset.forName(code(file));
            } catch (Exception e) {
                code = Charset.forName("gbk");
                e.printStackTrace();
            }
            // 文件备份
            StringBuilder strBuffer = new StringBuilder();
            String path = String.format("%s%s%s%s", File.separator, dir.getParentFile().getName(), File.separator,
                file.getAbsolutePath().substring(dir.getAbsolutePath().length() + 1));
            /* 读取数据 */
            try {
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file.getAbsolutePath()), code));
                // 文件行内容
                String lineTxt = null;
                // 行号
                int line = 0;
                boolean isSearch = false;
                //读取每一行
                while ((lineTxt = br.readLine()) != null) {
                    line++;
                    for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
                        // 判断是否存在, 大小写处理
                        boolean isFind = ingronCase ? lineTxt.toLowerCase().contains((entry.getKey()))
                            : lineTxt.contains(entry.getKey());
                        if (isFind) {
                            // 替换后的内容
                            isSearch = true;
                            SearchRes searchRes = new SearchRes();
                            searchRes.setIndex(getIndexOf(lineTxt, entry.getKey()) + 1);
                            searchRes.setFileName(file.getName());
                            searchRes.setLine(line);
                            searchRes.setKey(entry.getKey());
                            searchRes.setPath(path);
                            searchRes.setContent(lineTxt);
                            result.add(searchRes);
                            if (lineTxt.contains(entry.getKey())) {
                                if(regular){
                                    // 正则替换
                                    lineTxt = lineTxt.replaceAll(entry.getKey(), entry.getValue());
                                }
                                else{
                                    // 简单替换
                                    lineTxt = lineTxt.replace(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                    }
                    // 更新内容
                    strBuffer.append(lineTxt);
                    // 换行符
                    strBuffer.append(lineSeparator);
                }
                br.close();
                // 找到就更新
                if(isSearch) {
                    save(file, strBuffer.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 根据文件路径走搜索目录下的指定文件
     *
     * @param dirFile
     * @param suffix
     * @return
     */
    public List<String> getFileList(File dirFile, String suffix) {
        // 获取子文件
        File[] files = dirFile.listFiles();

        if (files == null) {
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            // 忽略
            if(isIgnore(files[i])){
                continue;
            }
            // 判断是文件还是文件夹
            if (files[i].isDirectory()) {
                // 获取文件绝对路径
                getFileList(files[i], suffix);
            } else if (suffix == null) {
                String strFileName = files[i].getAbsolutePath();
                pathlist.add(strFileName);
            } else if (fileName.endsWith(suffix)) {
                // 判断文件名是否以指定结尾
                String strFileName = files[i].getAbsolutePath();
                pathlist.add(strFileName);
            }

        }
        return pathlist;
    }

    /**
     * 是否忽略文件
     * @param file
     * @return
     */
    private boolean isIgnore(File file){
        String fileName = file.getName();
        Set<Map.Entry<String, Integer>> set  = ignoreMap.entrySet();
        for(Map.Entry<String, Integer> entry : set){
            // 忽略前缀
            if(entry.getValue() == 1 && fileName.startsWith(entry.getKey())){
                return true;
            }
            // 忽略后缀
            if(entry.getValue() == 2 && fileName.endsWith(entry.getKey())){
                return true;
            } // 全匹配
            if(entry.getValue() == 3 && fileName.equals(entry.getKey())){
                return true;
            }
        }

        return false;
    }

    /**
     * KMP算法 字符串匹配
     * @param s1
     * @param s2
     * @return
     */
    private static int getIndexOf(String s1, String s2) {
        if (s1 == null || s2 == null || s2.length() < 1 || s1.length() < s2.length()) {
            return -1;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int x = 0;
        int y = 0;
        // O(M) m <= n
        int[] next = getNextArray(str2);
        // O(N)
        while (x < str1.length && y < str2.length) {
            if (str1[x] == str2[y]) {
                x++;
                y++;
                // y == 0
            } else if (next[y] == -1) {
                x++;
            } else {
                y = next[y];
            }
        }
        return y == str2.length ? x - y : -1;
    }

    private static int[] getNextArray(char[] str2) {
        if (str2.length == 1) {
            return new int[]{-1};
        }
        int[] next = new int[str2.length];
        next[0] = -1;
        next[1] = 0;
        // 目前在哪个位置上求next数组的值
        int i = 2;
        // 当前是哪个位置的值再和i-1位置的字符比较
        int cn = 0;
        while (i < next.length) {
            // 配成功的时候
            if (str2[i - 1] == str2[cn]) {
                next[i++] = ++cn;
            } else if (cn > 0) {
                cn = next[cn];
            } else {
                next[i++] = 0;
            }
        }
        return next;
    }

    public boolean save(File file, String filecontent) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;

        boolean var5;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos, code);
            osw.write(filecontent);
            osw.close();
            return true;
        } catch (Exception var15) {
            var5 = false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    osw.close();
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
            }

        }

        return var5;
    }

    /**
     * @param file      原始文件
     * @param outPath   输出文件
     * @param strBuffer 文件内容
     */
    public void copyFile(File file, File outPath, String strBuffer)
        throws FileNotFoundException, UnsupportedEncodingException {
        // 检查输出目录是否存在，若不存在先创建
        if (!outPath.exists()) {
            outPath.mkdirs();
        }
        // 替换后输出文件路径
        PrintWriter printWriter = new PrintWriter(outPath + "\\" + file.getName(),
            String.valueOf(StandardCharsets.UTF_8));
        //又一次写入
        printWriter.write(strBuffer);
        printWriter.flush();
        printWriter.close();
    }

    /**
     * 获取文件编码格式
     * @param file
     * @return
     * @throws Exception
     */
    public static String code(File file) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
        int p = (bin.read() << 8) + bin.read();
        bin.close();
        switch (p) {
            case 0xefbb:
                return  "UTF-8";
            case 0xfffe:
                return "Unicode";
            case 0xfeff:
                return "UTF-16BE";
            default:
                return "GBK";
        }

    }

}

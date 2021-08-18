package blxt.qjava.file.search;

import blxt.qjava.file.search.SearchRes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author OpenJialei
 * @date 2021年08月12日 17:50
 */
public class QFileScaner {

    /**
     * 文件搜索
     * @param fileBase    根文件
     * @param fileName    搜索文件夹, 更文件的子文件夹名称
     * @param suffix      文件后缀, 可以为null
     * @param searchStr   搜索词
     * @param ingronCase  是否大小写
     * @return
     */
    public List<SearchRes> searchFile(File fileBase, String fileName, String suffix, String searchStr, boolean ingronCase) {
        List<SearchRes> result = new ArrayList<>();

        File fileSearchBase = new File(fileBase, fileName);

        // 忽略大小写
        if (ingronCase) {
            searchStr = searchStr.toLowerCase();
        }

        int fileBaseLength = Math.toIntExact(fileBase.toString().length());

        List<String> pathlist = getFileList(fileSearchBase, suffix);
        if(pathlist == null){
            return null;
        }
        for (int k = 0; k < pathlist.size(); k++) {
            File file = new File(pathlist.get(k));
            if (file.exists()) {

                String path = file.getAbsolutePath().substring(fileBaseLength+1);
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
                        boolean  isFind = ingronCase ? lineTxt.toLowerCase().contains((searchStr))
                            : lineTxt.contains(searchStr);
                        if (isFind) {
                            SearchRes searchRes = new SearchRes();
                            searchRes.setIndex(getIndexOf(lineTxt, searchStr) + 1);
                            searchRes.setFileName(path);
                            searchRes.setLine(second_count);
                            searchRes.setKey(searchStr);
                            searchRes.setContent(lineTxt);
                            result.add(searchRes);
                        }
                    }
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * 根据文件路径走搜索目录下的指定文件
     *
     * @param dirFile  目录
     * @param suffix   文件后缀, 可为null
     * @return
     */
    public static List<String> getFileList(File dirFile, String suffix) {
        // 获取子文件
        File[] files = dirFile.listFiles();
        List<String> pathlist = new ArrayList<>(files.length);
        if (files == null) {
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
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
            } else {
                continue;
            }
        }
        return pathlist;
    }

    // KMP算法 字符串匹配
    public static int getIndexOf(String s1, String s2) {
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
            } else if (next[y] == -1) { // y == 0
                x++;
            } else {
                y = next[y];
            }
        }
        return y == str2.length ? x - y : -1;
    }

    public static int[] getNextArray(char[] str2) {
        if (str2.length == 1) {
            return new int[]{-1};
        }
        int[] next = new int[str2.length];
        next[0] = -1;
        next[1] = 0;
        int i = 2; // 目前在哪个位置上求next数组的值
        int cn = 0; // 当前是哪个位置的值再和i-1位置的字符比较
        while (i < next.length) {
            if (str2[i - 1] == str2[cn]) { // 配成功的时候
                next[i++] = ++cn;
            } else if (cn > 0) {
                cn = next[cn];
            } else {
                next[i++] = 0;
            }
        }
        return next;
    }
}

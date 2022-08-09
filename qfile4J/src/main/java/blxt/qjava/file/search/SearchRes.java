package blxt.qjava.file.search;

import lombok.Data;

import java.io.File;

/**
 * 搜索结果
 * @author OpenJialei
 * @date 2021年08月12日 18:05
 */

@Data
public class SearchRes{

    /** 文件名 */
    String fileName;
    /** 行号 */
    Integer line;
    /** 列 */
    Integer index;
    /** 关键词 */
    String key;
    /** 本行内容 */
    String content;
    /** 相对路径,包名 */
    String path;


    public String getAbsolutePath(String parentFile) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(parentFile);
        if(!path.startsWith(File.separator)){
            stringBuilder.append(File.separator);
        }
        stringBuilder.append(path);

        return stringBuilder.toString();
    }


    @Override
    public boolean equals(Object o) {
        if(o==null) {
            return false;
        }
        SearchRes obj = (SearchRes)o;
        if(path.equals(obj.path)) {
            return true;
        }
        return false;
    }
}

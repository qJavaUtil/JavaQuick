package blxt.qjava.file.search;

import lombok.Data;

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
}

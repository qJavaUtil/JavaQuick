package test;

import blxt.qjava.excel.bean.ExcelBineName;
import lombok.Data;

/**
 * @author OpenJialei
 * @date 2021年08月21日 17:39
 */
@Data
public class Student {

    @ExcelBineName(column = "序号")
    int iid;

    @ExcelBineName(column = "姓名")
    String name;

    @ExcelBineName(column = "性别")
    String xb;

    @ExcelBineName(column = "排名", format="%.2f%%")
    float sg;

    @ExcelBineName(column = "住址")
    String wz;

    @ExcelBineName(column = "身份证号")
    String sfzh;

    /** 对照 */
    int test;
}

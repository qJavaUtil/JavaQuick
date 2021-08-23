package test;

import blxt.qjava.excel.ExcelHelper;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 导出测试 测试
 * @author OpenJialei
 * @date 2021年08月21日 17:37
 */
public class ExcelHelperTest {

    /**
     * 简单通用导出
     * @param
     */
    public void testSimple() {

        // 声明工具
        ExcelHelper excelHelper = new ExcelHelper();
        File file = new File("./test.xls");
        //表头数据
        String[] header = {"ID", "姓名", "性别", "年龄", "地址", "分数"};

        //数据内容
        Object[] student1 = {1, "小红", "女", 12.98, "成都青羊区", "96"};
        Object[] student2 = {2, "小强", "男", 12.6, "成都金牛区", "91"};
        Object[] student3 = {3, "小明", "男", 12.0, "成都武侯区", "341721199701233465456463111551"};

        List<Object[]> datas = new ArrayList<>();
        datas.add(student1);
        datas.add(student2);
        datas.add(student3);

        /// 行 单元格格式
        HSSFCellStyle[] styles = excelHelper.getStyles(datas.get(0));
        String[] format = null;

        try {
            // 创建sheet
            excelHelper.createSimpleTable( "学生", header, datas, styles, format);
            excelHelper.createSimpleTable( "学生2", header, datas, styles, format);

            // 创建文件流
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            // 输出
            excelHelper.write(fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对象导出测试
     * */
    public void testObj() {
        // 创建工具类
        ExcelHelper excelHelper = new ExcelHelper();
        // 表头
        String[] header = excelHelper.getHeads(Student.class);
        // 内容
        List<Student> datas = getdatas();
        // 单元格格式
        HSSFCellStyle[] styles = excelHelper.getStyles(Student.class);
        // 文本格式
        String[] format = excelHelper.getFormat(Student.class);

        try {
            // 创建sheet
            excelHelper.createObjectTable( "学生", header, datas, styles, format);
            excelHelper.createObjectTable( "学生", header, datas, styles, format);

            File file = new File("./test2.xls");
            // 创建文件流
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            // 输出
            excelHelper.write(fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();
            // 也可以 写到 HttpServletResponse response.getOutputStream() 中
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入测试
     */
    public void addtest(){
        // 获取指定表
        HSSFWorkbook hssfWorkbook = ExcelHelper.readExcel(new File("./test2.xls"));
        // 创建工具类, 从模板创建
        ExcelHelper excelHelper = new ExcelHelper(hssfWorkbook);
        // 数据
        String[] datas = {"测试", "测试", "测试", "测试", "测试", "测试"};
        // 写入表格
        excelHelper.addRow("学生", datas, 2, 1);

        // 写入到文件
        excelHelper.write(new File("./test2.xls"));
    }

    /**
     * 批量写入行测试
     */
    public void addListtest(){
        // 获取指定表
        HSSFWorkbook hssfWorkbook = ExcelHelper.readExcel(new File("./test2.xls"));
        // 创建工具类, 从模板创建
        ExcelHelper excelHelper = new ExcelHelper(hssfWorkbook);
        // 数据
        List<Object[]> datas = new ArrayList<>();
        datas.add(new String[]{"测试", "测试", "测试", "测试", "测试", "测试"});
        datas.add(new String[]{"测试", "测试", "测试", "测试", "测试", "测试"});
        datas.add(new String[]{"测试", "测试", "测试", "测试", "测试", "测试"});
        datas.add(new String[]{"测试", "测试", "测试", "测试", "测试", "测试"});
        datas.add(new String[]{"测试", "测试", "测试", "测试", "测试", "测试"});
        datas.add(new String[]{"测试", "测试", "测试", "测试", "测试", "测试"});
        datas.add(new String[]{"测试", "测试", "测试", "测试", "测试", "测试"});

        // 写入表格
        excelHelper.addRow("学生", datas, 2, 1, null);

        // 写入到文件
        excelHelper.write(new File("./test2.xls"));
    }



    public static void main(String[] args) {

        ExcelHelperTest excelHelperTest = new ExcelHelperTest();
        excelHelperTest.testObj();
        excelHelperTest.addtest();
        excelHelperTest.addListtest();
    }


    /**
     * 获取数据
     * @return
     */
    public  List<Student> getdatas(){
        List<Student> datas = new ArrayList<>(10);
        for (int i = 0; i < 10; i++){
            Student student = new Student();
            student.setIid(i);
            student.setName(String.format("张%d头", i));
            student.setXb( i % 2 == 0 ? "男" : "女");
            student.setSg((float) (10 * Math.random()));
            student.setWz("rwqnrfreawerk阿斯顿反馈");
            student.setSfzh("132132325654164165443545415154854543");
            datas.add(student);
        }
        return datas;
    }

}

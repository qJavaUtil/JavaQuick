package blxt.qjava.excel;

import blxt.qjava.autovalue.reflect.PackageUtil;
import blxt.qjava.excel.bean.ExcelBineName;
import org.apache.poi.hssf.usermodel.*;

import java.io.*;
import java.lang.reflect.Field;
import java.security.Timestamp;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * excel表格 导出工具
 * @author OpenJialei
 * @date 2021年08月21日 15:40
 */
public class ExcelHelper {
    /** 工作簿 **/
    HSSFWorkbook workbook = new HSSFWorkbook();

    /** 整型类型 */
    String formatString = "TEXT";
    /** 整型类型 */
    String formatInt = "0";
    /** 浮点类型*/
    String formatDouble = "0.00";
    /** 时间类型*/
    String formatTime = "yyyy年m月d日";

    public ExcelHelper(){

    }

    /**
     * 模板
     * @param workbook
     */
    public ExcelHelper(HSSFWorkbook workbook) {
        this.workbook = workbook;
    }



    /**
     * 根据对象 创建简单表格, 按行写入对象集合
     * @param sheetName
     * @param header
     * @param datas
     * @param styles
     * @param format     内容格式
     * @return
     * @throws Exception
     */
    public boolean createObjectTable(String sheetName,
                                     String[] header,
                                     List datas,
                                     HSSFCellStyle[] styles,
                                     String[] format)  throws Exception {

        Class<?> classz = datas.get(0).getClass();
        // 获取f对象对应类中的所有属性域
        List<Field> fieldList = PackageUtil.getfieldList(classz);
        // 有效字段
        List<Field> fieldRes = new ArrayList<>(fieldList.size());

        // 遍历属性
        for (Field field : fieldList) {
            // 过滤 final 元素
            if ((field.getModifiers() & 16) != 0) {
                continue;
            }
            ExcelBineName bineName = field.getAnnotation(ExcelBineName.class);
            if(bineName == null){
                continue;
            }
            fieldRes.add(field);
        }

        // 获取 行数据
        List<Object[]> datasValues = new ArrayList<>(datas.size());
        // 遍历对象
        for(Object bean : datas){
            // 遍历对象的值
            Object[] datasValue = new Object[fieldRes.size()];
            int count = 0;
            for (Field field : fieldRes) {
                boolean accessFlag = field.isAccessible();
                field.setAccessible(true);
                datasValue[count] = field.get(bean);
                field.setAccessible(accessFlag);
                count++;
            }
            // 把对象的元素值, 塞入行集合
            datasValues.add(datasValue);
        }

        return createSimpleTable(sheetName, header, datasValues, styles, format);
    }

    /**
     * 创建简单表格, 按行写入
     * @param sheetName  sheetName名
     * @param header     头
     * @param datas      行内容
     * @param styles     列单元格格式
     * @param format     内容格式
     * @return
     */
    public boolean createSimpleTable(String sheetName,
                                     String[] header,
                                     List<Object[]> datas,
                                     HSSFCellStyle[] styles,
                                     String[] format) throws Exception {

        //生成一个表格，设置表格名称为 sheetName, 不存在就创建
        HSSFSheet sheet = null;
        if(workbook.getSheetIndex(sheetName) < 0){
            sheet = workbook.createSheet(sheetName);
        }
        else{
            sheet = workbook.getSheet(sheetName);
        }

        //创建第一行表头
        HSSFRow headrow = sheet.createRow(0);

        // 使用默认单元格格式
        if(styles == null){
            styles = new HSSFCellStyle[header.length];
        }
        if(format == null){
            format = new String[header.length];
        }

        if (styles.length !=  header.length || format.length !=  header.length){
            throw new Exception("表头和列单元格格式长度不一致");
        }


        //遍历添加表头
        for (int i = 0; i < header.length; i++) {
            //创建一个单元格
            HSSFCell cell = headrow.createCell(i);
            //创建一个内容对象
            HSSFRichTextString text = new HSSFRichTextString(header[i]);
            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
            if(styles[i] == null){
                styles[i] = workbook.createCellStyle();
            }
            if(styles[i] == null){
                format[i] = "%s";
            }
        }


        //遍历结果集，把内容加入表格
        int count = 1;
        for(Object[] objects : datas){
            HSSFRow row1 = sheet.createRow(count);
            for (int i = 0; i < objects.length; i++) {
                HSSFCell cell = row1.createCell(i);
                String value = null;
                if(format[i] != null){
                    value = String.format(format[i],objects[i]);
                }
                else{
                    value = String.valueOf(objects[i]);
                }
                HSSFRichTextString text = new HSSFRichTextString(value);
                cell.setCellStyle(styles[i]);
                cell.setCellValue(text);
            }
            count++;
        }

        return true;
    }

    /**
     * 写到指定行
     * @param sheetName   工作簿
     * @param datas       数据
     * @param row         行号
     * @param index       列号
     * @return
     */
    public boolean addRow(String sheetName,
                          String[] datas,
                          int row,
                          int index){
        //获取工作簿
        HSSFSheet sheet = null;
        if(workbook.getSheetIndex(sheetName) < 0){
            // 工作簿不存在
           return false;
        }
        sheet = workbook.getSheet(sheetName);
        // 获取 行
        HSSFRow hssfRow = sheet.getRow(row);
        if(hssfRow == null){
            sheet.createRow(row);
        }

        // 写入表格
        for (int i = 0; i < datas.length; i++) {
            // 获取一个单元格
            HSSFCell cell = hssfRow.getCell(index + i);
            if(cell == null){
                cell = hssfRow.createCell(index + i);
            }
            // 创建一个内容对象
            HSSFRichTextString text = new HSSFRichTextString(datas[i]);
            // 将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
        }

        return true;
    }

    /**
     * 写到指定行
     * @param sheetName   工作簿
     * @param datas       数据集合
     * @param row         开始行号
     * @param index       开始列号
     * @param format      数据格式
     * @return
     */
    public boolean addRow(String sheetName,
                          List<Object[]> datas,
                          int row,
                          int index,
                          String[] format){
        //获取工作簿
        HSSFSheet sheet = null;
        if(workbook.getSheetIndex(sheetName) < 0){
            // 工作簿不存在
            return false;
        }
        sheet = workbook.getSheet(sheetName);

        // 遍历数据
        int rowCount = 0;
        for(Object[] data : datas){
            // 获取 行
            HSSFRow hssfRow = sheet.getRow(row + rowCount);
            if(hssfRow == null){
                sheet.createRow(row + rowCount);
            }
            // 写入表格
            for (int i = 0; i < data.length; i++) {
                // 获取一个单元格
                HSSFCell cell = hssfRow.getCell(index + i);
                if(cell == null){
                    cell = hssfRow.createCell(index + i);
                }
                // 获取值
                String value = null;
                if(format != null && format[i] != null){
                    value = String.format(format[i],data[i]);
                }
                else{
                    value = String.valueOf(data[i]);
                }
                // 创建一个内容对象
                HSSFRichTextString text = new HSSFRichTextString(value);
                // 将内容对象的文字内容写入到单元格中
                cell.setCellValue(text);
            }
            // 行号增加
            rowCount++;
        }

        return true;
    }

    /**
     * 获取 表头
     * @return
     */
    public String[] getHeads(Class<?> classz) {
        // 获取f对象对应类中的所有属性域
        List<Field> fieldList = PackageUtil.getfieldList(classz);
        // 存放结果
        List<String> fieldRes = new ArrayList<>(fieldList.size());

        // 遍历属性
        for (Field field : fieldList) {
            // 过滤 final 元素
            if ((field.getModifiers() & 16) != 0) {
                continue;
            }
            ExcelBineName bineName = field.getAnnotation(ExcelBineName.class);
            if(bineName == null){
                continue;
            }
            String column = bineName.column();
            fieldRes.add(column);
        }
        return fieldRes.toArray(new String[fieldRes.size()]);
    }

    /**
     * 通过对象类型获取 HSSFCellStyle
     * 更多格式, 看 @links{org.apache.poi.ss.usermodel.BuiltinFormats._formats}
     * @param objects
     * @return
     */
    public HSSFCellStyle[] getStyles(Object ...objects){
        HSSFCellStyle[] styles = new HSSFCellStyle[objects.length];
        HSSFDataFormat format = workbook.createDataFormat();

        //遍历添加表头
        for (int i = 0; i < objects.length; i++) {
            styles[i] = workbook.createCellStyle();
            if(objects[i] instanceof String){
                styles[i].setDataFormat(format.getFormat(formatString));
            }
            else if(objects[i] instanceof Integer){
                styles[i].setDataFormat(HSSFDataFormat.getBuiltinFormat(formatInt));
            }
            else if(objects[i] instanceof Double || objects[i] instanceof Float){
                styles[i].setDataFormat(format.getFormat(formatDouble));
            }
            else if(objects[i] instanceof Time
                || objects[i] instanceof Date
                || objects[i] instanceof Timestamp){
                styles[i].setDataFormat(format.getFormat(formatTime));
            }
        }


        return styles;
    }

    /**
     * 从类自动 获取单元格格式
     * @param classz
     * @return
     */
    public HSSFCellStyle[] getStyles(Class<?> classz) {
        // 获取f对象对应类中的所有属性域
        List<Field> fieldList = PackageUtil.getfieldList(classz);

        HSSFDataFormat format = workbook.createDataFormat();
        List<HSSFCellStyle> styles = new ArrayList<>(fieldList.size());

        // 遍历属性
        for (Field field : fieldList) {
            // 过滤 final 元素
            if ((field.getModifiers() & 16) != 0) {
                continue;
            }
            ExcelBineName bineName = field.getAnnotation(ExcelBineName.class);
            if(bineName == null){
                continue;
            }
            // 自定义单元格格式
            HSSFCellStyle style = workbook.createCellStyle();
            style.setDataFormat(format.getFormat(bineName.style()));
            styles.add(style);
        }

        return styles.toArray(new HSSFCellStyle[styles.size()]);
    }



    /**
     * 获取文本格式
     * @param classz
     * @return
     */
    public String[] getFormat(Class<?> classz) {
        // 获取f对象对应类中的所有属性域
        List<Field> fieldList = PackageUtil.getfieldList(classz);
        List<String> styles = new ArrayList<>(fieldList.size());

        // 遍历属性
        for (Field field : fieldList) {
            // 过滤 final 元素
            if ((field.getModifiers() & 16) != 0) {
                continue;
            }
            ExcelBineName bineName = field.getAnnotation(ExcelBineName.class);
            if(bineName == null){
                continue;
            }
            String format = bineName.format();
            if(!"null".equals(format)){
                styles.add(format);
                continue;
            }
            if(field.getType().equals(Integer.class) || field.getType().equals(int.class)){
                format = "%d";
            }
            else if(field.getType().equals(Float.class) || field.getType().equals(float.class)){
                format = "%.2f";
            }
            else if(field.getType().equals(Double.class) || field.getType().equals(double.class)){
                format = "%.2f";
            }
            else if(field.getType().equals(String.class)){
                format = "%s";
            }
            styles.add(format);
        }
        return styles.toArray(new String[styles.size()]);
    }

    /**
     * 从本地从文件获取一个 表格
     * @param file
     * @return
     */
    public static HSSFWorkbook readExcel(File file) {
        InputStream in = null;
        HSSFWorkbook work = null;
        try {
            in = new FileInputStream(file);
            work = new HSSFWorkbook(in);
        } catch (FileNotFoundException e) {
            System.out.println("文件路径错误");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("文件输入流错误");
            e.printStackTrace();
        }
        return work;
    }


    /**
     * 输出到文件流
     * @param stream  OutputStream
     * @return
     */
    public boolean write(OutputStream stream){
        // 写入文件流
        try {
            workbook.write(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 写入
     * @param file
     * @return
     */
    public boolean write(File file){
        try {
            // 创建文件流
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            // 输出
            write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            // 也可以 写到 HttpServletResponse response.getOutputStream() 中
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}

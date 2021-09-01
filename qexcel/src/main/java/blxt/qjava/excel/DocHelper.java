package blxt.qjava.excel;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 文档导出工具
 * @author OpenJialei
 * @date 2021年08月22日 22:43
 */
@Slf4j
public class DocHelper {
    XWPFDocument document = null;

    public DocHelper(File docFile) {
        this.document = read(docFile);
    }

    public DocHelper(XWPFDocument document) {
        this.document = document;
    }


    /**
     * 替换文档中的段落
     * 注意: 模板中替换的key, 需要按照指定格式: 文字替换: ${key}, 图片替换 @{key}
     * 注意: 模板中的key, 需要是段落格式完全一致的,建议中记事本中复制. 如果key是中英文的, 需要格式刷统一一下
     * @param params    键值对 map
     * @throws IOException
     * @throws InvalidFormatException
     */
    public boolean replaceWord(Map<String,Object> params){
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        if(paragraphs == null || paragraphs.isEmpty()){
            return false;
        }

        // 遍历所有段落
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            // 遍历段落里面的小段
            for (XWPFRun run : runs) {
                log.debug("段落:{}", run.text());
                if(strMatcher(run.text()).find()){
                    replaceInStr(run, params);
                }
            }
        }

        return true;
    }


    /**
     * 替换文档中的表格
     * 注意: 模板中替换的key, 需要按照指定格式: 文字替换: ${key}, 图片替换 @{key}
     * @param params    键值对 map
     * @throws IOException
     * @throws InvalidFormatException
     */
    public boolean replaceTable(Map<String,Object> params)
        throws IOException, InvalidFormatException {
        List<XWPFTableRow> rows=null;
        List<XWPFTableCell> cells=null;

        List<XWPFTable> tables=document.getTables();

        if(tables == null || tables.isEmpty()){
            return false;
        }

        //遍历这个word文档的所有table表格
        for (XWPFTable table :  tables) {
            rows=table.getRows();
            //遍历这个表格的所有行
            for (XWPFTableRow row : rows) {
                cells=row.getTableCells();
                //遍历这一行的单元格
                for (XWPFTableCell cell: cells) {
                    //判断该单元格的内容是否是字符串字段
                    if(strMatcher(cell.getText()).find()){
                        //替换字符串 字符串可以多行 也可以一行
                        replaceInStr(cell,params);
                        continue;
                    }
                    //判断该单元格内容是否是需要替换的图片
                    if(imgMatcher(cell.getText()).find()){
                        //把模板中的内容替换成图片 图片可以多涨
                        replaceInImg(cell,params);
                        continue;
                    }
                }
            }
        }

        return true;
    }

    /**
     * 指定表格, 追加行
     * @param tableIndex   第几个表格
     * @param row          开始行号
     * @param datas        数据行(注意数据数组长度要和单元格一样)
     * @return
     */
    public boolean addTable(int tableIndex, int row, List<String[]> datas){
        List<XWPFTable> tables=document.getTables();
        if(tables == null || tables.isEmpty() || tables.size() < tableIndex){
            return false;
        }
        XWPFTable table = tables.get(tableIndex);
        if(table == null){
            return false;
        }
        // 表格行
        List<XWPFTableRow> rows=table.getRows();
        // 表格列
        List<XWPFTableCell> cells = null;

        int idata = 0;
        int dataLeng = datas.size();
        while(idata < dataLeng){
            // 如果原表格的行不够, 就新建
            XWPFTableRow rowN = null;
            if(row + idata >= rows.size()){
                rowN = table.createRow();
                log.debug("新建行");
            }
            else{
                rowN = table.getRow(row + idata);
                log.debug("写入行");
            }
            // 单元格
            cells=rowN.getTableCells();
            for(int i= 0,  leng =  cells.size(); i < leng; i++){
                String value = datas.get(idata)[i];
                cells.get(i).setText(value);
            }

            idata++;
        }

        return true;
    }
    // TODO 替换标题


    /**
     * 返回模板中图片字符串的匹配Matcher类
     * @param imgstr
     * @return
     */
    private Matcher imgMatcher(String imgstr){
        Pattern pattern= Pattern.compile("@\\{(.+?)\\}");
        Matcher matcher=pattern.matcher(imgstr);
        return matcher;
    }

    /**
     * 返回模板中变量的匹配Matcher类
     * @param str
     * @return
     */
    private  Matcher strMatcher(String str){
        Pattern pattern=Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher=pattern.matcher(str);
        return matcher;
    }

    /**
     * 替换段落 键值
     * @param cell
     * @param params
     */
    private  void replaceInStr(XWPFRun cell, Map<String,Object> params) {
        //两种数据类型  一种是直接String  还有一种是List<String>
        String value = cell.text();

        for(Map.Entry<String, Object> entry : params.entrySet()){
            String key = String.format("${%s}", entry.getKey());
            if(value.contains(key)){
                key = String.format("\\$\\{%s}", entry.getKey());
                value = value.replaceAll(key, entry.getValue().toString());
                cell.setText(value,0);
            }
        }
    }

    /**
     * 替换模板Table中相应字段为对应的字符串值
     * @param cell
     * @param params
     */
    private  void replaceInStr(XWPFTableCell cell, Map<String,Object> params) {
        //两种数据类型  一种是直接String  还有一种是List<String>
        String key = cell.getText().substring(2, cell.getText().length() - 1);

        Integer datatype = getMapStrDataTypeValue(params, key);

        List<XWPFParagraph> parags = cell.getParagraphs();
        //先清空单元格中所有的段落
        for (int i = 0; i < parags.size(); i++) {
            cell.removeParagraph(i);
        }
        if (datatype.equals(0)) {
            return;
        } else if (datatype.equals(2)) {
            //如果类型是2 说明数据类型是List<String>
            List<String> strs = (List<String>) params.get(key);
            Iterator<String> iterator = strs.iterator();
            while (iterator.hasNext()) {
                XWPFParagraph para = cell.addParagraph();
                XWPFRun run = para.createRun();
                run.setText(iterator.next());
            }
        } else if (datatype.equals(3)) {
            String str = params.get(key).toString();
            cell.setText(str);
        }
    }

    /**
     * 替换模板Table中相应字段为图片
     * @param cell
     * @param params
     * @throws IOException
     * @throws InvalidFormatException
     */
    private  void replaceInImg(XWPFTableCell cell,Map<String,Object> params) throws IOException, InvalidFormatException {
        //拿参数 判断一下是什么类型 怎么处理   一般来说Map中只有两种类型 一种是一个模板图片变量放一张图片 只有一个Map  一种是一个模板变量中放多涨图片List<Map> 还有一种是不存在的情况
        String key=cell.getText().substring(2,cell.getText().length()-1);
        Integer datatype=getMapImgDataType(params,key);
        List<XWPFParagraph> parags=cell.getParagraphs();
        //先清空单元格中所有的段落
        for (int i=0;i<parags.size();i++){
            cell.removeParagraph(i);
        }
        if(datatype.equals(0)){
            return;
        }else if(datatype.equals(1)){
            //处理单张图片
            XWPFParagraph parag=cell.addParagraph();
            Map<String,Object> pic=(Map<String,Object>)params.get(key);
            insertImg(pic,parag);
        }else if(datatype.equals(2)) {
            //处理多涨图片
            List<Map<String,Object>> pics=(List<Map<String,Object>>)params.get(key);
            Iterator<Map<String, Object>> iterator = pics.iterator();
            XWPFParagraph para = cell.addParagraph();
            Integer count = 0;
            while (iterator.hasNext()) {
                Map<String, Object> pic = iterator.next();
                //图片并排插入
                if (Integer.parseInt(pic.get("style").toString()) == 1) {
                    //不做处理 这段代码可注释
                }
                //图片竖排插入
                if (Integer.parseInt(pic.get("style").toString()) == 2) {
                    if (count > 0) {
                        para = cell.addParagraph();
                    }
                }
                insertImg(pic, para);
                count++;
            }
        }
    }


    //插入图片   run创建
    private  void insertImg(Map<String,Object> pic,XWPFParagraph para) throws IOException, InvalidFormatException {
        if(pic.get("imgpath").toString()==null) {
            return;
        }
        String picpath=pic.get("imgpath").toString();
        InputStream is=null;
        BufferedImage bi=null;
        if(picpath.startsWith("http")) {
            is= getFileStream(picpath);
            bi= ImageIO.read(getFileStream(picpath));
        }else {
            is = new FileInputStream(picpath);
            bi=ImageIO.read(new File(picpath));
        }
        XWPFRun run =para.createRun();
        //原图片的长宽
        Integer width=bi.getWidth();
        Integer heigh=bi.getHeight();
        Double much=80.0/width;
        //图片按宽80 比例缩放
        run.addPicture(is,getPictureType(picpath.substring(picpath.lastIndexOf(".")+1)),"",
                                                           Units.toEMU(80),Units.toEMU(heigh*much));
        //图片原长宽
//        run.addPicture(is,getPictureType(pic.get("picType").toString()),"",Units.toEMU(width),Units.toEMU(heigh));
        close(is);
        bi=null;
    }



    //处理模板中的变量名字，去掉${}  然后根据这个变量名在参数map中查找对应的Value值
    private  Integer getMapStrDataTypeValue(Map<String,Object> params,String key){
        if(params.get(key)==null){
            return 0;
        }else if(params.get(key) instanceof List){
            return 2;
        }else if(params.get(key) instanceof String){
            return 3;
        }else {
            throw new RuntimeException("Str data type error!");
        }

    }

    //从params参数中找到模板中key对应的value值
    private  Integer getMapImgDataType(Map<String,Object> params,String key){
        Object valve = params.get(key);
        if(valve==null){
            return 0;
        }
        else if(valve instanceof  Map){
            return 1;
        }else if(valve instanceof List){
            return 2;
        }else {
            throw  new RuntimeException("image data type error!");
        }
    }


    /**
     * 根据图片类型，取得对应的图片类型代码
     *
     * @param picType
     * @return int
     */
    private  int getPictureType(String picType) {
        int res = XWPFDocument.PICTURE_TYPE_PICT;
        if (picType != null) {
            if (picType.equalsIgnoreCase("png")) {
                res = XWPFDocument.PICTURE_TYPE_PNG;
            } else if (picType.equalsIgnoreCase("dib")) {
                res = XWPFDocument.PICTURE_TYPE_DIB;
            } else if (picType.equalsIgnoreCase("emf")) {
                res = XWPFDocument.PICTURE_TYPE_EMF;
            } else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")) {
                res = XWPFDocument.PICTURE_TYPE_JPEG;
            } else if (picType.equalsIgnoreCase("wmf")) {
                res = XWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }

    /**
     * 将输入流中的数据写入字节数组
     *
     * @param in
     * @return
     */
    public  byte[] inputStream2ByteArray(InputStream in, boolean isClose) {
        byte[] byteArray = null;
        try {
            //获得输入流中还有多少字节可读取
            int total = in.available();
            byteArray = new byte[total];
            in.read(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isClose) {
                try {
                    in.close();
                } catch (Exception e2) {
                    e2.getStackTrace();
                }
            }
        }
        return byteArray;
    }


    /**
     * 关闭输入流
     *
     * @param is
     */
    private  void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    private  void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * description:
     *          通过图片URL地址得到文件流, Get接口
     * @Author any
     * @Date 2020/8/14 11:51
     * @param url:图片地址
     * @return java.io.InputStream
     */
    public static InputStream getFileStream(String url){
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            // 通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            return inStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件读取文档
     * @param file
     * @return
     */
    public static XWPFDocument read(File file){
        try {
            return new XWPFDocument(OPCPackage.open(file));
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 流式读取文档
     * @param is
     * @return
     */
    public static XWPFDocument read(InputStream is){
        try {
            return new XWPFDocument(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 输出到文件流
     * @param stream  OutputStream
     * @return
     */
    public boolean write(OutputStream stream){
        // 写入文件流
        try {
            document.write(stream);
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

    public boolean write2Pdf(File file){
        // 创建文件流
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            // 输出
            write(fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

//        FileOutputStream fos = null;
//        InputStream is = new ByteArrayInputStream();
//        InputStream total = new SequenceInputStream();
//        try {
//            IOUtils.copyLarge(is, fos);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        IConverter converter = LocalConverter.builder().build();
//        converter.convert(is).as(DocumentType.DOCX).to(fileOutputStream).as(DocumentType.PDF).execute();

        return true;
    }

}

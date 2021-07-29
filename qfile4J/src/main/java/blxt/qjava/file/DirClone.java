package blxt.qjava.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件夹复制
 * @author OpenJialei
 * @date 2021年07月28日 13:49
 */
public class DirClone {
    /** 源路径 */
    public String sourse;
    /** 目标路径 */
    public String target;

    public DirClone(String sourse, String target) {
        this.sourse = sourse;
        this.target = target;
        File filetarget = new File(target);
        if(!filetarget.exists()){
            // 先创建目标文件夹
            filetarget.mkdirs();
        }
    }

    /**
     * 开始克隆
     */
    public boolean onClone(){
        return onClone(sourse);
    }

    /**
     * 遍历文件夹并复制
     */
    public boolean onClone(String url){
        //获取目录下所有文件
        File f = new File(url);
        File[] allf = f.listFiles();

        //遍历所有文件
        for(File fi:allf) {
            try {
                //拼接目标位置
                String URL = target+fi.getAbsolutePath().substring(sourse.length());
                File fileNew = new File(URL);
                //创建目录或文件
                if(fi.isDirectory()) {
                    if(!fileNew.mkdirs()){
                        return false;
                    }
                }else {
                    if(!QFile.MFile.copyFile(fi, fileNew)){
                        return false;
                    }
                }
                //递归调用
                if(fi.isDirectory()) {
                    if(!onClone(fi.getAbsolutePath())){
                        return false;
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

}

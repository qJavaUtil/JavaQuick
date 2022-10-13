package blxt.qjava.file;

import blxt.qjava.file.callback.FileScanCallback;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目空间文件树
 * @author ZhangJieLei
 */
@Data
public class FileTreeBean {
    /** 包名截取 */
    @JsonIgnore
    @JSONField(serialize=false)
    public static int PACKAGE_INDEX = 0;

    /** 目录标记 */
    boolean dir;
    /** 文件 */
    @JsonIgnore
    @JSONField(serialize=false)
    File file;
    /** 文件名 */
    String name;
    /** 包名(文件夹名) */
    String packageName;

    /** 文件路径hash */
    String hashId;

    @JsonIgnore
    @JSONField(serialize=false)
    int packageIndex;

    /** 文件逆序 */
    @JsonIgnore
    @JSONField(serialize=false)
    boolean reversed = false;

    @JsonIgnore
    @JSONField(serialize=false)
    FileScanCallback callback = null;

    /** 子文件 */
    List<FileTreeBean> files = new ArrayList<>();

    public FileTreeBean(File file) {
        this.file = file;
        packageIndex = PACKAGE_INDEX;
        init(PACKAGE_INDEX);
    }

    public FileTreeBean(File file, int packageIndex) {
        this.file = file;
        this.packageIndex = packageIndex;
        init(packageIndex);
    }

    private void init(int packageIndex){
        name = file.getName();
        packageName = file.getAbsolutePath().substring(packageIndex);
        hashId = ((packageName + File.separator + name).hashCode() & Integer.MAX_VALUE) + System.currentTimeMillis() + "";
    }

    /**
     * 构建文件树
     * @return
     */
    public FileTreeBean buile(){
        if (file.isDirectory()){
            dir = true;
            File[] files = file.listFiles();
            if (files != null){
                // 按文件名排序
                List<File>  fileList = QFile.MFile.orderByName(files, reversed);
                for (File file1 : fileList) {
                    // 接口回调
                    if(callback != null){
                        boolean isAdd = false;
                        if(file1.isDirectory()){
                            isAdd = callback.onDir(file1);
                        }
                        else {
                            isAdd = callback.onFile(file1);
                        }
                        // 如果不添加, 就忽略
                        if(!isAdd){
                            continue;
                        }
                    }
                    // 添加解析
                    this.files.add(new FileTreeBean(file1, packageIndex).buile());
                }
            }
        }
        return this;
    }

    public String toJsonString(String rootPath){
        String json = JSON.toJSONString(this);
        return json;
    }


    public static void main(String[] args) {
        File file = new File("E:\\ShiTou\\Desktop\\演示地址\\测试");
        FileTreeBean fileTreeBean = new FileTreeBean(file, file.getAbsolutePath().length());
        fileTreeBean.buile();

        System.out.println(JSON.toJSONString(fileTreeBean));
    }

}

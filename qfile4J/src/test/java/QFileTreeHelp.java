import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QFileTreeHelp {

    /** 文件回调 */
    public static FileTreeBeanCallBack callBack = null;
    /** home路径 */
    public static String workPath = "";
    public static int fileCount = 0;


    /**
     *
     * @param file   入参一定是一个文件夹
     * @param tree   文件树层级
     * @return
     */
    public static FileTreeBean getFileTree(File file, int tree){
        if (!file.exists()){
            return null;
        }

        FileTreeBean fileTreeBean = new FileTreeBean(file, file.getParentFile(), tree);

        for(File file1 : Objects.requireNonNull(file.listFiles())){
            FileTreeBean fs = null;
            if(file1.getName().startsWith(".") ){
                // ".*"文件
                continue;
            }
            if(file1.isDirectory()){
                fs = getFileTree(file1, tree + 1);
                if(fs == null){
                    continue;
                }
                fileTreeBean.addFile(fs);
            }
            else{
                // 忽略根目录下的文件
                if(tree == -1 ){
                    continue;
                }
                fs = new FileTreeBean(file1, file, tree + 1);
                fileTreeBean.addFile(fs);
                if(callBack != null){
                    callBack.onFile(fs);
                }
            }
            fileCount++;
        }

        // 跳过根目录
        if(callBack != null && tree > -1){
            if(tree == 0){ // 成果
                callBack.onBank(fileTreeBean);
            }
            else{ // 文库
                callBack.onDirectory(fileTreeBean);
            }
        }

        return fileTreeBean;
    }

    /**
     * 回调接口
     */
    public interface FileTreeBeanCallBack{
        void onFile(FileTreeBean bean);
        void onDirectory(FileTreeBean bean);
        void onBank(FileTreeBean bean);
    }

    /**
     * 文件树定义
     */
    public static class FileTreeBean{
        public int fid;
        public int ffid;
        public File file;
        public String name;
        public String path;
        public String type;
        public int treen;

        public List<FileTreeBean> fileTreeBeans;

        public FileTreeBean(File file, File ffile, int treen) {
            this.file = file;
            this.name = file.getName();
            this.path = file.getPath().substring(workPath.length());
            this.fid =  file.getAbsolutePath().hashCode() & Integer.MAX_VALUE;
            this.ffid = ffile.getAbsolutePath().hashCode() & Integer.MAX_VALUE;
            this.type = file.isFile()? "file" :"Directory";
            this.treen = treen;

            if(file.isDirectory()){
                this.type = "directory";
            }
            else{
                int index = name.lastIndexOf(".");
                if(index > 0){
                    this.type = name.substring(index + 1);
                }
                else{
                    this.type = "file";
                }
            }

        }

        public FileTreeBean addFile(FileTreeBean fileTreeBean){
            if(fileTreeBeans == null){
                fileTreeBeans = new ArrayList<>();
            }
            fileTreeBeans.add(fileTreeBean);
            return this;
        }

        @Override
        public String toString() {

            String str = "{" +
                    "fid=" + fid +
//                    ", ffid=" + ffid +
//                    ", name='" + name + '\'' +
                    ", treen='" + treen + '\'' +
                    ", type='" + type + '\'' +
                    ", path='" + path + '\'' +
                    ", \r\n\t fileTreeBeans=" + fileTreeBeans +
                    '}' + "\r\n";


            return str;
        }
    }

    public static void main(String[] args) throws Exception {
        FileTreeBean fileTreeBean =  getFileTree(new File("E:\\ZhangJieLei\\Documents\\workspace\\workSudy\\笔记"), -1);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(fileTreeBean);
        System.out.println(jsonObject);
        int fileCount = 1000;
//        File fileRoot = new File("E:\\ZhangJieLei\\Documents\\workspace\\workSudy\\笔记");
//        File[] files = fileRoot.listFiles();


        //System.out.println(fileTreeBean.toString());
    }
}

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QFileTools {

    public static FileTreeBean getFileTree(File file, int tree){
        if (!file.exists() || file.getName().startsWith(".") ){
            return null;
        }

        if(!file.isDirectory()){
            return new FileTreeBean(file, file.getParentFile(), tree + 1);
        }

        FileTreeBean fileTreeBean = new FileTreeBean(file, file.getParentFile(), tree);

        for(File file1 : file.listFiles()){
            if(file.isFile()){
                fileTreeBean.addFile(new FileTreeBean(file1, file, tree + 1));
            }
            else{
                FileTreeBean sf = getFileTree(file1, tree + 1);
                if(sf == null){
                    continue;
                }
                fileTreeBean.addFile(sf);
            }
        }

        return fileTreeBean;
    }

    public static class FileTreeBean{
        public int fid;
        public int ffid;
        public File file;
        public String name;
        public String path;
        public String type;
        public int treen;

        public List<FileTreeBean> fileTreeBeans = new ArrayList<>();

        public FileTreeBean(File file, File ffile, int treen) {
            this.file = file;
            this.name = file.getName();
            this.path = file.getPath();
            this.ffid = ffile.getAbsolutePath().hashCode();
            this.type = file.isFile()?"file" :"Directory";
            this.treen = treen;

            fid = file.getAbsolutePath().hashCode();
        }

        public FileTreeBean addFile(FileTreeBean fileTreeBean){
            fileTreeBeans.add(fileTreeBean);
            return this;
        }

        @Override
        public String toString() {
            return "FileTreeBean{" +
                    "fid=" + fid +
                    "ffid=" + ffid +
                    ", type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", path='" + path + '\'' +
                    ", \r\n fileTreeBeans=" + fileTreeBeans +
                    '}';
        }
    }

    public static void main(String[] args) throws Exception {
        FileTreeBean fileTreeBean =  getFileTree(new File("E:\\ZhangJieLei\\Documents\\workspace\\workSudy\\笔记"), -1);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(fileTreeBean);
        System.out.println(jsonObject);
        //System.out.println(fileTreeBean.toString());
    }
}

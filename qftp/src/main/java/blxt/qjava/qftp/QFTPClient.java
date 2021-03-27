package blxt.qjava.qftp;

import blxt.qjava.autovalue.inter.value.IntDef;
import blxt.qjava.utils.BitUtils;
import lombok.Data;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * ftp客户端
 */
@Data
public class QFTPClient extends FTPClient {
    static Logger logger = LoggerFactory.getLogger(QFTPClient.class);
    public final static byte ONLY_FILE = 0x01;
    public final static byte ONLY_DIR = 0x03;
    public final static byte ONLY_FILE_DIR = 0x02;

    String host;
    int port;
    String uname;
    String pwd;

    /** 当前录路径 */
    String pathUse = "/";

    /** 本地编码 */
    String localCharset = "GBK";

    /** 服务器编码 FTP协议里面，规定文件名编码为iso-8859-1 */
    String serverCharset = "iso-8859-1";
    /** 启用编码转换 */
    boolean falChangCharset = false;

    OnFTPClientListener listener = null;

    public QFTPClient(){

    }

    public QFTPClient(String host, int port, String uname, String pwd) {
        this.host = host;
        this.port = port;
        this.uname = uname;
        this.pwd = pwd;
    }


    /**
     *  连接FTP服务器
     */
    public boolean connect(){
        try {
            super.connect(host, port);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if(listener != null){
            listener.onConnected(true);
        }
        return  true;
    }

    /**
     * 登录
     */
    public boolean login()
    {
        try {
            boolean fal =  super.login(uname, pwd);
            if(listener != null){
                listener.onLogin(fal);
            }
            return fal;
        } catch (IOException e) {
            e.printStackTrace();
            if(listener != null){
                listener.onLogin(false);
            }
            return false;
        }

    }


    /**
     * 关闭
     */
    public void close() {
        try {
            logout();     //退出登录
            disconnect(); //关闭连接
            if(listener != null){
                listener.onDisConnected(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if(listener != null){
                listener.onDisConnected(false);
            }
        }

    }

    /**
     * 开启服务器对UTF-8
     */
    public void setUtf8(){
        // 设置上传文件的类型为二进制类型
        try {
            // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
            if (FTPReply.isPositiveCompletion(sendCommand("OPTS UTF8", "ON"))) {
                localCharset = "UTF-8";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setControlEncoding(localCharset);
    }

    /**
     * 目录跳转
     * @param pathname
     * @return
     * @throws IOException
     */
    public boolean changeWorkingDirectory(String pathname) {
        if(!pathname.endsWith(File.separator)){
            pathname += File.separator;
        }
        if(!pathname.startsWith(File.separator)){
            pathname = File.separator + pathname;
        }
        try {
            // 对中文文件名进行转码，否则中文名称的文件下载失败ftpPath
            pathUse = pathname;
            if(falChangCharset){
                pathname = new String(pathname.getBytes(localCharset), serverCharset);
            }
            return super.changeWorkingDirectory(pathname);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建目录
     * @param pathname
     * @return
     * @throws IOException
     */
    public boolean makeDirectory(String pathname){
        if(!pathname.endsWith(File.separator)){
            pathname += File.separator;
        }
        if(!pathname.startsWith(File.separator)){
            pathname = File.separator + pathname;
        }
        try {
            pathUse = pathname;
            if(falChangCharset){
                pathname = new String(pathname.getBytes(localCharset), serverCharset);
            }
            return super.makeDirectory(pathname);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建多级目录
     *
     * @param remoteDir  服务器目录绝对路径
     * @return
     * @throws IOException
     */
    public boolean makeMultiDir(String remoteDir)  {
        if (changeWorkingDirectory(remoteDir)) {
            return false;
        }
        String[] remotePaths = remoteDir.split("/");
        for (int i = 1; i < remotePaths.length; i++) {
            String temp = "/";
            for (int j = 1; j <= i; j++) {
                temp += remotePaths[j] + "/";
            }
            temp = temp.substring(0, temp.lastIndexOf("/"));// 将该多级目录分解
            if (!changeWorkingDirectory(temp)) {// 说明该分级目录不存在
                if (i == 1) {
                    // 不允许创建第一级目录
                    return false;
                }
                if (!makeDirectory(temp)) {// 该远程路径不存在，创建该分级目录
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 从FTP服务器下载文件,单个文件
     *
     * @param ftpPath         FTP服务器中文件所在路径 格式： ftptest/aa
     * @param localPath      下载到本地的位置 格式：H:/download
     * @param ftpFileName    ftp文件名称
     * @param localFileName  本地保存新文件名
     */
    public boolean download(String ftpPath, String localPath, String ftpFileName, String localFileName) {
        String serverpath = null;
        String localFilePath = null;
        try {
            if(!ftpPath.endsWith(File.separator)){
                ftpPath += File.separator;
            }
            if(!localPath.endsWith(File.separator)){
                localPath += File.separator;
            }

            enterLocalPassiveMode();// 设置被动模式
            setFileType(FTP.BINARY_FILE_TYPE);// 设置传输的模式

            changeWorkingDirectory(ftpPath);
            // 路径
            serverpath = ftpPath + ftpFileName;
            localFilePath = localPath + localFileName;
            // 文件
            File localFile = new File(localFilePath);
            File localParent = localFile.getParentFile();
            // 本地文件检查
            if (!localParent.exists()) {
                localParent.mkdirs();
                logger.warn("文件夹不存在,已创建:{}", localPath);
            }
            if (localFile.exists()) {
                localFile.delete();
                logger.warn("文件已存在,已删除:{}", localFile.getAbsoluteFile());
            }

            logger.debug("下载文件中{}->{}", serverpath,  localFile.getAbsoluteFile());

            // 下载文件
            OutputStream os = new FileOutputStream(localFile);
            boolean fal = retrieveFile(serverpath, os);
            os.flush();
            os.close();

            logger.debug("下载文件{}:{}", fal ? "成功" : "失败", serverpath);

            if(!fal){
                localFile.delete();
            }
            return fal;
        } catch (FileNotFoundException e) {
            logger.error("没有找到文件:{}", serverpath);
            e.printStackTrace();
            return false;
        }catch (IOException e) {
            e.printStackTrace();
            logger.error("文件读取错误:{}", localFilePath);
            e.printStackTrace();
            return false;
        }

    }


    /**
     * Description: 向FTP服务器上传文件
     *
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01/
     * @param filename 上传到FTP服务器上的文件名
     * @param input    输入流
     * @return 成功返回true，否则返回false
     */
    public boolean uploadFile(String filePath, String filename, InputStream input) {
        boolean result = false;
        try {
            int reply = getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                logger.error("链接异常,已关闭.");
                disconnect();
                return result;
            }
            if(!filePath.endsWith(File.separator)){
                filePath += File.separator;
            }

            // 切换到上传目录
            if (!changeWorkingDirectory(filePath)) {
                // 如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = "";
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir))
                        continue;
                    tempPath += "/" + dir;
                    if (!changeWorkingDirectory(tempPath)) {
                        if (!makeDirectory(tempPath)) {
                            return result;
                        } else {
                            changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }

            enterLocalPassiveMode();// 设置被动模式
            setFileType(FTP.BINARY_FILE_TYPE);// 设置传输的模式
            // 上传文件
            filename = new String(filename.getBytes(localCharset), serverCharset);
            if (!storeFile(filename, input)) {
                return result;
            }

            if (null != input) {
                input.close();
            }

            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取当前路径文件列表
     * @return
     */
    public List<String> getFileList(){
        return getFileList(pathUse);
    }

    /***
     * 获取当前路径文件夹列表
     * @return
     */
    public List<String> getDirList(){
        return getDirList(pathUse);
    }


    /**
     * 获取文件和文件夹列表
     * @param pathName  绝对路径, 如 /xxx/xxx/. 已 '/' 开头和结尾
     * @return
     */
    public List<String> getFileList(String pathName){
        return getFilesList(pathName, ONLY_FILE_DIR);
    }

    /**
     * 仅获取文件列表
     * @param pathName  绝对路径, 如 /xxx/xxx/. 已 '/' 开头和结尾
     * @return
     */
    public List<String> getFilesList(String pathName){
        return getFilesList(pathName, ONLY_FILE);
    }

    /**
     * 仅获取文件夹列表
     * @param pathName 绝对路径, 如 /xxx/xxx/. 已 '/' 开头和结尾
     * @return
     */
    public List<String> getDirList(String pathName){
        return getFilesList(pathName, ONLY_DIR);
    }


    /**
     * 获取文件夹列表
     * @param pathName   绝对路径, 如 /xxx/xxx/. 已 '/' 开头和结尾
     * @param fileType   筛选. ONLY_FILE 或  ONLY_DIR 或 ONLY_FILE_DIR
     * @return
     */
    public List<String> getFilesList(String pathName, @VisibleValue byte fileType){
        ArrayList<String> arFiles = new ArrayList<>();

        //更换目录到当前目录
        FTPFile[] files = null;
        try {
            changeWorkingDirectory(pathName);
            files = listFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(files == null){
            return arFiles;
        }
        for (FTPFile file : files) {
            boolean falFile = false;
            boolean falDir  = false;

            if(BitUtils.checkBitValue(fileType, 0)){
                falFile = true;
            }
            if(BitUtils.checkBitValue(fileType, 0)){
                falDir = true;
            }

            if (falFile && file.isFile()) {
                arFiles.add(pathName + file.getName());
            }

            if (falDir && file.isDirectory()) {
                arFiles.add(pathName + file.getName());
            }
        }

        return arFiles;
    }


    /**
     * 文件列表获取筛选入参限定
     */
    @IntDef({ONLY_FILE,ONLY_DIR, ONLY_FILE_DIR})
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface VisibleValue{}


    /**
     * ftp监听回调
     */
    public interface OnFTPClientListener{
        /**
         * 链接和断开链接
         * @param isConnected
         */
        void onConnected(boolean isConnected);
        void onDisConnected(boolean isConnected);
        void onLogin(boolean isLogin);
    }
}

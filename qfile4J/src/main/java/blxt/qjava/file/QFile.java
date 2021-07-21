package blxt.qjava.file;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 简单文件工具
 */
public class QFile {
    /** 默认编码 */
    public static String CODE_DEFAULT = "UTF-8";
    public static int BUFFER_MAX = 1024;
    protected static int length;

    public QFile() {
    }

    public static byte[] toBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int ch;
        while ((ch = in.read()) != -1) {
            out.write(ch);
        }

        byte[] buffer = out.toByteArray();
        out.close();
        return buffer;
    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0L;

        try {
            File[] fileList = file.listFiles();

            for (int i = 0; i < fileList.length; ++i) {
                if (fileList[i].isDirectory()) {
                    size += getFolderSize(fileList[i]);
                } else {
                    size += fileList[i].length();
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return size;
    }

    public static boolean move(File oldFile, File newFile) {
        return oldFile.isDirectory() ? MFolder.moveFolder(oldFile, newFile) : MFile.moveFile(oldFile, newFile);
    }

    public static boolean copy(File oldFile, File newFile) {
        return oldFile.isDirectory() && newFile.isDirectory() ? MFolder.copyFolder(oldFile, newFile) : MFile.copyFile(oldFile, newFile);
    }

    public static void CloseableClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static class MFolder {
        public MFolder() {
        }

        public static long getDirSize(File dir) {
            if (dir == null) {
                return 0L;
            } else if (!dir.isDirectory()) {
                return 0L;
            } else {
                long dirSize = 0L;
                File[] files = dir.listFiles();
                File[] var4 = files;
                int var5 = files.length;

                for (int var6 = 0; var6 < var5; ++var6) {
                    File file = var4[var6];
                    if (file.isFile()) {
                        dirSize += file.length();
                    } else if (file.isDirectory()) {
                        dirSize += file.length();
                        dirSize += getDirSize(file);
                    }
                }

                return dirSize;
            }
        }

        public long getFileList(File dir) {
            long count = 0L;
            File[] files = dir.listFiles();
            count = (long) files.length;
            File[] var5 = files;
            int var6 = files.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                File file = var5[var7];
                if (file.isDirectory()) {
                    count += this.getFileList(file);
                    --count;
                }
            }

            return count;
        }


        public static boolean createDirectory(String directoryName) {
            boolean status;
            if (directoryName.trim().length() > 0) {
                File newPath = new File(directoryName);
                status = newPath.mkdir();
                status = true;
            } else {
                status = false;
            }

            return status;
        }

        /**
         * 删除文件
         * @param dirFile
         * @return
         */
        public static boolean delete(File dirFile) {
            // 如果dir对应的文件不存在，则退出
            if (dirFile==null || !dirFile.exists()) {
                return false;
            }

            if (dirFile.isFile()) {
                return dirFile.delete();
            } else {
                for (File file : dirFile.listFiles()) {
                    if (!delete(file)){
                        return false;
                    }
                }
            }
            return dirFile.delete();
        }

        public static boolean copyFolder(File oldFile, File newPath) {
            if (oldFile.isFile()) {
                return MFile.copyFile(oldFile, new File(newPath, oldFile.getName()));
            } else {
                try {
                    newPath.mkdirs();
                    File[] temps = oldFile.listFiles();
                    boolean flag = true;
                    QFile.length = temps.length;

                    for (int i = 0; i < QFile.length; ++i) {
                        File temp = temps[i];
                        if (temp.isFile()) {
                            File path = new File(newPath, oldFile.getName());
                            path.mkdirs();
                            File file = new File(path, temp.getName());
                            flag = MFile.copyFile(temp, file);
                        } else if (temp.isDirectory()) {
                            flag = copyFolder(temp, new File(newPath + File.separator + oldFile.getName()));
                        }

                        if (!flag) {
                            break;
                        }
                    }

                    return flag;
                } catch (Exception var8) {
                    var8.printStackTrace();
                    return false;
                }
            }
        }

        public static boolean copyFolder(String oldPath, String newPath) {
            return copyFolder(new File(oldPath), new File(newPath));
        }

        public static boolean moveFolder(File oldFile, File newPath) {
            return copyFolder(oldFile, newPath) && delete(oldFile);
        }
    }

    public static class MFile {
        public MFile() {
        }

        public static File createFile(File file) {
            if (file.isDirectory()) {
                return null;
            } else {
                if (!file.exists()) {
                    try {
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }

                        file = new File(file.getParentFile(), file.getName());
                        file.createNewFile();
                        if (!file.exists()) {
                            return null;
                        }
                    } catch (Exception var2) {
                        return null;
                    }
                }

                return file;
            }
        }

        public static File createFile(String folderPath, String fileName) {
            File destDir = new File(folderPath);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            return new File(folderPath, fileName + fileName);
        }

        /**
         * 获取文件名, 不包括后缀
         * @param filePath
         * @return
         */
        public static String getFileName(String filePath) {
            int i = filePath.lastIndexOf(File.separator);
            i = Math.max(i, 0);
            int l = filePath.lastIndexOf(".");
            l = l < 0 ? filePath.length() : l;
            filePath = filePath.substring(i, l);
            return filePath;
        }

        /**
         * 获取文件后缀
         * @param fileName
         * @return
         */
        public static String getSuffix(String fileName){
            String[] suffix = fileName.split("\\.");
            if(suffix.length <= 1){
                return null;
            }
            return suffix[suffix.length - 1];
        }


        public static String getNameWithoutFormat(String filePath) {

            int point = filePath.lastIndexOf(46);
            return filePath.substring(filePath.lastIndexOf(File.separator) + 1, point);

        }

        public static String getFormat(String fileName) {
            int point = fileName.lastIndexOf(46);
            return fileName.substring(point + 1);
        }

        public static long getFileSize(String filePath) {
            long size = 0L;
            File file = new File(filePath);
            if (file != null && file.exists()) {
                size = file.length();
            }

            return size;
        }

        public static String getFileSize(long size) {
            if (size <= 0L) {
                return "0";
            } else {
                DecimalFormat df = new DecimalFormat("##.##");
                float temp = (float) size / (BUFFER_MAX * 1.0f);
                return temp >= 1024.0F ? df.format((double) (temp / (BUFFER_MAX * 1.0f))) + "M" : df.format((double) temp) + "K";
            }
        }

        public static String getFileSizeStr(long fileS) {
            DecimalFormat df = new DecimalFormat("#.00");
            String fileSizeString = "";
            if (fileS < 1024L) {
                fileSizeString = df.format((double) fileS) + "B";
            } else if (fileS < 1048576L) {
                fileSizeString = df.format((double) fileS / 1024.0D) + "KB";
            } else if (fileS < 1073741824L) {
                fileSizeString = df.format((double) fileS / 1048576.0D) + "MB";
            } else {
                fileSizeString = df.format((double) fileS / 1.073741824E9D) + "G";
            }

            return fileSizeString;
        }

        public static boolean isExists(String filepath) {
            boolean status;
            if (!filepath.equals("")) {
                File newPath = new File(filepath);
                status = newPath.exists();
            } else {
                status = false;
            }

            return status;
        }

        @SuppressWarnings("resource")
        public static boolean copyFile(File sourceFile, File targetFile) {
            boolean result = false;
            if (sourceFile != null && targetFile != null) {
                if (targetFile.exists()) {
                    targetFile.delete();
                }

                try {
                    targetFile.createNewFile();
                } catch (IOException var9) {
                    var9.printStackTrace();
                }

                FileChannel srcChannel = null;
                FileChannel dstChannel = null;

                try {
                    srcChannel = (new FileInputStream(sourceFile)).getChannel();
                    dstChannel = (new FileOutputStream(targetFile)).getChannel();
                    srcChannel.transferTo(0L, srcChannel.size(), dstChannel);
                    result = true;
                } catch (FileNotFoundException var7) {
                    var7.printStackTrace();
                } catch (IOException var8) {
                    var8.printStackTrace();
                }

                try {
                    if (srcChannel != null) {
                        srcChannel.close();
                    }

                    if (dstChannel != null) {
                        dstChannel.close();
                    }
                } catch (IOException var6) {
                    var6.printStackTrace();
                }

                return result;
            } else {
                return result;
            }
        }

        public static boolean delete(String fileName) {
            SecurityManager checker = new SecurityManager();
            boolean status;
            if (fileName.trim().length() > 0) {
                File newPath = new File(fileName);
                checker.checkDelete(newPath.toString());
                if (newPath.isFile()) {
                    try {
                        newPath.delete();
                        status = true;
                    } catch (SecurityException var5) {
                        var5.printStackTrace();
                        status = false;
                    }
                } else {
                    status = false;
                }
            } else {
                status = false;
            }

            return status;
        }

        public static boolean delete(File file) {
            return MFolder.delete(file);
        }

        public static boolean moveFile(File oldPath, File newPath) {
            if (oldPath.isDirectory()) {
                return false;
            } else {
                if (newPath.isDirectory()) {
                    newPath = new File(newPath, oldPath.getName());
                }

                try {
                    return oldPath.renameTo(newPath);
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return false;
                }
            }
        }

        /**
         * 更具文件名称排序
         * @param files
         * @return
         */
        public static List<File> orderByName(File[] files) {
            List<File> fileList = Arrays.asList(files);
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile()) {
                        return -1;
                    }
                    if (o1.isFile() && o2.isDirectory()) {
                        return 1;
                    }
                    Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
                    return com.compare(o1.getName(), o2.getName());
                }
            });
            return fileList;
        }

        /**
         * 文件日期排序
         * @param files
         * @return
         */
        public static List<File> orderByDate(File[] files) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0) {
                        return 1;
                    }
                    else if (diff == 0) {
                        return 0;
                    }
                    else {
                        return -1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减
                    }
                }

                @Override
                public boolean equals(Object obj) {
                    return true;
                }

            });
            return Arrays.asList(files);
        }

        /**
         * 文件大小排序
         * @param files
         * @return
         */
        public static List<File> orderByLength(File[] files) {
            List<File> fileList = Arrays.asList(files);
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    long diff = f1.length() - f2.length();
                    if (diff > 0) {
                        return 1;
                    }
                    else if (diff == 0) {
                        return 0;
                    }
                    else {
                        return -1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减
                    }
                }

                @Override
                public boolean equals(Object obj) {
                    return true;
                }
            });

            return fileList;
        }
    }

    public static class Read {
        public Read() {
        }

        public static byte[] getBytes(File file) throws IOException {
            File f = file;
            if (!file.exists()) {
                throw new FileNotFoundException("file not exists");
            } else {
                ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
                BufferedInputStream in = null;

                try {
                    in = new BufferedInputStream(new FileInputStream(f));
                    int buf_size = BUFFER_MAX;
                    byte[] buffer = new byte[buf_size];

                    int len;
                    while (-1 != (len = in.read(buffer, 0, buf_size))) {
                        bos.write(buffer, 0, len);
                    }

                    byte[] var7 = bos.toByteArray();
                    return var7;
                } catch (IOException var16) {
                    var16.printStackTrace();
                    throw var16;
                } finally {
                    try {
                        in.close();
                    } catch (IOException var15) {
                        var15.printStackTrace();
                    }

                    bos.close();
                }
            }
        }

        @SuppressWarnings("resource")
        public static String getStr(File file) {
            StringBuilder sb = new StringBuilder();

            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file.getPath()));
                char[] buff = new char[BUFFER_MAX];

                for (; br.read(buff) != -1; ) {
                    sb.append(buff);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }finally {
                if(br != null){
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();
        }

        public static List<String> getStrByLine(File file, int leng) {
            List<String> strList = null;
            if (leng <= 0) {
                strList = new ArrayList<String>();
            } else {
                strList = new ArrayList<String>(leng);
            }

            if (file.isDirectory()) {
                return null;
            } else {
                try {
                    InputStream instream = new FileInputStream(file);
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);

                    String line;
                    while ((line = buffreader.readLine()) != null) {
                        strList.add(line);
                    }

                    instream.close();
                    inputreader.close();
                    buffreader.close();

                    return strList;
                } catch (IOException var7) {
                    return null;
                }
            }
        }
        /**
         *
         * @return
         */
        public static String getStrByBufferedReader(BufferedReader reader){
            try {
                StringBuilder strber = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    strber.append(line).append("\n");
                }
                return strber.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        /**
         *
         * @param inStream
         * @return
         */
        public static String getStrByInputStream(InputStream inStream){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inStream, StandardCharsets.UTF_8));
                StringBuilder strber = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    strber.append(line).append("\n");
                }
                return strber.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public static byte[] readBytes(InputStream is){
            byte[] data = null;
            List<Byte> list = new ArrayList<>();
            try {
                //严谨起见,一定要加上这个判断,不要返回data[]长度为0的数组指针
                if(is.available()==0){
                    return data;
                }
                data = new byte[is.available()];
                is.read(data);
                return data;
            } catch (IOException e) {
                return data;
            }
        }

        @SuppressWarnings("resource")
        public static byte[] getBytes4Access(String filePath) {
            FileChannel fc = null;
            byte[] result = null;

            try {
                fc = (new RandomAccessFile(filePath, "r")).getChannel();
                MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0L, fc.size()).load();
                result = new byte[(int) fc.size()];
                if (byteBuffer.remaining() > 0) {
                    byteBuffer.get(result, 0, byteBuffer.remaining());
                }

                byte[] var4 = result;
                return var4;
            } catch (IOException var14) {
                var14.printStackTrace();
            } finally {
                try {
                    fc.close();
                } catch (IOException var13) {
                    var13.printStackTrace();
                }

            }

            return (byte[]) result;
        }


        @SuppressWarnings("unused")
        private static String readStrByStream(FileInputStream inStream) {
            try {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                boolean var3 = true;

                int length;
                while ((length = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, length);
                }

                outStream.close();
                inStream.close();
                return outStream.toString();
            } catch (IOException var4) {
                System.err.println(var4.getMessage());
                return null;
            }
        }




    }

    public static class Write {
        public Write() {
        }

        public static boolean save(File file, byte[] datas) {
            file = MFile.createFile(file);
            if (file == null) {
                return false;
            } else {
                FileOutputStream out = null;

                boolean var4;
                try {
                    out = new FileOutputStream(file);
                    out.write(datas);
                    out.close();
                    boolean var3 = true;
                    return var3;
                } catch (IOException var9) {
                    var9.printStackTrace();
                    var4 = false;
                    return var4;
                } catch (Exception var10) {
                    var4 = false;
                } finally {
                    QFile.CloseableClose(out);
                }

                return var4;
            }
        }

        public static boolean save(File file, String filecontent) {
            FileOutputStream fos = null;
            OutputStreamWriter osw = null;

            boolean var5;
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                fos = new FileOutputStream(file);
                osw = new OutputStreamWriter(fos, CODE_DEFAULT);
                osw.write(filecontent);
                osw.close();
                return true;
            } catch (Exception var15) {
                var5 = false;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                        osw.close();
                    } catch (IOException var14) {
                        var14.printStackTrace();
                    }
                }

            }

            return var5;
        }

        public static boolean add(File fileName, byte[] datas) {
            if (!fileName.exists()) {
                try {
                    fileName.createNewFile();
                } catch (IOException var10) {
                    var10.printStackTrace();
                }
            }

            FileOutputStream out = null;

            boolean var4;
            try {
                out = new FileOutputStream(fileName, true);

                for (int i = 0; i < datas.length; ++i) {
                    out.write(datas[i]);
                }

                boolean var14 = true;
                return var14;
            } catch (IOException var11) {
                var11.printStackTrace();
                var4 = false;
                return var4;
            } catch (Exception var12) {
                var4 = false;
            } finally {
                QFile.CloseableClose(out);
            }

            return var4;
        }

        public static boolean add(File fileName, String content) {
            FileOutputStream fos = null;
            OutputStreamWriter osw = null;

            boolean var5;
            try {
                if (!fileName.exists()) {
                    fileName.createNewFile();
                    fos = new FileOutputStream(fileName);
                } else {
                    fos = new FileOutputStream(fileName, true);
                }

                osw = new OutputStreamWriter(fos, CODE_DEFAULT);
                osw.write(content);
                osw.close();
                return true;
            } catch (Exception var15) {
                var5 = false;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                        osw.close();
                    } catch (IOException var14) {
                        var14.printStackTrace();
                    }
                }

            }

            return var5;
        }
    }


    /**
     * qingxi mulu
     * @param path
     * @return
     */
    public static String cleanPath(String path){
        int index = path.indexOf(".." + File.separator);
        while(index >= 0){
            int indexI = path.lastIndexOf(".." + File.separator, index);
            indexI = path.lastIndexOf(File.separator, indexI - 4);
            path = path.substring(0, indexI + 1) +
                    path.substring(index + 3);
            index = path.indexOf(".." + File.separator);
        }
        path = path.replace("." + File.separator, "");
        return path;
    }

    /**
     * 获取真实路径
     * @param docWorkSpace
     * @return
     */
    public static String getRealPath(String docWorkSpace){
        docWorkSpace = docWorkSpace.trim();

        File fileHome = new File(docWorkSpace);
        fileHome = new File(fileHome.getParentFile().getAbsolutePath(), fileHome.getName());
        docWorkSpace = fileHome.getAbsolutePath();
        // 处理相对目录
        docWorkSpace = QFile.cleanPath(docWorkSpace);

        fileHome = new File(docWorkSpace);
        if(!fileHome.exists()){
            fileHome.mkdirs();
        }
        return fileHome.getAbsolutePath();
    }

}

package blxt.qjava.file;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 * 文件zip工具.
 */
public class QZip {
    public static int BUFFER_MAX = 1024;

    /**
     * 压缩文件
     *
     * @param src      压缩目标
     * @param target   保存路径
     * @throws Exception
     */
    public static void makeZip(File src, File target) throws Exception {
        List<File> fileList = new ArrayList<File>();
        String baseDir = "";
        if (src.isDirectory()) {
            baseDir = src.getPath();
            fileList.addAll(getSubFiles(src));
        } else {
            baseDir = src.getParent();
            fileList.add(src);
        }

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(target));
        ZipEntry ze = null;
        byte[] buf = new byte[BUFFER_MAX];
        int readLen = 0;
        for (int i = 0; i < fileList.size(); i++) {
            File f = fileList.get(i);
            ze = new ZipEntry(getAbsFileName(baseDir, f));
            ze.setSize(f.length());
            ze.setTime(f.lastModified());
            zos.putNextEntry(ze);
            InputStream is = new BufferedInputStream(new FileInputStream(f));
            while ((readLen = is.read(buf, 0, BUFFER_MAX)) != -1) {
                zos.write(buf, 0, readLen);
            }
            is.close();
        }
        zos.close();
    }

    /**
     * 解压文件.
     *
     * @param zipSrc      压缩文件路径
     * @param target      解压路径
     * @throws Exception
     */
    public static void unZipFile(File zipSrc, String target) throws Exception {
        ZipFile zfile = new ZipFile(zipSrc);
        Enumeration<? extends ZipEntry> zList = zfile.entries();
        ZipEntry ze = null;
        String dirZip = target;

        if (!dirZip.endsWith("/")) {
            dirZip = dirZip + "/";
        }

        File desPath = new File(dirZip);
        if (!desPath.exists()) {
            desPath.mkdirs();
        }
        byte[] buf = new byte[BUFFER_MAX];
        while (zList.hasMoreElements()) {
            ze = zList.nextElement();
            if (ze.isDirectory()) {
                File desFile = new File(dirZip + ze.getName());
                desFile.mkdir();
                continue;
            }
            File desFile = new File(dirZip + ze.getName());
            OutputStream os = new BufferedOutputStream(new FileOutputStream(desFile));
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();
    }

    private static String getAbsFileName(String baseDir, File realFileName) {
        File real = realFileName;
        File base = new File(baseDir);
        String ret = real.getName();
        while (true) {
            real = real.getParentFile();
            if (real == null) {
                break;
            }
            if (real.equals(base)) {
                break;
            }
            ret = real.getName() + "/" + ret;
        }
        return ret;
    }

    private static List<File> getSubFiles(File baseDir) {
        List<File> ret = new ArrayList<File>();
        File[] tmp = baseDir.listFiles();
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].isFile()) {
                ret.add(tmp[i]);
            }
            if (tmp[i].isDirectory()) {
                ret.addAll(getSubFiles(tmp[i]));
            }
        }
        return ret;
    }

    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                ret = new File(ret, dirs[i]);
            }
            if (!ret.exists()) {
                ret.mkdirs();
            }
            ret = new File(ret, dirs[dirs.length - 1]);
            return ret;
        }
        return ret;
    }
}

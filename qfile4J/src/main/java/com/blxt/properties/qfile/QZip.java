package com.blxt.properties.qfile;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 * 文件zip工具
 */
public class QZip {
    public static final int BUFFER = 1024;

    /**
     * 压缩文件
     *
     * @param baseFile
     * @param fileDest
     * @throws Exception
     */
    public static void zipFile(File baseFile, File fileDest) throws Exception {
        List<File> fileList = new ArrayList<File>();
        String baseDir = "";
        if (baseFile.isDirectory()) {
            baseDir = baseFile.getPath();
            fileList.addAll(getSubFiles(baseFile));
        } else {

            baseDir = baseFile.getParent();
            fileList.add(baseFile);
        }

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileDest));
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        int readLen = 0;
        for (int i = 0; i < fileList.size(); i++) {
            File f = fileList.get(i);
            ze = new ZipEntry(getAbsFileName(baseDir, f));
            ze.setSize(f.length());
            ze.setTime(f.lastModified());
            zos.putNextEntry(ze);
            InputStream is = new BufferedInputStream(new FileInputStream(f));
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                zos.write(buf, 0, readLen);
            }
            is.close();
        }
        zos.close();
    }

    /**
     * 解压文件
     *
     * @param zipName
     * @param unZipPath
     * @throws Exception
     */
    public static void unZipFile(File zipName, String unZipPath) throws Exception {
        ZipFile zfile = new ZipFile(zipName);
        Enumeration<? extends ZipEntry> zList = zfile.entries();
        ZipEntry ze = null;
        String dirZip = unZipPath;


        if (!dirZip.endsWith("/")) {
            dirZip = dirZip + "/";
        }


        File desPath = new File(dirZip);
        if (!desPath.exists()) {
            desPath.mkdirs();
        }

        byte[] buf = new byte[1024];


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

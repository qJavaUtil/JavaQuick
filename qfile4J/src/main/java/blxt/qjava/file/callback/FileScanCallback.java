package blxt.qjava.file.callback;

import java.io.File;

/**
 * 文件扫描回调接口
 * @author OpenJialei
 * @date 2021年06月21日 20:58
 */
public interface FileScanCallback {
    /**
     * 文件回调
     * @param file
     * @return
     */
    boolean onFile(File file);

    /**
     * 文件夹回调
     * @param file
     * @return
     */
    boolean onDir(File file);
}

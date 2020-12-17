package blxt.qjava.file;

import java.io.File;
import java.net.URI;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/8 11:09
 */
public class CacheFile extends File {

    public CacheFile(String s) {
        super(s);
    }

    public CacheFile(String s, String s1) {
        super(s, s1);
    }

    public CacheFile(File file, String s) {
        super(file, s);
    }

    public CacheFile(URI uri) {
        super(uri);
    }

}

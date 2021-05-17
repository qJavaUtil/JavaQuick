package blxt.qjava.utils.image;

import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * 图片转换工具
 * @author OpenJialei
 * @date 2021年05月15日 23:54
 */
public class ImageConvertrt {

    /**
     * 本地图片转换base64
     * @param imgPath
     * @return
     */
    public static String toBase64(String imgPath) {
        byte[] data = null;
        File file = new File(imgPath);
        if(!file.exists() || file.isDirectory()){
            return null;
        }
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(Objects.requireNonNull(data));
    }


}

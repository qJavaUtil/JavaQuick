package blxt.qjava.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 文件读写工具
 * @author OpenJialei
 * @date 2021年07月01日 18:47
 */
public class ImageFiles {

    /**
     * 导入本地图片到缓冲区
     */
    public static BufferedImage loadImageBuffered(String imgName) {
        try {
            return ImageIO.read(new File(imgName));
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * 从文件获取图片
     * @param imgFile
     * @return
     */
    public static BufferedImage loadImageBuffered(File imgFile) {
        try {
            return ImageIO.read(imgFile);
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * 从网络, 获取 图片
     * @param urlstr
     * @return
     */
    public static BufferedImage loadImageBufferedByUrl(String urlstr) {
        try {
            return ImageIO.read(new URL(urlstr));
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * 生成新图片到本地
     */
    public static void writeImageBuffered(String newImage, BufferedImage img, String formatName) {
        if (newImage != null && img != null) {
            try {
                File outputfile = new File(newImage);
                ImageIO.write(img, formatName, outputfile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 克隆
     * @param bi
     * @return
     */
    public static BufferedImage copyDeep(BufferedImage bi) {

        ColorModel cm = bi.getColorModel();

        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();

        WritableRaster raster = bi.copyData(null);

        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);

    }



    /**
     * 链接url下载图片
     * @param file        本地路径
     * @param urlstr      url链接
     */
    public static void downloadPicture(File file, String urlstr) {
        URL url = null;
        int imageNumber = 0;

        try {
            url = new URL(urlstr);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());


            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context=output.toByteArray();
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

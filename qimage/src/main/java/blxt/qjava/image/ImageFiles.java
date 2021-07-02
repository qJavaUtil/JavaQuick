package blxt.qjava.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

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


}

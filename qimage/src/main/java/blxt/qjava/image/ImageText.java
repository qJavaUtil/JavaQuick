package blxt.qjava.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片文字工具
 * @author OpenJialei
 * @date 2021年07月01日 19:17
 */
public class ImageText {


    public static BufferedImage modifyImagetogeter(BufferedImage src, String subjoin, int x, int y){
        return modifyImagetogeter(src, subjoin, x, y, null, null);
    }
    /**
     * 图片合并
     * @param src       原图
     * @param subjoin   附加文字
     * @param x         附加x起始
     * @param y         附加y起始
     * @return
     */
    public static BufferedImage modifyImagetogeter(BufferedImage src, String subjoin, int x, int y,
                                                   Font font , Color color) {

        try {
            Graphics2D g = src.createGraphics();
            //根据图片的背景设置水印颜色
            if(color != null) {
                g.setColor(color);
            }
            //设置字体
            if (font != null) {
                g.setFont(font);
            }

            //画出水印
            g.drawString(subjoin, x, y);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return src;
    }

}

package blxt.qjava.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片裁剪工具
 * @author OpenJialei
 * @date 2021年07月01日 18:46
 */
public class ImageTailor {

    /**
     * 图片合并
     * @param src       原图
     * @param subjoin   附加图片
     * @param x         附加x起始
     * @param y         附加y起始
     * @return
     */
    public static BufferedImage modifyImagetogeter(BufferedImage src, BufferedImage subjoin,  int x, int y) {

        try {
            int w = subjoin.getWidth();
            int h = subjoin.getHeight();
            Graphics2D  g = src.createGraphics();
            g.drawImage(subjoin, x, y, w, h, null);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return src;
    }




    public static void main(String[] args) {

        // 文件
        BufferedImage a = ImageFiles.loadImageBuffered("E:\\ShiTou\\Desktop\\演示地址\\王冰林结业证书/证书模板.jpg");
        BufferedImage b = ImageFiles.loadImageBuffered("E:\\ShiTou\\Desktop\\演示地址\\王冰林结业证书/盖章.png");
        BufferedImage c = ImageFiles.loadImageBuffered("E:\\ShiTou\\Desktop\\演示地址\\王冰林结业证书/签名.png");

        // 字体
        Font font = new Font("微软雅黑", Font.PLAIN, 75);
        // 字体颜色
        Color color=new Color(51,51,51);

        // 文字
        ImageText.modifyImagetogeter(a, "姓名", 320, 1500, font, color);
        ImageText.modifyImagetogeter(a, "班次", 1100, 1750, font, color);
        ImageText.modifyImagetogeter(a, "编号", 620, 2540, font, color);
        ImageText.modifyImagetogeter(a, "2021 年 7 月 1 日", 1580, 2730, font, color);

        // 贴图
        ImageTailor.modifyImagetogeter(a, b, 1700, 2600);
        ImageTailor.modifyImagetogeter(a, c, 600, 2600);

        // 保存
        ImageFiles.writeImageBuffered("E:\\ShiTou\\Desktop\\演示地址\\王冰林结业证书//new10.jpg", a,"jpg");

        //将多张图片合在一起
        System.out.println("success");
    }



}

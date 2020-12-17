package blxt.qjava.frame.util;
/**
 * @ReadWriteRAW.java
 * @Version 1.0, 2009.5.18
 * @Author Xie-Hua Sun
 * Read *.gif, *.jpg, *.jpeg, *.png, *.raw, *.dat
 * Save as *.RAW images
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Raw 图片转换
 */
public class RawImageConvert extends JPanel
{
    /**
     * 格式转换
     * @param path
     * @param newPath
     * @param width
     * @param height
     * @param tpye  格式:jpg,png
     */
    public void convert(String path, String newPath, int width, int height, String tpye)
    {
        Image image = readRAW(path, width, height);
        savePic(image, width, height, newPath, tpye);
    }


    /**
     * 读取raw文件
     * @param path  文件类型
     * @param iw    宽
     * @param ih    高
     * @return
     */
    public Image readRAW(String path, int iw, int ih)
    {
        int[] pixels = new int[iw*ih];
        try
        {
            FileInputStream fin = new FileInputStream(path);
            DataInputStream in = new DataInputStream(fin);
            //磁盘文件读入数据
            for(int i = 0; i < iw*ih; i++){
                int c = in.readByte();
                if(c < 0){
                    c = c + 256;
                }
                pixels[i] = (255<<24)|(c<<16)|(c<<8)|c;
            }
        }
        catch(IOException e1){
            System.out.println("可能尺寸不对!");
        }

        //将数组中的象素产生一个图像
        ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
        Image image = createImage(ip);
        return  image;
    }


    /**
     * 保存图片
     * @param iamge
     * @param w        宽
     * @param h        高
     * @param path     保存路径
     * @param type     文件类型, 格式:jpg,png
     */
    public void savePic(Image iamge, int w, int h, String path, String type){
        //首先创建一个BufferedImage变量，因为ImageIO写图片用到了BufferedImage变量。
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

        //再创建一个Graphics变量，用来画出来要保持的图片，及上面传递过来的Image变量
        Graphics g = bi.getGraphics();
        try {
            g.drawImage(iamge, 0, 0, null);

        //将BufferedImage变量写入文件中。
            ImageIO.write(bi, type, new File(path));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
package blxt.qjava.image;

import java.awt.image.BufferedImage;

/**
 * 图片转换工具
 * @author OpenJialei
 * @date 2021年09月28日 15:03
 */
public class ImageConver {

    /**
     * 灰度图像提取数组
     * @param image
     * @return int[][]数组
     */
    public static int[][] imageToArray(BufferedImage image){

        int width=image.getWidth();
        int height=image.getHeight();

        int[][] result=new int[height][width];
        for(int j=0;j<height;j++){
            for(int i=0;i<width;i++){
                int rgb=image.getRGB(i, j);
                int grey=(rgb>>16)&0xFF;
                result[j][i]=grey;

            }
        }
        return result ;
    }


    /**
     * 数组转为灰度图像
     * @param sourceArray
     * @return
     */
    public static BufferedImage arrayToGreyImage(int[][] sourceArray){
        int width=sourceArray[0].length;
        int height=sourceArray.length;
        BufferedImage targetImage=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int j=0;j<height;j++){
            for(int i=0;i<width;i++){
                int greyRGB=sourceArray[j][i];
                int rgb=(greyRGB<<16)|(greyRGB<<8)|greyRGB;
                targetImage.setRGB(i, j, rgb);
            }
        }

        return targetImage;
    }

}

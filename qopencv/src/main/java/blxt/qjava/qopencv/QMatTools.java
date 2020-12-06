package blxt.qjava.qopencv;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

/**
 * mat图片工具
 * @Author: Zhang.Jialei
 * @Date: 2020/12/3 18:04
 */
public class QMatTools {

    /**
     * 调整对比度
     * @param image
     * @param alpha  调整对比度参数,如 1.3f
     */
    public static Mat setContrast(Mat image, float alpha) {
        // 初始化一个与输入图像大小深度一致的无数据的Mat对象，用来存储调整后的图像数据
        Mat dst = new Mat(image.size(), image.type());
        //获取图像通道数
        int channels = image.channels();
        //初始化需要的变量
        //存储像素的值
        double[] pixelArr = new double[3];
        //此处也可使用convertTo()方法，传参请看下面伽马校正里的详解
        //image.convertTo（new_image，-1，alpha，beta）;
        for (int i = 0, rlen = image.rows(); i < rlen; i++) {
            for (int j = 0, clen = image.cols(); j < clen; j++) {
                //1 图片为3通道即(B,G,R)
                if (channels == 3) {
                    pixelArr = image.get(i, j).clone();
                    // B G R
                    pixelArr[0] = pixelArr[0]*alpha;
                    pixelArr[1] = pixelArr[1]*alpha;
                    pixelArr[2] = pixelArr[2]*alpha;
                    dst.put(i, j, pixelArr);
                    //2 图片为单通道即灰度图
                } else {
                    pixelArr=image.get(i, j).clone();
                    dst.put(i, j, pixelArr[0]*alpha);
                }
            }
        }
        //HighGui.imshow("线性变化", dst);
        //HighGui.waitKey();

        return dst;
    }

    /**
     * 调整亮度
     * @param image
     * @param bate  调整亮度参数,如 40f
     */
    public static Mat setBrilliance(Mat image, float bate) {
        // 初始化一个与输入图像大小深度一致的无数据的Mat对象，用来存储调整后的图像数据
        Mat dst = new Mat(image.size(), image.type());
        //获取图像通道数
        int channels = image.channels();
        //初始化需要的变量
        //存储像素的值
        double[] pixelArr = new double[3];
        //此处也可使用convertTo()方法，传参请看下面伽马校正里的详解
        //image.convertTo（new_image，-1，alpha，beta）;
        for (int i = 0, rlen = image.rows(); i < rlen; i++) {
            for (int j = 0, clen = image.cols(); j < clen; j++) {
                //1 图片为3通道即(B,G,R)
                if (channels == 3) {
                    pixelArr = image.get(i, j).clone();
                    pixelArr[0] = pixelArr[0]*1+bate;
                    pixelArr[1] = pixelArr[1]*1+bate;
                    pixelArr[2] = pixelArr[2]*1+bate;
                    dst.put(i, j, pixelArr);
                    //2 图片为单通道即灰度图
                } else {
                    pixelArr=image.get(i, j).clone();
                    dst.put(i, j, pixelArr[0]*1+bate);
                }
            }
        }
        HighGui.imshow("线性变化", dst);
        HighGui.waitKey();

        return dst;
    }


}

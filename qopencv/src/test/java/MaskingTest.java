import blxt.qjava.qopencv.OpencvFactory;

import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/3 16:56
 */
public class MaskingTest {

    /**
     * 蒙版测试
     * 给demo图片创建一个绿色,半透明矩形蒙版
     */
    @Test
    public void masking(){

        OpencvFactory.load("E:/Documents/workspace/java/Stpringcloud/JavaQuick/qopencv/src/main/resources/opencv_java420.dll");

        String path1 = "E:/Documents/workspace/java/Stpringcloud/JavaQuick/qopencv/src/test/resources/demo.jpg";
        String path2 = path1 + "_res.jpg";

        // 原始
        Mat mat1 = Imgcodecs.imread(path1);
        new File(path2).delete();

        //扫描后的图片
        Mat hsv_image1 = new Mat(mat1.size(), mat1.type());
        //将图片的格式转为HSV模式,原来为RGB, // 色调（H），饱和度（S），明度（V)
        Imgproc.cvtColor(mat1, hsv_image1, Imgproc.COLOR_BGR2HSV);

        for (int i = 0; i < hsv_image1.rows(); i++) {
            for (int j = 0; j < hsv_image1.cols(); j++) {
                // 获取每个像素
                double[] clone1 = hsv_image1.get(i, j).clone();

                if(i > 50 && i < 200 && j > 50 && j < 200){
                    clone1[1] = 100;
                    mat1.put(i, j, clone1);
                }
            }
        }
        //保存图像到Result目录中
        Imgcodecs.imwrite(path2, mat1);
    }
}

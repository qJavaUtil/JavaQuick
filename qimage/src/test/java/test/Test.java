package test;


import blxt.qjava.image.ImageFiles;
import blxt.qjava.image.ImageFilter;

import java.awt.*;
import java.io.File;

/**
 * @author OpenJialei
 * @date 2021年09月22日 20:28
 */
public class Test {
    static String path = "E:/ZhangJieLei/Documents/workspace/workProject/ocr/image2.png";
    static String path2 = "E:/ZhangJieLei/Documents/workspace/workProject/ocr/image.png";
    static File file = new File(path);
    static File file2 = new File(path2);

    /**
     * @param args
     */
    public static void main(String[] args) {
        test1();
        // TODO Auto-generated method stub

//        ImageFilter imageFilter = new ImageFilter(file);
////        ImageFiles.writeImageBuffered("./二值化.jpg", new ImageFilter(file).changeGrey(), "jpg");
//        ImageFiles.writeImageBuffered("./锐化.jpg", new ImageFilter(file).sharp(), "jpg");
////        ImageFiles.writeImageBuffered("./中值滤波.jpg", new ImageFilter(file).median(), "jpg");
//        ImageFiles.writeImageBuffered("./中值滤波2.jpg", new ImageFilter(file).medianFiltering(), "jpg");
//        ImageFiles.writeImageBuffered("./中值滤波3.jpg", new ImageFilter(new File("./中值滤波2.jpg")).medianFiltering(), "jpg");
//        ImageFiles.writeImageBuffered("./中值滤波4.jpg", new ImageFilter(new File("./中值滤波3.jpg")).medianFiltering(), "jpg");
//        ImageFiles.writeImageBuffered("./中值滤波5.jpg", new ImageFilter(new File("./中值滤波4.jpg")).medianFiltering(), "jpg");
//        ImageFiles.writeImageBuffered("./中值滤波6.jpg", new ImageFilter(new File("./中值滤波5.jpg")).medianFiltering(), "jpg");
////        ImageFiles.writeImageBuffered("./线性灰度变换.jpg", new ImageFilter(file).lineGrey(), "jpg");
////        ImageFiles.writeImageBuffered("./转换为黑白灰度图.jpg", new ImageFilter(file).grayFilter(), "jpg");
////        ImageFiles.writeImageBuffered("./平滑缩放2.jpg", new ImageFilter(file).scaling(2), "jpg");
////        ImageFiles.writeImageBuffered("./平滑缩放5.jpg", new ImageFilter(file).scaling(0.5), "jpg");
////
//
////        ImageFiles.writeImageBuffered("./中值滤波2-二值化.jpg",
////                                                    new ImageFilter(new File("./二值化.jpg")).medianFiltering(),
////                                            "jpg");
//
//        ImageFiles.writeImageBuffered("./对比度-100.jpg", new ImageFilter(file).contrast(100), "jpg");
//        for(int i = 0; i < 100; i++){
//            System.out.println("对比度:" + i);
//            ImageFiles.writeImageBuffered("./对比度2-100.jpg", new ImageFilter(file2).contrast(i), "jpg");
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        ImageFiles.writeImageBuffered("./亮度2-100.jpg", new ImageFilter(file2).lumAdjustment(100), "jpg");
//
//
//        ImageFiles.writeImageBuffered("./中值滤波6-对比度-100.jpg", new ImageFilter(new File("./对比度-100.jpg")).medianFiltering(), "jpg");
//        ImageFiles.writeImageBuffered("./颜色替换-中值滤波6-对比度-100.jpg",
//                                                new ImageFilter(new File("./中值滤波6-对比度-100.jpg"))
//                                                    .convert(new int[]{Color.WHITE.getRGB(), Color.BLACK.getRGB()},
//                                                             new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB()}), "jpg");
//
//
//        ImageFiles.writeImageBuffered("./中值滤波7.jpg", new ImageFilter(new File("./颜色替换-中值滤波6-对比度-100.jpg")).medianFiltering(), "jpg");
//        ImageFiles.writeImageBuffered("./中值滤波7.jpg", new ImageFilter(new File("./中值滤波7.jpg")).medianFiltering(), "jpg");
//        ImageFiles.writeImageBuffered("./中值滤波7.jpg", new ImageFilter(new File("./中值滤波7.jpg")).medianFiltering(), "jpg");
//        ImageFiles.writeImageBuffered("./中值滤波7.jpg", new ImageFilter(new File("./中值滤波7.jpg")).medianFiltering(), "jpg");
//
//        ImageFiles.writeImageBuffered("./中值滤波7-亮度.jpg", new ImageFilter(new File("./中值滤波7.jpg")).lumAdjustment(10), "jpg");
//        ImageFiles.writeImageBuffered("./中值滤波7-对比度.jpg", new ImageFilter(new File("./中值滤波7.jpg")).contrast(150), "jpg");
//
//
//
//        ImageFiles.writeImageBuffered("./灰度 膨胀-100.jpg",
//            new ImageFilter(new File("./中值滤波7-对比度.jpg")).dilate(100), "jpg");
//        ImageFiles.writeImageBuffered("./灰度 膨胀-200.jpg",
//            new ImageFilter(new File("./中值滤波7-对比度.jpg")).dilate(200), "jpg");
//
//        ImageFiles.writeImageBuffered("./灰度 腐蚀-100.jpg",
//            new ImageFilter(new File("./中值滤波7-对比度.jpg")).correde(100), "jpg");
//        ImageFiles.writeImageBuffered("./灰度 腐蚀-200.jpg",
//            new ImageFilter(new File("./中值滤波7-对比度.jpg")).correde(200), "jpg");
    }


    public static void test1() {

//        ImageFiles.writeImageBuffered("./对比度.jpg", new ImageFilter(file2).contrast(100), "jpg");
//        ImageFiles.writeImageBuffered("./黑白灰度图.jpg", new ImageFilter(new File("./对比度.jpg")).grayFilter(), "jpg");
        ImageFiles.writeImageBuffered("./二值化.jpg", new ImageFilter(new File("./黑白灰度图.jpg")).grayImage(), "jpg");

        ImageFiles.writeImageBuffered("./膨胀.jpg",
            new ImageFilter(new File("./二值化.jpg")).dilate(100), "jpg");
        ImageFiles.writeImageBuffered("./膨胀.jpg",
            new ImageFilter(new File("./膨胀.jpg")).dilate(200), "jpg");
        ImageFiles.writeImageBuffered("./膨胀.jpg",
            new ImageFilter(new File("./膨胀.jpg")).dilate(100), "jpg");
        ImageFiles.writeImageBuffered("./膨胀.jpg",
            new ImageFilter(new File("./膨胀.jpg")).dilate(100), "jpg");

        ImageFiles.writeImageBuffered("./降噪.jpg", new ImageFilter(new File("./膨胀.jpg")).denoise(), "jpg");

        ImageFiles.writeImageBuffered("./颜色替换-降噪.jpg",
            new ImageFilter(new File("./降噪.jpg")).convert(), "jpg");

    }
}

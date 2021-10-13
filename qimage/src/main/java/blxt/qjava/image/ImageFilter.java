package blxt.qjava.image;


import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 图像过滤
 *
 * @author OpenJialei
 * @date 2021年09月22日 20:25
 */
public class ImageFilter {
    private BufferedImage image;
    private int iw, ih;
    private int[] pixels;

    ///结构元素
    private static int sData[]={
        0,0,0,
        0,1,0,
        0,1,1
    };

    public ImageFilter(File image) {
        this(ImageFiles.loadImageBuffered(image));
    }

    public ImageFilter(BufferedImage image) {
        this.image = image;
        iw = image.getWidth();
        ih = image.getHeight();
        pixels = new int[iw * ih];
    }

    /**
     * 图像二值化
     */
    public BufferedImage changeGrey() {
        PixelGrabber pg = new PixelGrabber(image.getSource(), 0, 0, iw, ih, pixels, 0, iw);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 设定二值化的域值，默认值为100
        int grey = 100;
        // 对图像进行二值化处理，Alpha值保持不变
        ColorModel cm = ColorModel.getRGBdefault();

        for (int i = 0; i < iw * ih; i++) {
            int red, green, blue;
            int alpha = cm.getAlpha(pixels[i]);
            if (cm.getRed(pixels[i]) > grey) {
                red = 255;
            } else {
                red = 0;
            }
            if (cm.getGreen(pixels[i]) > grey) {
                green = 255;
            } else {
                green = 0;
            }
            if (cm.getBlue(pixels[i]) > grey) {
                blue = 255;
            } else {
                blue = 0;
            }
            pixels[i] = alpha << 24 | red << 16 | green << 8 | blue;
        }
        // 将数组中的象素产生一个图像
        return imageProducerToBufferedImage(new MemoryImageSource(iw, ih, pixels, 0, iw));

    }

    public BufferedImage grayImage()  {

        int width = image.getWidth();
        int height = image.getHeight();
        ColorModel cm =  image.getColorModel();

        BufferedImage grayBufferedImage = new BufferedImage(width, height, image.getType());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                final int color = image.getRGB(i, j);
                final int r = (color >> 16) & 0xff;
                final int g = (color >> 8) & 0xff;
                final int b = color & 0xff;
                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                int alpha = cm.getAlpha(pixels[i * iw + j]);

                if(isWhite(image.getRGB(i, j))){
                    grayBufferedImage.setRGB(i, j, Color.WHITE.getRGB());
                }
                else{
                   // grayBufferedImage.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }

        return grayBufferedImage;

    }


    /**
     * 提升清晰度,进行锐化
     */
    public BufferedImage sharp() {
        PixelGrabber pg = new PixelGrabber(image.getSource(), 0, 0, iw, ih, pixels, 0, iw);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 象素的中间变量
        int tempPixels[] = new int[iw * ih];
        for (int i = 0; i < iw * ih; i++) {
            tempPixels[i] = pixels[i];
        }
        // 对图像进行尖锐化处理，Alpha值保持不变
        ColorModel cm = ColorModel.getRGBdefault();
        for (int i = 1; i < ih - 1; i++) {
            for (int j = 1; j < iw - 1; j++) {
                int alpha = cm.getAlpha(pixels[i * iw + j]);
                // 对图像进行尖锐化
                int red6 = cm.getRed(pixels[i * iw + j + 1]);
                int red5 = cm.getRed(pixels[i * iw + j]);
                int red8 = cm.getRed(pixels[(i + 1) * iw + j]);
                int sharpRed = Math.abs(red6 - red5) + Math.abs(red8 - red5);
                int green5 = cm.getGreen(pixels[i * iw + j]);
                int green6 = cm.getGreen(pixels[i * iw + j + 1]);
                int green8 = cm.getGreen(pixels[(i + 1) * iw + j]);
                int sharpGreen = Math.abs(green6 - green5) + Math.abs(green8 - green5);
                int blue5 = cm.getBlue(pixels[i * iw + j]);
                int blue6 = cm.getBlue(pixels[i * iw + j + 1]);
                int blue8 = cm.getBlue(pixels[(i + 1) * iw + j]);
                int sharpBlue = Math.abs(blue6 - blue5) + Math.abs(blue8 - blue5);

                if (sharpRed > 255) {
                    sharpRed = 255;
                }
                if (sharpGreen > 255) {
                    sharpGreen = 255;
                }
                if (sharpBlue > 255) {
                    sharpBlue = 255;
                }
                tempPixels[i * iw + j] = alpha << 24 | sharpRed << 16 | sharpGreen << 8 | sharpBlue;
            }
        }
        // 将数组中的象素产生一个图像
        return imageProducerToBufferedImage(new MemoryImageSource(iw, ih, tempPixels, 0, iw));
    }

    /**
     * 中值滤波
     */
    public BufferedImage median() {
        PixelGrabber pg = new PixelGrabber(image.getSource(), 0, 0, iw, ih, pixels, 0, iw);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 对图像进行中值滤波，Alpha值保持不变
        ColorModel cm = ColorModel.getRGBdefault();
        for (int i = 1; i < ih - 1; i++) {
            for (int j = 1; j < iw - 1; j++) {
                int red, green, blue;
                int alpha = cm.getAlpha(pixels[i * iw + j]);
                // int red2 = cm.getRed(pixels[(i - 1) * iw + j]);
                int red4 = cm.getRed(pixels[i * iw + j - 1]);
                int red5 = cm.getRed(pixels[i * iw + j]);
                int red6 = cm.getRed(pixels[i * iw + j + 1]);
                // int red8 = cm.getRed(pixels[(i + 1) * iw + j]);
                // 水平方向进行中值滤波
                if (red4 >= red5) {
                    if (red5 >= red6) {
                        red = red5;
                    } else {
                        if (red4 >= red6) {
                            red = red6;
                        } else {
                            red = red4;
                        }
                    }
                } else {
                    if (red4 > red6) {
                        red = red4;
                    } else {
                        if (red5 > red6) {
                            red = red6;
                        } else {
                            red = red5;
                        }
                    }
                }
                // int green2 = cm.getGreen(pixels[(i - 1) * iw + j]);
                int green4 = cm.getGreen(pixels[i * iw + j - 1]);
                int green5 = cm.getGreen(pixels[i * iw + j]);
                int green6 = cm.getGreen(pixels[i * iw + j + 1]);
                // int green8 = cm.getGreen(pixels[(i + 1) * iw + j]);
                // 水平方向进行中值滤波
                if (green4 >= green5) {
                    if (green5 >= green6) {
                        green = green5;
                    } else {
                        if (green4 >= green6) {
                            green = green6;
                        } else {
                            green = green4;
                        }
                    }
                } else {
                    if (green4 > green6) {
                        green = green4;
                    } else {
                        if (green5 > green6) {
                            green = green6;
                        } else {
                            green = green5;
                        }
                    }
                }
                // int blue2 = cm.getBlue(pixels[(i - 1) * iw + j]);
                int blue4 = cm.getBlue(pixels[i * iw + j - 1]);
                int blue5 = cm.getBlue(pixels[i * iw + j]);
                int blue6 = cm.getBlue(pixels[i * iw + j + 1]);
                // int blue8 = cm.getBlue(pixels[(i + 1) * iw + j]);
                // 水平方向进行中值滤波
                if (blue4 >= blue5) {
                    if (blue5 >= blue6) {
                        blue = blue5;
                    } else {
                        if (blue4 >= blue6) {
                            blue = blue6;
                        } else {
                            blue = blue4;
                        }
                    }
                } else {
                    if (blue4 > blue6) {
                        blue = blue4;
                    } else {
                        if (blue5 > blue6) {
                            blue = blue6;
                        } else {
                            blue = blue5;
                        }
                    }
                }
                pixels[i * iw + j] = alpha << 24 | red << 16 | green << 8 | blue;
            }
        }
        // 将数组中的象素产生一个图像
        return imageProducerToBufferedImage(new MemoryImageSource(iw, ih, pixels, 0, iw));
    }

    /**
     * 中值滤波2
     *
     * @return
     */
    public BufferedImage medianFiltering() {
        int w = image.getWidth();
        int h = image.getHeight();
        int[] pix = new int[w * h];
        image.getRGB(0, 0, w, h, pix, 0, w);
        int newpix[] = medianFiltering(pix, w, h);
        image.setRGB(0, 0, w, h, newpix, 0, w);
        BufferedImage outBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        outBufferedImage.setRGB(0, 0, w, h, newpix, 0, w);
        return outBufferedImage;
    }


    /**
     * 线性灰度变换
     */
    public BufferedImage lineGrey() {
        PixelGrabber pg = new PixelGrabber(image.getSource(), 0, 0, iw, ih, pixels, 0, iw);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 对图像进行进行线性拉伸，Alpha值保持不变
        ColorModel cm = ColorModel.getRGBdefault();
        for (int i = 0; i < iw * ih; i++) {
            int alpha = cm.getAlpha(pixels[i]);
            int red = cm.getRed(pixels[i]);
            int green = cm.getGreen(pixels[i]);
            int blue = cm.getBlue(pixels[i]);
            // 增加了图像的亮度
            red = (int) (1.1 * red + 30);
            green = (int) (1.1 * green + 30);
            blue = (int) (1.1 * blue + 30);
            if (red >= 255) {
                red = 255;
            }
            if (green >= 255) {
                green = 255;
            }
            if (blue >= 255) {
                blue = 255;
            }
            pixels[i] = alpha << 24 | red << 16 | green << 8 | blue;
        }
        // 将数组中的象素产生一个图像
        return imageProducerToBufferedImage(new MemoryImageSource(iw, ih, pixels, 0, iw));
    }

    /**
     * 转换为黑白灰度图
     */
    public BufferedImage grayFilter() {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(cs, null);
        return op.filter(image, null);
    }

    /**
     * 平滑缩放
     */
    public BufferedImage scaling(double s) {
        AffineTransform tx = new AffineTransform();
        tx.scale(s, s);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }

    /**
     * 图片亮度调整
     * @param param
     * @throws IOException
     */
    public BufferedImage lumAdjustment(int param) {
        int rgb, R, G, B;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                rgb = image.getRGB(i, j);
                R = ((rgb >> 16) & 0xff) + param;
                G = ((rgb >> 8) & 0xff) + param;
                B = (rgb & 0xff) + param;

                rgb = ((clamp(255) & 0xff) << 24) | ((clamp(R) & 0xff) << 16) | ((clamp(G) & 0xff) << 8)
                    | ((clamp(B) & 0xff));
                image.setRGB(i, j, rgb);
            }
        }
        return image;
    }

    /**
     * 对比度
     * @param contrast
     * @return
     */
    public BufferedImage contrast(int contrast) {
        try {
            int contrast_average = 128;
            //创建一个不带透明度的图片
            BufferedImage back=new BufferedImage(image.getWidth(), image.getHeight(),BufferedImage.TYPE_INT_RGB);
            int width = image.getWidth();
            int height = image.getHeight();
            int pix;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int pixel = image.getRGB(j, i);
                    Color color = new Color(pixel);

                    if (color.getRed() < contrast_average)
                    {
                        pix = color.getRed()- Math.abs(contrast);
                        if (pix < 0) {
                            pix = 0;
                        }
                    }
                    else
                    {
                        pix = color.getRed() + Math.abs(contrast);
                        if (pix > 255) {
                            pix = 255;
                        }
                    }
                    int red= pix;
                    if (color.getGreen() < contrast_average)
                    {
                        pix = color.getGreen()- Math.abs(contrast);
                        if (pix < 0){
                            pix = 0;
                        }
                    }
                    else
                    {
                        pix = color.getGreen() + Math.abs(contrast);
                        if (pix > 255){
                            pix = 255;
                        }
                    }
                    int green= pix;
                    if (color.getBlue() < contrast_average)
                    {
                        pix = color.getBlue()- Math.abs(contrast);
                        if (pix < 0){
                            pix = 0;
                        }
                    }
                    else
                    {
                        pix = color.getBlue() + Math.abs(contrast);
                        if (pix > 255){
                            pix = 255;
                        }
                    }
                    int blue= pix;

                    color = new Color(red,green,blue);
                    int x=color.getRGB();
                    back.setRGB(j,i,x);
                }
            }
            return back;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 反色2
     */
    public BufferedImage convert(){

        int width = image.getWidth();
        int height = image.getHeight();
        ColorModel cm =  image.getColorModel();

        BufferedImage grayBufferedImage = new BufferedImage(width, height, image.getType());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                final int color = image.getRGB(i, j);
                final int r = 0xff - (color >> 16) & 0xff;
                final int g = 0xff - (color >> 8) & 0xff;
                final int b = 0xff - color & 0xff;
                int alpha = cm.getAlpha(pixels[i * iw + j]);
                int newp = alpha << 24 | r << 16 | g << 8 | b;
                grayBufferedImage.setRGB(i, j, newp);
            }
        }

        return grayBufferedImage;
    }

    /**
     * 指定某种颜色替换成另一种
     * @param colors          源颜色调换数组  new int[]{ 源颜色, 新颜色}
     * @return
     * @throws IOException
     */
    public BufferedImage convert(int[] ... colors) {
        int w = image.getWidth();
        int h = image.getHeight();
        int minx = image.getMinTileX();
        int miny = image.getMinTileY();

        for(int i = minx;i<w;i++){
            for(int j = miny;j<h;j++){
                int rgb = image.getRGB(i,j);
                int r = (rgb & 0xff0000) >>16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);

                for(int[] color : colors){
                    int R = (color[0] & 0xff0000) >>16;
                    int G = (color[0] & 0xff00) >> 8;
                    int B = (color[0] & 0xff);
                    if(Math.abs(R -r)<75
                        &&Math.abs(G -g)<75
                        &&Math.abs(B -b )<75 ){
                        image.setRGB(i, j, color[1]);
                    }
                }
            }
        }
        return image;
    }

    /**
     * 腐蚀运算
     * @param threshold  当灰度值大于阈值（小于阈值）时并且结构元素为1（0）时，才认为对应位置匹配上；
     * @return
     */
    public BufferedImage correde(int threshold){
        int[][] source = ImageConver.imageToArray(image);
        source = correde(source, threshold);
        return ImageConver.arrayToGreyImage(source);
    }

    /**
     * 膨胀运算
     * @param threshold 当与运算结果值小于阈值时，图像点的值仍然设为0
     * @return
     */
    public BufferedImage dilate(int threshold){
        int[][] source = ImageConver.imageToArray(image);
        source = dilate(source, threshold);
        return ImageConver.arrayToGreyImage(source);
    }


    /**
     * 降噪，以1个像素点为单位（实际使用中可以循环降噪，或者把单位可以扩大为多个像素点）
     * @return
     */
    public BufferedImage denoise(){
        int w = image.getWidth();
        int h = image.getHeight();
        int white = Color.WHITE.getRGB();
        int black = Color.BLACK.getRGB();

        if(isWhite(image.getRGB(1, 0)) && isWhite(image.getRGB(0, 1)) && isWhite(image.getRGB(1, 1))){
            image.setRGB(0,0,white);
        }
        if(isWhite(image.getRGB(w-2, 0)) && isWhite(image.getRGB(w-1, 1)) && isWhite(image.getRGB(w-2, 1))){
            image.setRGB(w-1,0,white);
        }
        if(isWhite(image.getRGB(0, h-2)) && isWhite(image.getRGB(1, h-1)) && isWhite(image.getRGB(1, h-2))){
            image.setRGB(0,h-1,white);
        }
        if(isWhite(image.getRGB(w-2, h-1)) && isWhite(image.getRGB(w-1, h-2)) && isWhite(image.getRGB(w-2, h-2))){
            image.setRGB(w-1,h-1,white);
        }

        for(int x = 1; x < w-1; x++){
            int y = 0;
            if(isBlack(image.getRGB(x, y))){
                int size = 0;
                if(isWhite(image.getRGB(x-1, y))){
                    size++;
                }
                if(isWhite(image.getRGB(x+1, y))){
                    size++;
                }
                if(isWhite(image.getRGB(x, y+1))){
                    size++;
                }
                if(isWhite(image.getRGB(x-1, y+1))){
                    size++;
                }
                if(isWhite(image.getRGB(x+1, y+1))){
                    size++;
                }
                if(size>=5){
                    image.setRGB(x,y,white);
                }
            }
        }
        for(int x = 1; x < w-1; x++){
            int y = h-1;
            if(isBlack(image.getRGB(x, y))){
                int size = 0;
                if(isWhite(image.getRGB(x-1, y))){
                    size++;
                }
                if(isWhite(image.getRGB(x+1, y))){
                    size++;
                }
                if(isWhite(image.getRGB(x, y-1))){
                    size++;
                }
                if(isWhite(image.getRGB(x+1, y-1))){
                    size++;
                }
                if(isWhite(image.getRGB(x-1, y-1))){
                    size++;
                }
                if(size>=5){
                    image.setRGB(x,y,white);
                }
            }
        }

        for(int y = 1; y < h-1; y++){
            int x = 0;
            if(isBlack(image.getRGB(x, y))){
                int size = 0;
                if(isWhite(image.getRGB(x+1, y))){
                    size++;
                }
                if(isWhite(image.getRGB(x, y+1))){
                    size++;
                }
                if(isWhite(image.getRGB(x, y-1))){
                    size++;
                }
                if(isWhite(image.getRGB(x+1, y-1))){
                    size++;
                }
                if(isWhite(image.getRGB(x+1, y+1))){
                    size++;
                }
                if(size>=5){
                    image.setRGB(x,y,white);
                }
            }
        }

        for(int y = 1; y < h-1; y++){
            int x = w - 1;
            if(isBlack(image.getRGB(x, y))){
                int size = 0;
                if(isWhite(image.getRGB(x-1, y))){
                    size++;
                }
                if(isWhite(image.getRGB(x, y+1))){
                    size++;
                }
                if(isWhite(image.getRGB(x, y-1))){
                    size++;
                }
                //斜上下为空时，去掉此点
                if(isWhite(image.getRGB(x-1, y+1))){
                    size++;
                }
                if(isWhite(image.getRGB(x-1, y-1))){
                    size++;
                }
                if(size>=5){
                    image.setRGB(x,y,white);
                }
            }
        }



        //降噪，以1个像素点为单位
        for(int y = 1; y < h-1; y++){
            for(int x = 1; x < w-1; x++){
                if(isBlack(image.getRGB(x, y))){
                    int size = 0;
                    //上下左右均为空时，去掉此点
                    if(isWhite(image.getRGB(x-1, y))){
                        size++;
                    }
                    if(isWhite(image.getRGB(x+1, y))){
                        size++;
                    }
                    //上下均为空时，去掉此点
                    if(isWhite(image.getRGB(x, y+1))){
                        size++;
                    }
                    if(isWhite(image.getRGB(x, y-1))){
                        size++;
                    }
                    //斜上下为空时，去掉此点
                    if(isWhite(image.getRGB(x-1, y+1))){
                        size++;
                    }
                    if(isWhite(image.getRGB(x+1, y-1))){
                        size++;
                    }
                    if(isWhite(image.getRGB(x+1, y+1))){
                        size++;
                    }
                    if(isWhite(image.getRGB(x-1, y-1))){
                        size++;
                    }
                    if(size>=6){
                        System.out.println("去除黑色早点");
                        image.setRGB(x,y,white);
                    }
                }
                else
                {
                    int size = 0;
                    //上下左右均为空时，去掉此点
                    if(isBlack(image.getRGB(x-1, y))){
                        size++;
                    }
                    if(isBlack(image.getRGB(x+1, y))){
                        size++;
                    }
                    //上下均为空时，去掉此点
                    if(isBlack(image.getRGB(x, y+1))){
                        size++;
                    }
                    if(isBlack(image.getRGB(x, y-1))){
                        size++;
                    }
                    //斜上下为空时，去掉此点
                    if(isBlack(image.getRGB(x-1, y+1))){
                        size++;
                    }
                    if(isBlack(image.getRGB(x+1, y-1))){
                        size++;
                    }
                    if(isBlack(image.getRGB(x+1, y+1))){
                        size++;
                    }
                    if(isBlack(image.getRGB(x-1, y-1))){
                        size++;
                    }
                    if(size>=6){
                        System.out.println("去除白色早点");
                        image.setRGB(x,y,black);
                    }
                }
            }
        }

        return image;
    }

    /**
     * 降噪, 原始图片需要是 二值化的灰图
     * @param size
     * @return
     */
    public BufferedImage denoise(int size) {
        PixelGrabber pg = new PixelGrabber(image.getSource(), 0, 0, iw, ih, pixels, 0, iw);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 设定二值化的域值，默认值为100
        int grey = 100;
        // 对图像进行二值化处理，Alpha值保持不变
        ColorModel cm = ColorModel.getRGBdefault();

        // 如果一个点的周围都是白色的，而它确是黑色的，删除它
        for (int i = 0; i < iw - 1 ; i++) {
            for (i = 1; i < ih - 1; ++i) {

            }
        }
        for (int i = 0; i < iw * ih; i++) {
            int red, green, blue;
            int alpha = cm.getAlpha(pixels[i]);
            if (cm.getRed(pixels[i]) > grey) {
                red = 255;
            } else {
                red = 0;
            }
            if (cm.getGreen(pixels[i]) > grey) {
                green = 255;
            } else {
                green = 0;
            }
            if (cm.getBlue(pixels[i]) > grey) {
                blue = 255;
            } else {
                blue = 0;
            }
            pixels[i] = alpha << 24 | red << 16 | green << 8 | blue;
        }
        // 将数组中的象素产生一个图像
        return imageProducerToBufferedImage(new MemoryImageSource(iw, ih, pixels, 0, iw));

    }

    private static boolean isBlack(int colorInt)
    {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= 300)
        {
            return true;
        }
        return false;
    }

    private static boolean isWhite(int colorInt)
    {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() >= 300)
        {
            return true;
        }
        return false;
    }

    private static int isBlack(int colorInt, int whiteThreshold) {
        final Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= whiteThreshold) {
            return 1;
        }
        return 0;
    }

    /**
     * 腐蚀运算
     * @param source
     * @param threshold 当灰度值大于阈值（小于阈值）时并且结构元素为1（0）时，才认为对应位置匹配上；
     * @return
     */
    private static int[][] correde(int[][] source,int threshold){
        int width=source[0].length;
        int height=source.length;

        int[][] result=new int[height][width];

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                ///边缘不进行操作，边缘内才操作
                if(i>0&&j>0&&i<height-1&&j<width-1){
                    int max =0;

                    ///对结构元素进行遍历
                    for(int k=0;k<sData.length;k++){
                        int x=k/3;///商表示x偏移量
                        int y=k%3;///余数表示y偏移量


                        if(sData[k]!=0){
                            ///不为0时，必须全部大于阈值，否则就设置为0并结束遍历
                            if(source[i-1+x][j-1+y]>=threshold){
                                if(source[i-1+x][j-1+y]>max){
                                    max=source[i-1+x][j-1+y];
                                }
                            }else{
                                // 与结构元素不匹配,赋值0,结束遍历
                                max=0;
                                break;
                            }
                        }
                    }

                    // 此处可以设置阈值，当max小于阈值的时候就赋为0
                    result[i][j]=max;

                }else{
                    ///直接赋值
                    result[i][j]=source[i][j];

                }///end of the most out if-else clause .

            }
        }///end of outer for clause

        return result;
    }

    /**
     * 膨胀运算
     * @param source
     * @param threshold  当与运算结果值小于阈值时，图像点的值仍然设为0
     * @return
     */
    private static int[][] dilate(int[][] source,int threshold){
        int width=source[0].length;
        int height=source.length;

        int[][] result=new int[height][width];

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                ///边缘不进行操作
                if(i>0&&j>0&&i<height-1&&j<width-1){
                    int max =0;

                    ///对结构元素进行遍历
                    for(int k=0;k<sData.length;k++){
                        ///商表示x偏移量
                        int x=k/3;
                        ///余数表示y偏移量
                        int y=k%3;

                        if(sData[k]!=0){
                            ///当结构元素中不为0时,取出图像中对应各项的最大值赋给图像当前位置作为灰度值
                            if(source[i-1+x][j-1+y]>max){
                                max=source[i-1+x][j-1+y];
                            }
                        }
                    }


                    // 此处可以设置阈值，当max小于阈值的时候就赋为0
                    if(max<threshold){
                        result[i][j]=0;
                    }else{
                        result[i][j]=max;
                    }

                }else{
                    ///直接赋值
                    result[i][j]=source[i][j];
                }

            }
        }

        return result;
    }


    /**
     * 判断a,r,g,b值，大于256返回256，小于0则返回0,0到256之间则直接返回原始值
     * @param rgb
     * @return
     */
    private int clamp(int rgb) {
        if (rgb > 255) {
            return 255;
        }
        if (rgb < 0) {
            return 0;
        }
        return rgb;
    }


    public BufferedImage scale(Float s) {

        int srcW = image.getWidth();
        int srcH = image.getHeight();
        int newW = Math.round(srcW * s);
        int newH = Math.round(srcH * s);
        // 先做水平方向上的伸缩变换
        BufferedImage tmp = new BufferedImage(newW, newH, image.getType());
        Graphics2D g = tmp.createGraphics();
        for (int x = 0; x < newW; x++) {
            g.setClip(x, 0, 1, srcH);
            // 按比例放缩
            g.drawImage(image, x - x * srcW / newW, 0, null);
        }

        // 再做垂直方向上的伸缩变换
        BufferedImage dst = new BufferedImage(newW, newH, image.getType());
        g = dst.createGraphics();
        for (int y = 0; y < newH; y++) {
            g.setClip(0, y, newW, 1);
            // 按比例放缩
            g.drawImage(tmp, 0, y - y * srcH / newH, null);
        }
        return dst;
    }

    public static BufferedImage imageProducerToBufferedImage(ImageProducer imageProducer) {
        return imageToBufferedImage(Toolkit.getDefaultToolkit().createImage(imageProducer));
    }

    public static BufferedImage imageToBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
            BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        return bufferedImage;
    }


    /**
     * 中值滤波
     *
     * @param pix 像素矩阵数组
     * @param w   矩阵的宽
     * @param h   矩阵的高
     * @return 处理后的数组
     */
    public int[] medianFiltering(int pix[], int w, int h) {
        int newpix[] = new int[w * h];
        int[] temp = new int[9];
        ColorModel cm = ColorModel.getRGBdefault();
        int r = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (x != 0 && x != w - 1 && y != 0 && y != h - 1) {
                    //g = median[(x-1,y-1) + f(x,y-1)+ f(x+1,y-1)
                    //  + f(x-1,y) + f(x,y) + f(x+1,y)
                    //  + f(x-1,y+1) + f(x,y+1) + f(x+1,y+1)]
                    temp[0] = cm.getRed(pix[x - 1 + (y - 1) * w]);
                    temp[1] = cm.getRed(pix[x + (y - 1) * w]);
                    temp[2] = cm.getRed(pix[x + 1 + (y - 1) * w]);
                    temp[3] = cm.getRed(pix[x - 1 + (y) * w]);
                    temp[4] = cm.getRed(pix[x + (y) * w]);
                    temp[5] = cm.getRed(pix[x + 1 + (y) * w]);
                    temp[6] = cm.getRed(pix[x - 1 + (y + 1) * w]);
                    temp[7] = cm.getRed(pix[x + (y + 1) * w]);
                    temp[8] = cm.getRed(pix[x + 1 + (y + 1) * w]);
                    Arrays.sort(temp);
                    r = temp[4];
                    newpix[y * w + x] = 255 << 24 | r << 16 | r << 8 | r;
                } else {
                    newpix[y * w + x] = pix[y * w + x];
                }
            }
        }
        return newpix;
    }

    public int[] snnFiltering(int pix[], int w, int h) {
        int newpix[] = new int[w * h];
        int n = 9;
        int temp, i1, i2, sum;
        int[] temp1 = new int[n];
        int[] temp2 = new int[n / 2];
        ColorModel cm = ColorModel.getRGBdefault();
        int r = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (x != 0 && x != w - 1 && y != 0 && y != h - 1) {
                    sum = 0;
                    temp1[0] = cm.getRed(pix[x - 1 + (y - 1) * w]);
                    temp1[1] = cm.getRed(pix[x + (y - 1) * w]);
                    temp1[2] = cm.getRed(pix[x + 1 + (y - 1) * w]);
                    temp1[3] = cm.getRed(pix[x - 1 + (y) * w]);
                    temp1[4] = cm.getRed(pix[x + (y) * w]);
                    temp1[5] = cm.getRed(pix[x + 1 + (y) * w]);
                    temp1[6] = cm.getRed(pix[x - 1 + (y + 1) * w]);
                    temp1[7] = cm.getRed(pix[x + (y + 1) * w]);
                    temp1[8] = cm.getRed(pix[x + 1 + (y + 1) * w]);
                    for (int k = 0; k < n / 2; k++) {
                        i1 = Math.abs(temp1[n / 2] - temp1[k]);
                        i2 = Math.abs(temp1[n / 2] - temp1[n - k - 1]);
                        temp2[k] = i1 < i2 ? temp1[k] : temp1[n - k - 1];  //选择最接近原像素值的一个邻近像素
                        sum = sum + temp2[k];
                    }
                    r = sum / (n / 2);
                    //System.out.println("pix:" + temp1[4] + "  r:" + r);
                    newpix[y * w + x] = 255 << 24 | r << 16 | r << 8 | r;
                } else {
                    newpix[y * w + x] = pix[y * w + x];
                }
            }
        }
        return newpix;
    }

    public int ostu(int[][] gray, int w, int h) {
        int[] histData = new int[w * h];
        // Calculate histogram
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int red = 0xFF & gray[x][y];
                histData[red]++;
            }
        }

        // Total number of pixels
        int total = w * h;

        float sum = 0;
        for (int t = 0; t < 256; t++) {
            sum += t * histData[t];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++) {
            wB += histData[t]; // Weight Background
            if (wB == 0) {
                continue;
            }

            wF = total - wB; // Weight Foreground
            if (wF == 0) {
                break;
            }

            sumB += (float) (t * histData[t]);

            float mB = sumB / wB; // Mean Background
            float mF = (sum - sumB) / wF; // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }

        return threshold;
    }

}
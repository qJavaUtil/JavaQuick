package blxt.qjava.image;


import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

/**
 * 图片比较
 * 汉明距离算法实现图像内容相似度比较算法：
 * @author OpenJialei
 * @date 2021年09月29日 12:19
 */
public class ImagePHash {

    private int size = 32;
    private int smallerSize = 8;

    public ImagePHash() {
        initCoefficients();
    }

    public ImagePHash(int size, int smallerSize) {
        this.size = size;
        this.smallerSize = smallerSize;

        initCoefficients();
    }

    public int[] getData(BufferedImage img) {
        try {
            BufferedImage slt = new BufferedImage(100, 100,
                BufferedImage.TYPE_INT_RGB);
            slt.getGraphics().drawImage(img, 0, 0, 100, 100, null);
            // ImageIO.write(slt,"jpeg",new File("slt.jpg"));
            int[] data = new int[256];
            for (int x = 0; x < slt.getWidth(); x++) {
                for (int y = 0; y < slt.getHeight(); y++) {
                    int rgb = slt.getRGB(x, y);
                    Color myColor = new Color(rgb);
                    int r = myColor.getRed();
                    int g = myColor.getGreen();
                    int b = myColor.getBlue();
                    data[(r + g + b) / 3]++;
                }
            }
            // data 就是所谓图形学当中的直方图的概念
            return data;
        } catch (Exception exception) {
            System.out.println("有文件没有找到,请检查文件是否存在或路径是否正确");
            return null;
        }
    }

    public float compare(int[] s, int[] t) {
        try {
            float result = 0F;
            for (int i = 0; i < 256; i++) {
                int abs = Math.abs(s[i] - t[i]);
                int max = Math.max(s[i], t[i]);
                result += (1 - ((float) abs / (max == 0 ? 1 : max)));
            }
            return (result / 256) * 100;
        } catch (Exception exception) {
            return 0;
        }
    }

    public int distance(String s1, String s2) {
        int counter = 0;
        for (int k = 0; k < s1.length(); k++) {
            if (s1.charAt(k) != s2.charAt(k)) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * 返回一个'二进制字符串'(比如。001010111011100010)，这很容易做汉明距离。
     * @param is
     * @return
     * @throws Exception
     */
    public String getHash(InputStream is) throws Exception {
        BufferedImage img = ImageIO.read(is);

        /*
         * 1.
         * 减少尺寸。和Average Hash一样，pHash从一个小图像开始。
         * 然而，图像大于8x8;32x32的尺寸比较合适。
         * 这样做实际上是为了简化DCT计算，而不是因为它需要减少高频。
         */
        img = resize(img, size, size);

        /*
         * 2. 减少颜色。为了进一步简化计算次数，图像被简化为灰度。
         */
        img = grayscale(img);

        double[][] vals = new double[size][size];

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                vals[x][y] = getBlue(img, x, y);
            }
        }

        /*
         * 3. 计算DCT。DCT将图像分割成一组频率和标量。
         *    JPEG使用8x8 DCT，而这个算法使用32x32 DCT。
         */
        long start = System.currentTimeMillis();
        double[][] dctVals = applyDCT(vals);
        System.out.println("DCT: " + (System.currentTimeMillis() - start));

        /*
         * 4. 减少DCT。这是神奇的一步。
         * DCT是32x32，保持左上角的8x8即可。这些代表了图中最低的频率。
         */
        /*
         * 5. 计算平均值。与平均哈希一样，
         * 计算平均DCT值(仅使用8 × 8 DCT低频值，并排除第一项，因为DC系数可能与其他值有显著差异，并将偏离平均值)。
         */
        double total = 0;

        for (int x = 0; x < smallerSize; x++) {
            for (int y = 0; y < smallerSize; y++) {
                total += dctVals[x][y];
            }
        }
        total -= dctVals[0][0];

        double avg = total / (double) ((smallerSize * smallerSize) - 1);

        /*
         * 6. 进一步减少DCT。这是神奇的一步。
         * 根据64个DCT值是高于还是低于平均值，
         * 将64个散列位设置为0或1。
         * 结果并没有告诉我们实际的低频;
         * 它只告诉我们频率相对于均值的大致相对比例。
         * 只要图像的整体结构保持不变，结果就不会发生变化;
         * 这可以幸存伽玛和颜色直方图调整没有问题。
         */
        String hash = "";

        for (int x = 0; x < smallerSize; x++) {
            for (int y = 0; y < smallerSize; y++) {
                if (x != 0 && y != 0) {
                    hash += (dctVals[x][y] > avg ? "1" : "0");
                }
            }
        }

        return hash;
    }

    private BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    private ColorConvertOp colorConvert = new ColorConvertOp(
        ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

    private BufferedImage grayscale(BufferedImage img) {
        colorConvert.filter(img, img);
        return img;
    }

    private static int getBlue(BufferedImage img, int x, int y) {
        return (img.getRGB(x, y)) & 0xff;
    }

    // DCT function stolen from
    // http://stackoverflow.com/questions/4240490/problems-with-dct-and-idct-algorithm-in-java

    private double[] c;

    private void initCoefficients() {
        c = new double[size];

        for (int i = 1; i < size; i++) {
            c[i] = 1;
        }
        c[0] = 1 / Math.sqrt(2.0);
    }

    private double[][] applyDCT(double[][] f) {
        int N = size;

        double[][] F = new double[N][N];
        for (int u = 0; u < N; u++) {
            for (int v = 0; v < N; v++) {
                double sum = 0.0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        sum += Math
                            .cos(((2 * i + 1) / (2.0 * N)) * u * Math.PI)
                            * Math.cos(((2 * j + 1) / (2.0 * N)) * v
                            * Math.PI) * (f[i][j]);
                    }
                }
                sum *= ((c[u] * c[v]) / 4.0);
                F[u][v] = sum;
            }
        }
        return F;
    }



    public static void main(String[] args) {

        String path = "E:/ZhangJieLei/Documents/workspace/workProject/ocr/image.png";
        String path2 = "E:/ZhangJieLei/Documents/workspace/workProject/ocr/image2.png";

        ImagePHash p = new ImagePHash();
        String image1;
        String image2;

        float percent = p.compare(p.getData(ImageFiles.loadImageBuffered(path)),
                                  p.getData(ImageFiles.loadImageBuffered(path2)));
        if (percent == 0) {
            System.out.println("无法比较");
        } else {
            System.out.println("两张图片的相似度为：" + percent + "%");
        }

        try {
            image1 = p.getHash(new FileInputStream(new File( path)));
            image2 = p.getHash(new FileInputStream(new File( path2)));
            // 结果说明：汉明距离越大表明图片差异越大，如果不相同的数据位不超过5，就说明两张图片很相似；如果大于10，就说明这是两张不同的图片。
            System.out.println("不一致像素比例 " + p.distance(image1, image2));
            System.out.println(image1);
            System.out.println(image2);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

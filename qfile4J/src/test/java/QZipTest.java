import blxt.qjava.file.QZip;

import java.io.File;

public class QZipTest {

    public static void main(String[] args) throws Exception {
        File file = new File("E:\\Documents\\workspace\\workProject\\Ideh-local\\Eclipse2022-4\\plugins2\\org.yaml.snakeyaml_1.27.0.v20201111-1638");
        QZip.makeZip(file, new File(file.getAbsolutePath() + ".jar"));
    }
}

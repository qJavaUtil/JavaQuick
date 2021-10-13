package blxt.qjava.cipher;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5 工具
 * @author OpenJialei
 * @date 2021年09月20日 11:53
 */
public class MD5Helper {

    /**
     * A + B 钥 加密
     * @param keyA
     * @param keyB
     * @return
     */
    public static String getMd5(String keyA, String keyB){
        return getMd5(keyA + keyB);
    }

    /**
     * 单个加密
     */
    public static String getMd5(String key){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("加密失败");
            e.printStackTrace();
            return null;
        }
        md.update(key.getBytes(StandardCharsets.UTF_8));
        return new BigInteger(1, md.digest()).toString(16);
    }

}

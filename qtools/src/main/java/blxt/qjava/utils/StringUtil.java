package blxt.qjava.utils;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author OpenJialei
 * @date 2021年12月22日 20:49
 */
public class StringUtil {

   static final String SPLIT_KEY_DEFAULT = "-|\\.| |_|:|：|，|,";

    /**
     * 首字母大写
     * @param name
     * @return
     */
    public static String toUpperCaseFirst(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);

    }

    /**
     * 分词
     * @param text
     * @return
     * @throws Exception
     */
    public static String[] split(String text) throws Exception {
        return split(text, SPLIT_KEY_DEFAULT);
    }

    /**
     * 分词
     * @param text
     * @return
     * @throws Exception
     */
    public static String[] split(String text, String key) throws Exception {
        return text.split(key);
    }

    /**
     * 分词器分词
     * @param text
     * @return
     * @throws Exception
     */
    public static List<String> splitFenCi(String text) throws Exception {
        Set<String> set = new HashSet<>();
        StringReader re = new StringReader(text.trim());
        IKSegmenter ik = new IKSegmenter(re, true);
        Lexeme lex;
        while ((lex = ik.next()) != null) {
            set.add(lex.getLexemeText());
        }
        return new ArrayList<>(set);
    }

    /**
     * 清理符号
     * @return
     */
    public static String clearSymbol(String text){
        String str = "[`\\\\~!@#$%^&*()+=|{}':;,\\[\\].<>＜＞《》/?！￥…（）—【】‘；：”“’。，、？]";
        return text.replaceAll(str,"").trim();
    }

    /**
     * 清理名称, 除去不能存在文件路径中的特殊符号
     * @return
     */
    public static String clearFileName(String text){
        String str = "[`^*|':,\\[\\].＜＞?~！…（）—+{}‘；：’。，、？]";
        return text.replaceAll(str,"").trim();
    }

    public static String MD5(String pwd){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md.update(pwd.getBytes(StandardCharsets.UTF_8));
        byte[] result = md.digest();
        return new BigInteger(1, result).toString(16);
    }

    public static void main(String[] args) {
        String text = "mopojmni  mo播＜sjfosdf133??45234?545^&*%$%^年后23";
        System.out.println(clearSymbol(text));
    }

}

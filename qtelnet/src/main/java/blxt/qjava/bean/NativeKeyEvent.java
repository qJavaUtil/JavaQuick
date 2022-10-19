package blxt.qjava.bean;

import lombok.Data;

/**
 * Shell操作信息对象
 * @author OpenJialei
 * @since 2022年10月08日 16:51
 */
@Data
public class NativeKeyEvent {

    /** 操作码， 1通用字符 | 2组合键  */
    int opt;
    /** 键盘码 */
    int code;
    /** 键盘字符 */
    String word;
}

package blxt.qjava.utils.exception;

/**
 * 自定义数据异常
 * @Author: Zhang.Jialei
 * @Date: 2020/9/6 19:45
 */
public class DataException extends RuntimeException{

    private String code;
    private String msg;
    private Object data;

    public DataException(String code, String msg, Object data) {
        super(String.format(code == null ? "%s" : "%s, 错误码:%s", msg, code));
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }


}

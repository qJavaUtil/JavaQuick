package blxt.qjava.autovalue.bean;

import lombok.Data;

/**
 * 符号枚举
 * @author OpenJialei
 * @date 2021年06月27日 13:39
 */
public enum JoinEnum {
    /** 默认联查 */
    PG_JOIN("JOIN") ,
    /** 内连接 */
    PG_INNER_JOIN("INNER JOIN") ,
    /** 左连接 */
    PG_LEFT_JOIN("LEFT JOIN") ,
    /** 左外连接 */
    PG_LEFT_OUTER_JOIN("LEFT OUTER JOIN") ,
    /** 右连接 */
    PG_RIGHT_JOIN("RIGHT JOIN") ,
    /** 右外连接 */
    PG_RIGHT_OUT_JOIN("RIGHT OUT JOIN") ,
    /** 外连接 */
    PG_FULL_OUTER_JOIN("FULL OUTER JOIN") ,

    /** 交叉连接 */
    PG_("CROSS JOIN") ;

    String value;

    JoinEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

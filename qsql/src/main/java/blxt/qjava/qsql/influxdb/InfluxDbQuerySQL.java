package blxt.qjava.qsql.influxdb;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Formatter;
import java.util.Map;

/**
 * influxdb查询语句
 * @Author: Zhang.Jialei
 * @Date: 2020/8/7 15:40
 */
@Data
public class InfluxDbQuerySQL {
    /**                                     行名      表名            开始时间 结束时间 分组 排序 限制 分页偏移 时区*/
    public static final String SQL = "SELECT %s FROM \"%s\" WHERE true %s %s %s %s %s %s %s TZ('%s')";
    String columns = "*";
    /** * 表名 */
    String table;
    /** 开始时间,不能为空, 如果用时间字符,需要用单引号包裹 */
    String timeStart;
    /** 结束时间, 如果用时间字符,需要用单引号包裹, 或者用now()-1d等 */
    String timeEnd ;
    /** 更多筛选 */
    String rule;
    /** 如果group不为null,就组装一下语句,分组只能是*或者tagkey,字段不能是sql关键字 */
    String group;
    /** 排序,只支持时间顺序和倒序 : desc/asc */
    String order;
    /** 限制结果数量 */
    String limit;
    /** 分页偏移 */
    String offset;
    /** 默认时区 : 上海 Asia/Shanghai  */
    String timezone = "Asia/Shanghai";

    Map<String, String> linkedHashMap = Maps.newLinkedHashMap();


    /**
     * 构建sql语句
     * @return
     */
    public String build(){
        // table不能为空
        if(table == null || table.trim().isEmpty()){
            return null;
        }
        // columns不能为空
        if(columns == null || columns.trim().isEmpty()){
            return null;
        }

        // 开始时间
        if(timeStart != null && !timeStart.trim().isEmpty()){
            timeStart = "and time >= " + timeStart;
        }
        else{
            timeStart = "";
        }
        // 结束时间
        if(timeEnd != null && !timeEnd.trim().isEmpty()){
            timeEnd = "and time <= " + timeEnd;
        }
        else{
            timeEnd = "";
        }
        // 其他筛选条件
        if(rule != null && !rule.trim().isEmpty()){
            rule = "and" + rule;
        }
        else{
            rule = "";
        }
        // 分组
        if(group != null && !group.trim().isEmpty()){
            group = "GROUP BY " + group;
        }
        else{
            group = "";
        }
        // 排序,目前只支持时间排序
        if(order != null && !order.trim().isEmpty()){
            order = "ORDER BY time " + order;
        }
        else{
            order = "";
        }
        // 分页限制
        if(limit != null && !limit.trim().isEmpty()){
            limit = "LIMIT " + limit;
        }
        else{
            limit = "";
        }
        // 分页偏移
        if(offset != null && !offset.trim().isEmpty()){
            offset = "OFFSET " + offset;
        }
        else{
            offset = "";
        }
        return String.format(SQL, columns, table, timeStart, timeEnd, rule, group, order, limit, offset, timezone);
    }

    private String format(String format, Object arg) {

        String format1 = null, format2 = null;
        int index;

        return new Formatter().format(format1, arg).toString() + format2;
    }

}

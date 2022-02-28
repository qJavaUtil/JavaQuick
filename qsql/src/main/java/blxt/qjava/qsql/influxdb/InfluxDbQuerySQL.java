package blxt.qjava.qsql.influxdb;

import lombok.Data;


/**
 * influxdb查询语句
 * @Author: Zhang.Jialei
 * @Date: 2020/8/7 15:40
 */
@Data
public class InfluxDbQuerySQL {
    /**                               行名      表名 查询条件 分组 排序 限制 分页偏移 时区*/
    //public static final String SQL = "SELECT %s FROM \"%s\" %s %s %s %s %s TZ('%s')";
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

    /**
     * 构建sql语句
     * @return
     */
    public String build(){
        String columns = this.columns;
        String table = this.table;

        // table不能为空
        if(table == null || table.trim().isEmpty()){
            return null;
        }
        // columns不能为空
        if(columns == null || columns.trim().isEmpty()){
            return null;
        }

        return getSelectHead() + getWhere() + getGroup();
    }

    /**
     * 获取查询头
     * @return
     */
    private String getSelectHead(){
        StringBuilder select = new StringBuilder();
        select.append("SELECT ");
        select.append(this.columns);
        select.append(" FROM ");
        select.append("\"");
        select.append(this.table);
        select.append("\" ");
        return select.toString();
    }

    /**
     * 拼接查询条件
     * @return
     */
    private String getWhere(){
        StringBuilder where = new StringBuilder();

        // 开始时间
        if(timeStart != null && !timeStart.trim().isEmpty()){
            where.append(" time >= ");
            where.append(timeStart);
        }
        // 结束时间
        if(timeEnd != null && !timeEnd.trim().isEmpty()){
            if(where.length() > 0){
                where.append(" and ");
            }
            where.append("time <= ");
            where.append(timeEnd);
        }

        // 其他筛选条件
        if(rule != null && !rule.trim().isEmpty()){
            if(where.length() > 0){
                where.append(" and ");
            }
            where.append(rule);
        }
        if(where.length() > 0){
           return " WHERE " + where.toString();
        }

        return "";
    }


    /**
     * 获取其他分组
     * @return
     */
    private String getGroup(){
        StringBuilder stringBuilder = new StringBuilder();
        // 分组
        if(group != null && !group.trim().isEmpty()){
            stringBuilder.append( " GROUP BY ");
            stringBuilder.append(group);
        }

        // 排序,目前只支持时间排序
        if(order != null && !order.trim().isEmpty()){
            stringBuilder.append(" ORDER BY time ");
            stringBuilder.append(order);
        }

        // 分页限制
        if(limit != null && !limit.trim().isEmpty()){
            stringBuilder.append(" LIMIT ");
            stringBuilder.append(limit);
        }

        // 分页偏移
        if(offset != null && !offset.trim().isEmpty()){
            stringBuilder.append(" OFFSET ");
            stringBuilder.append(offset);
        }

        // 时间
        if(timezone != null && !timezone.trim().isEmpty()){
            stringBuilder.append(String.format(" TZ('%s')", timezone));
        }
        return stringBuilder.toString();
    }

}

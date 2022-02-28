package blxt.qjava.qsql.utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import blxt.qjava.autovalue.inter.SqlBean;
import blxt.qjava.autovalue.inter.SqlColumn;
import blxt.qjava.autovalue.util.ObjectValue;

/**
 * 数据库查询结转换工具
 */
public class ResultSetMapper<T> {
    @SuppressWarnings("unchecked")
    public List<T> toObject(ResultSet rs, Class outputClass) {
        List<T> outputList = null;
        if (rs == null) {
            return null;
        }
        if (!outputClass.isAnnotationPresent(SqlBean.class)) {
            return null;
        }
        try {
            Field[] fields = outputClass.getDeclaredFields();
            while (rs.next()) {
                T bean = (T) outputClass.newInstance();
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int _iterator = 0; _iterator < rsmd.getColumnCount(); _iterator++) {
                    String columnName = rsmd.getColumnName(_iterator + 1);
                    Object columnValue = rs.getObject(_iterator + 1);
                    if(columnValue == null){
                        continue;
                    }
                    for (Field field : fields) {
                        // 过滤 final 元素
                        if (!field.isAnnotationPresent(SqlColumn.class) || (field.getModifiers() & 16) != 0) {
                            continue;
                        }
                        SqlColumn column = field.getAnnotation(SqlColumn.class);
                        if (column.value().equalsIgnoreCase(columnName) ) {
                            ObjectValue.setObjectValue(bean, field, columnValue, true);
                            break;
                        }
                    }
                }
                if (outputList == null) {
                    outputList = new ArrayList<T>();
                }
                outputList.add(bean);
            }

        } catch (IllegalAccessException | SQLException | InstantiationException e) {
            e.printStackTrace();
        }
        return outputList;
    }


    /**
     * 将infux查询结果转换成map集合
     * @param rs   查询结果
     * @param leng 查询长度
     * @return
     */
    public static List<Map<String, Object>> toArray(ResultSet rs, int leng){
        List<Map<String, Object>> outputList = new ArrayList<>(leng);
        if (rs == null) {
            return null;
        }
        try {
            while (rs.next()) {
                // 获取原始字段
                ResultSetMetaData rsmd = rs.getMetaData();
                Map<String, Object> map = new LinkedHashMap<>(rsmd.getColumnCount());
                // 获取值
                for (int _iterator = 0; _iterator < rsmd.getColumnCount(); _iterator++) {
                    String columnName = rsmd.getColumnName(_iterator + 1);
                    Object columnValue = rs.getObject(_iterator + 1);
                    if(columnValue == null){
                        continue;
                    }
                    map.put(columnName, columnValue);
                }
                outputList.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outputList;
    }


}

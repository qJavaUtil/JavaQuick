package blxt.qjava.qsql.influxdb;

import blxt.qjava.autovalue.util.ObjectValue;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * influx查询结果适配
 * @param <T>
 */
public class QueryResultMapper<T> {

    /**
     * influx查询结果适配, 官方自带方法
     * @param rs
     * @param outputClass
     * @return
     */
    public List<T> toPOJO(QueryResult rs, Class<T> outputClass){
        InfluxDBResultMapper mapper = new InfluxDBResultMapper();
        return mapper.toPOJO(rs, outputClass);
    }

    /**
     * influx查询结果适配
     * @param rs             influx 查询结果
     * @param outputClass    适配类
     * @return
     * @throws Exception
     */
    public List<T> toObject(QueryResult rs, Class<T> outputClass) throws Exception {
        if (rs == null) {
            return null;
        }

        List<T> beans = new ArrayList<>();
        List<QueryResult.Result> results = rs.getResults();
        // 遍历 Result
        for (QueryResult.Result result : results) {
            List<QueryResult.Series>  series = result.getSeries();
            // 遍历 Series
            for (QueryResult.Series series1 : series) {
                if(series1 == null) continue;
                Map<String, String> tags = series1.getTags();
                List<String> columns = series1.getColumns();
                List<List<Object>> values = series1.getValues();

                List<Field> fields = new ArrayList<>();

                // 获取到元素
                for (String column : columns) {
                    Field field = outputClass.getField(column);
                    fields.add(field);
                }

                // 按顺序,适配变量
                for (List<Object> value : values) {
                    int fid = 0;
                    T bean = outputClass.newInstance();
                    for (Object o : value) {
                        if(o == null)continue;
                        ObjectValue.setObjectValue(bean,fields.get(fid), o, true);
                        fid++;
                    }
                    beans.add(bean);
                }
            }
        }
        return beans;
    }
}

package blxt.qjava.qsql.influxdb;

import blxt.qjava.autovalue.util.ObjectValue;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * influx查询结果适配
 * @param <T>
 */
@Slf4j
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
            if(series == null){
                log.warn("查询结果空");
                return null;
            }
            // 遍历 Series
            for (QueryResult.Series series1 : series) {
                if(series1 == null){ continue;}
                Map<String, String> tags = series1.getTags();
                List<String> columns = series1.getColumns();
                List<List<Object>> values = series1.getValues();

                List<Field> fields = new ArrayList<>();

                // 获取到元素
                for (String column : columns) {
                    try {
                        Field field = outputClass.getField(column);
                        if(field == null){
                            continue;
                        }
                        fields.add(field);
                    }catch (Exception e){
                        log.warn("未匹配的字段:{}->{}", outputClass, column);
                    }
                }

                // 按顺序,适配变量
                for (List<Object> value : values) {
                    int fid = 0;
                    T bean = outputClass.newInstance();
                    for (Object o : value) {
                        if(o == null){ continue; }
                        if(fid >= fields.size()){
                            break;
                        }
                        try {
                            ObjectValue.setObjectValue(bean,fields.get(fid), o, true);
                        } catch (Exception var5) {
                            log.error("赋值异常:{}<-{}", fields.get(fid).getName(), o);
                            var5.printStackTrace();
                        }finally {
                            fid++;
                        }
                    }
                    beans.add(bean);
                }
            }
        }
        return beans;
    }


    /**
     * 将infux查询结果转换成map集合
     * @param rs   查询结果
     * @return
     */
    public static List<Map<String, Object>> toArray(QueryResult rs){
        if (rs == null) {
            return null;
        }
        // 初始化返回类
        List<QueryResult.Result> results = rs.getResults();
        List<Map<String, Object>> outputList = new ArrayList<>(rs.getResults().size());
        // 遍历 Result
        for (QueryResult.Result result : results) {
            List<QueryResult.Series>  series = result.getSeries();
            if(series == null){
                log.warn("查询结果空");
                return null;
            }
            // 遍历 Series
            for (QueryResult.Series series1 : series) {
                if(series1 == null){ continue;}
                List<String> columns = series1.getColumns();
                List<List<Object>> values = series1.getValues();
                // 获取到元素
                for (List<Object> value : values) {
                    Map<String, Object> map = new LinkedHashMap<>(columns.size());
                    for (int i = 0; i < columns.size(); i++) {
                        map.put(columns.get(i), value.get(i));
                    }
                    outputList.add(map);
                }
            }
        }
        return outputList;
    }

}

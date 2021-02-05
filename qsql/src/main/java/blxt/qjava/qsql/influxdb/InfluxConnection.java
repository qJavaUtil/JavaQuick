package blxt.qjava.qsql.influxdb;


import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.*;
import org.influxdb.dto.Point.Builder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * InfluxDB数据库连接操作类
 *
 * @author Mi
 */
public class InfluxConnection {


    private InfluxDB influxDB;
    InfluxBean influxBean;

    /**
     * @param influxBean        用户名
     */
    public InfluxConnection(InfluxBean influxBean) {
        this.influxBean = influxBean;
        influxDbBuild();
    }



//    public boolean isConnect(){
//        boolean flag = false;
//        influxDB.is
//
//        return flag;
//    }


    /**
     * 创建数据库
     *
     * @param dbName
     */
    @SuppressWarnings("deprecation")
    public void createDB(String dbName) {
        influxDB.createDatabase(dbName);
    }

    /**
     * 删除数据库
     *
     * @param dbName
     */
    @SuppressWarnings("deprecation")
    public void deleteDB(String dbName) {
        influxDB.deleteDatabase(dbName);
    }

    /**
     * 测试连接是否正常
     *
     * @return true 正常
     */
    public boolean ping() {
        boolean isConnected = false;
        Pong pong;
        try {
            pong = influxDB.ping();
            if (pong != null) {
                isConnected = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isConnected;
    }

    /**
     * 连接时序数据库 ，若不存在则创建
     *
     * @return
     */
    public InfluxDB influxDbBuild() {
        if (influxDB == null) {
            influxDB = InfluxDBFactory.connect(influxBean.openurl, influxBean.username, influxBean.password);
        }
        if (influxDB == null) {
            return null;
        }
        try {
            if (influxBean.isCreatDateBase && !influxDB.databaseExists(influxBean.database)) {
                createDB(influxBean.database);
                createDefaultRetentionPolicy( influxBean.getRetentionTimeStr());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            influxDB.setRetentionPolicy(influxBean.getRetentionPolicyStr());
        }
        influxDB.setLogLevel(InfluxDB.LogLevel.NONE);
        return influxDB;
    }

    /**
     * 重新链接
     * @return
     */
    public InfluxDB reBuild(){
        if(influxDB != null){
            influxDB.close();
            influxDB = null;
        }

        return influxDbBuild();
    }

    /**
     * 创建自定义保留策略
     *
     * @param policyName  策略名
     * @param duration    保存时间
     * @param replication 保存副本数量
     * @param isDefault   是否设为默认保留策略
     */
    public void createRetentionPolicy(String policyName, String duration, int replication, Boolean isDefault) {
        String sql = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s %s", policyName,
                influxBean.database, duration, replication, isDefault ? "DEFAULT" : "");
        this.query(sql);
    }

    /**
     * 创建默认的保留策略
     * default，保存天数：30天，保存副本数量：1
     * 设为默认保留策略
     * time为0h时,认为是永久保存
     *
     * @param times 保存时长, xxD :周w, 天d,小时h,分钟m,秒s
     */
    public void createDefaultRetentionPolicy(String times) {
        createRetentionPolicy("default", times, 1, true);
    }

    /**
     * 查询
     *
     * @param command 查询语句
     * @return
     */
    public QueryResult query(String command) {
        return influxDB.query(new Query(command, influxBean.database));
    }

    /**
     * 插入
     *
     * @param measurement 表
     * @param tags        标签
     * @param fields      字段
     */
    public void insert(String measurement, Map<String, String> tags, Map<String, Object> fields, long time,
                       TimeUnit timeUnit) {
        Builder builder = Point.measurement(measurement);
        builder.tag(tags);
        builder.fields(fields);
        if (0 != time) {
            builder.time(time, timeUnit);
        }
        influxDB.write(influxBean.database, influxBean.retentionPolicyStr, builder.build());
    }

    /**
     * 批量写入测点
     *
     * @param batchPoints
     */
    public void batchInsert(BatchPoints batchPoints) {
        influxDB.write(batchPoints);
        // influxDB.enableGzip();
        // influxDB.enableBatch(2000,100,TimeUnit.MILLISECONDS);
        // influxDB.disableGzip();
        // influxDB.disableBatch();
    }

    /**
     * 批量写入数据
     *
     * @param database        数据库
     * @param retentionPolicy 保存策略
     * @param consistency     一致性
     * @param records         要保存的数据（调用BatchPoints.lineProtocol()可得到一条record）
     */
    public void batchInsert(final String database, final String retentionPolicy, final ConsistencyLevel consistency,
                            final List<String> records) {
        influxDB.write(database, retentionPolicy, consistency, records);
    }

    /**
     * 删除
     *
     * @param command 删除语句
     * @return 返回错误信息
     */
    public String deleteMeasurementData(String command) {
        QueryResult result = influxDB.query(new Query(command, influxBean.database));
        return result.getError();
    }

    /**
     * 关闭数据库
     */
    public void close() {
        influxDB.close();
    }

    /**
     * 构建Point
     *
     * @param measurement
     * @param time
     * @param fields
     * @return
     */
    public Point pointBuilder(String measurement, long time, Map<String, String> tags, Map<String, Object> fields) {
        Point point = Point.measurement(measurement).time(time, TimeUnit.MILLISECONDS).tag(tags).fields(fields).build();
        return point;
    }

}



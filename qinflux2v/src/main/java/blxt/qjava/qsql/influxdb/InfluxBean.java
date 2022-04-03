package blxt.qjava.qsql.influxdb;

import blxt.qjava.autovalue.inter.PropertySource;
import blxt.qjava.autovalue.inter.Value;

import java.lang.annotation.RetentionPolicy;

/**
 * 一个influxdb的连接参数
 * @auth 张家磊
 */
@PropertySource(ignoreResourceNotFound=true)
public class InfluxBean {

    /**
     * 连接地址 http://192.168.0.118:8086
     */
    @Value("influx.url")
    public String openurl;

    @Value("influx.auth.token")
    public String token;


    /**
     * 用户名
     */
    @Value("influx.auth.username")
    public String username;

    /**
     * 密码
     */
    @Value("influx.auth.pwd")
    public String password;

    @Value("influx.database")
    public String database;

    /**
     * 保留策略名
     */
    @Value("influx.replication")
    public String retentionPolicyStr ="default";

    @Value("influx.replication.time")
    public String retentionTimeStr ="0s";

    /**
     * 不存在是否创建
     */
    @Value("influx.isCreatDateBase")
    public boolean isCreatDateBase = false;

    @Value("influx.connect.timeout")
    public int connectTimeout;

   // RetentionPolicy retentionPolicy = ;

    public InfluxBean(){

    }

    public InfluxBean(String username, String password, String openurl) {
        this.username = username;
        this.password = password;
        this.openurl = openurl;
    }

    /**
     * 构造方法
     * @param username          用户名
     * @param password          密码
     * @param openurl           连接地址 http://192.168.0.118:8086
     * @param database          数据库名
     * @param retentionPolicyStr   保留策略名
     */
    public InfluxBean(String username, String password, String openurl,
                      String database, String retentionPolicyStr, boolean isCreatDateBase) {
        this.username = username;
        this.password = password;
        this.openurl = openurl;
        this.database = database;
        this.retentionPolicyStr = retentionPolicyStr;
        this.isCreatDateBase = isCreatDateBase;
    }

    public String getUsername() {
        return username;
    }

    public InfluxBean setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public InfluxBean setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getOpenurl() {
        return openurl;
    }

    public InfluxBean setOpenurl(String openurl) {
        this.openurl = openurl;
        return this;
    }

    public String getDatabase() {
        return database;
    }

    public InfluxBean setDatabase(String database) {
        this.database = database;
        return this;
    }

    public String getRetentionPolicyStr() {
        return retentionPolicyStr;
    }

    public InfluxBean setRetentionPolicyStr(String retentionPolicyStr) {
        this.retentionPolicyStr = retentionPolicyStr;
        return this;
    }

    public boolean isCreatDateBase() {
        return isCreatDateBase;
    }

    public InfluxBean setCreatDateBase(boolean creatDateBase) {
        isCreatDateBase = creatDateBase;
        return this;
    }

    public String getRetentionTimeStr() {
        return retentionTimeStr;
    }

    public InfluxBean setRetentionTimeStr(String retentionTimeStr) {
        this.retentionTimeStr = retentionTimeStr;
        return this;
    }

    @Override
    public InfluxBean clone() {
        return  new InfluxBean(username, password, openurl, database, retentionPolicyStr, isCreatDateBase)
                .setRetentionTimeStr(retentionTimeStr);
    }
}

package blxt.qjava.qsql.postgresql;

import blxt.qjava.qsql.utils.Toos;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.io.File;
import java.sql.SQLException;
import java.util.Properties;

/**
 * druid连接池
 * @Author: Zhang.Jialei
 * @Date: 2020/9/10 9:45
 */
public class DBPoolConnection {

    private static DBPoolConnection dbPoolConnection = null;
    private static DruidDataSource druidDataSource = null;

    public static synchronized boolean newInstance(File file){
        if (druidDataSource != null){
            return true;
        }
        System.out.println("druid连接池配置:" + file.getAbsolutePath());
        Properties properties = Toos.loadPropertiesFile(file);
        if(properties == null){
            return false;
        }
        try {
            // DruidDataSrouce工厂模式
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            System.err.println("druid连接池配置文件错误:" + file.getPath());
            e.printStackTrace();
            return false;
        }
        System.out.println("druid连接池配置成功");
        return true;
    }

    /**
     * 数据库连接池单例
     * @return
     */
    public static synchronized DBPoolConnection getInstance(){
        if (null == dbPoolConnection){
            dbPoolConnection = new DBPoolConnection();
        }
        return dbPoolConnection;
    }

    /**
     * 返回druid数据库连接
     * @return
     * @throws SQLException
     */
    public synchronized DruidPooledConnection getConnection() throws SQLException{
        return druidDataSource.getConnection();
    }


}
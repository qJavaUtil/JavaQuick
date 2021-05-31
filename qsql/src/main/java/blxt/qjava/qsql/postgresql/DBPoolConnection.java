package blxt.qjava.qsql.postgresql;

import blxt.qjava.qsql.utils.Toos;
import blxt.qjava.utils.check.CheckUtils;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * druid连接池
 * @Author: Zhang.Jialei
 * @Date: 2020/9/10 9:45
 */
public class DBPoolConnection {

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
     * 返回druid数据库连接
     * @return
     * @throws SQLException
     */
    public static synchronized DruidPooledConnection getConnection() throws SQLException{
        CheckUtils.objectCheckNull(druidDataSource, "Druid未初始化", -1, "" );
        return druidDataSource.getConnection();
    }

    public static synchronized void closeConnection(DruidPooledConnection connection) throws SQLException {
        if(connection != null){
            connection.close();
        }
    }

    public static boolean execute(DruidPooledConnection connection, String sql){
        CheckUtils.objectCheckNull(connection, "DruidPooledConnection不能为空", -1, "");
        try {
            // 执行查询
            PreparedStatement pst = connection.prepareStatement(sql);
            return pst.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static ResultSet executeQuery(DruidPooledConnection connection, String sql){
        // 连接
        CheckUtils.objectCheckNull(connection, "DruidPooledConnection不能为空", -1, "");
        try {
            // 执行查询
            PreparedStatement pst = connection.prepareStatement(sql);
            return pst.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static int executeUpdate(DruidPooledConnection connection, String sql){
        CheckUtils.objectCheckNull(connection, "DruidPooledConnection不能为空", -1, "");
        try {
            // 执行查询
            PreparedStatement pst = connection.prepareStatement(sql);
            return pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }


}
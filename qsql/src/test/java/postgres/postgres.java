package postgres;

import blxt.qjava.qsql.utils.ResultSetMapper;
import com.alibaba.druid.pool.DruidPooledConnection;
import blxt.qjava.qsql.postgresql.DBPoolConnection;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/10 8:43
 */
public class postgres {


    public static void main(String[] args) throws Exception {

        DBPoolConnection.newInstance(new File("E:\\ZhangJieLei\\Documents\\workspace\\workJava\\JavaQuick\\JavaQuick\\qsql\\src\\test\\db_server.properties"));
        DruidPooledConnection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection =  DBPoolConnection.getConnection();
            connection.setAutoCommit(false);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        String sql = "select * from \"mqtt\".\"userbase\"";
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            ResultSetMapper<User> resultSetMapper = new ResultSetMapper();
            List<User> userList = resultSetMapper.toObject(rs, User.class);

            for(User u :userList){
                System.out.println("结果" + u.toString());
            }


            pst.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

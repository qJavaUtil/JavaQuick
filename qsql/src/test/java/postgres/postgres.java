package postgres;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.qjava.qsql.postgresql.DBPoolConnection;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/10 8:43
 */
public class postgres {

    @Test
    void test()
    {
        DBPoolConnection dbPoolConnection = DBPoolConnection.getInstance();
        DruidPooledConnection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection =  dbPoolConnection.
                    getConnection();
            connection.setAutoCommit(false);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        String sql = "select * from mqtt.ut_device_product where user_key = 'user2'";
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while(rs.next()){

                System.out.print(rs.getString(1));
                System.out.print(rs.getString(2));
                System.out.print(rs.getString(3));
                System.out.print(rs.getString(4));
                System.out.print(rs.getString(5));
                System.out.print(rs.getString(6));
                System.out.println(rs.getString(7));
            }

            pst.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

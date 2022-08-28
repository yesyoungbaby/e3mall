package cn.e3.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/10/14 20:54
 */
public class JdbcTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/e3?characterEncoding=utf8&serverTimeZone=UTC",
                "root","Aa123456");
        String sql = "insert into tb_user(id,username) values(?,?)";
        PreparedStatement statement = connection.prepareCall(sql);
        statement.setInt(1,4);
        statement.setString(2,"小明");
        int i = statement.executeUpdate();
        System.out.println(i);
        statement.close();
        connection.close();

    }
}

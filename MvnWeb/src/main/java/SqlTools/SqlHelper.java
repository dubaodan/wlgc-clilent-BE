package SqlTools;

import java.sql.*;

public class SqlHelper {
    //驱动
    private String drive; //= "com.mysql.jdbc.Driver";
    //url
    private String url;//= "jdbc:mysql://localhost:3306/test1?characterEncoding=utf8";
    // 用户，密码
    private String user;// = "root";
    private String password ;//= "123123123";
    //连接数据库
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet result = null;

    public SqlHelper(String drive,String url,String user,String password) throws ClassNotFoundException {
        this.drive = drive;
        this.url = url;
        this.user = user;
        this.password = password;
        Class.forName(this.drive);
    }
    public SqlHelper(){
        drive="com.mysql.cj.jdbc.Driver";
        url="jdbc:mysql://localhost:3306/wlgc?characterEncoding=UTF-8&serverTimezone=UTC";
        user="root";
        password="1234";
    }
    public void ConnectData()  {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void CloseConnect() throws SQLException {
        //System.out.println(conn);
        result.close();//数据库先开后关
        statement.close();
        connection.close();//关闭数据库
    }


    public ResultSet GetResultSet(String sql) throws SQLException {
        statement = connection.createStatement();
        result = statement.executeQuery(sql);
        if (result != null)
            return result;
        else {
            System.out.println("数据集为空");
            return null;
        }
    }
    public int GetResultSetOfUpdate(String sql) throws SQLException {
        statement = connection.createStatement();
        int affectedRows = statement.executeUpdate(sql);
        return affectedRows;
    }

    public boolean DeleteCart(int userId) {
        try {
            String sql_str = "Delete From shopingcart where cu_Id = " + userId + "";
            System.out.println(sql_str);
            statement = connection.createStatement();
            statement.executeUpdate(sql_str);
            System.out.println(userId+ ":已删除");
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}

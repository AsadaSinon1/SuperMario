package src.code;

import java.sql. * ;
public class SQLConnection {
    //根据用户名在数据库中查找用户的得分情况
    public static int Search(String userName) {
        // 驱动程序名
        String driver = "com.mysql.cj.jdbc.Driver";

        // URL指向要访问的数据库名
        String url = "jdbc:mysql://127.0.0.1:3306/db_for_mario";

        // MySQL的用户名
        String user = "root";

        // MySQL的密码
        String password = "mysqlroot";
        int score = 0;
        try {
            // 加载驱动程序
            Class.forName(driver);

            // 连接数据库
            Connection conn = DriverManager.getConnection(url, user, password);
            if (!conn.isClosed()){
                System.out.println("Successful Connection!");
            }

            // statement用来执行SQL语句
            Statement statement = conn.createStatement();

            // 要执行的SQL语句
            String sql = "SELECT score FROM mario_users WHERE name = '"+ userName + "'";

            // 执行语句，得到结果集
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                score = rs.getInt("score");
                System.out.println(userName +"'s score is " + score);
            }

            //关闭
            rs.close();
            statement.close();
            conn.close();
        } catch(ClassNotFoundException e) {
            System.out.println("Sorry,can't find the Driver!");
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return score;
    }
    public static void Update(String userName,int newScore,boolean choice){
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/db_for_mario";

        String user = "root";
        String password = "mysqlroot";
        try{
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, user, password);
            if (!conn.isClosed()){
                System.out.println("Successful Connection!");
            }

            // statement用来执行SQL语句
            Statement statement = conn.createStatement();

            // 要执行的SQL语句
            String sql1 = "INSERT INTO mario_users (name,score)" + "VALUES ('"+ userName +"',0)";

            String sql2 = "UPDATE mario_users SET score = "+ newScore +" WHERE name = '"+ userName + "'";

            //true插入新的用户数据
            if(choice){
                statement.executeUpdate(sql1);
            }
            else{
                //false更新已有的用户数据
                 statement.executeUpdate(sql2);
            }

            //关闭连接和声明
            statement.close();
            conn.close();
        } catch(ClassNotFoundException e) {
            System.out.println("Sorry,can't find the Driver!");
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

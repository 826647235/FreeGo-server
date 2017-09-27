import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectSQL {
    private static Connection connection=null;

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String user="root";
            String password="wcvr123";
            String url="jdbc:mysql://localhost:3306/login?useSSL=false";
            connection= DriverManager.getConnection(url,user,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}

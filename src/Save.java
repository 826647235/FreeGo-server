import java.sql.Connection;
import java.sql.PreparedStatement;

public class Save {

    public static boolean saveUser(Users user) {
        Connection connection=null;
        try {
            connection = ConnectSQL.getConnection();
            String sql = "insert into Users(Account,Password) values(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getAccount());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

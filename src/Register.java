import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "Register")
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String account = request.getParameter("Account");
        String password = request.getParameter("Password");
        String name = request.getParameter("Name");
        OutputStreamWriter out=new OutputStreamWriter(response.getOutputStream());
        if(!checkRepeat(account)){
            out.write("false");
        } else {
            Users user=new Users();
            user.setAccount(account);
            user.setPassword(password);
            user.setName(name);
            saveUser(user);
            out.write("true");
        }
        out.flush();
        out.close();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    private boolean checkRepeat(String account) {
        try {
            Connection connection = ConnectSQL.getConnection();
            String sql = "select Account FROM member WHERE Account=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,account);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveUser(Users user) {
        Connection connection=null;
        try {
            connection = ConnectSQL.getConnection();
            String sql = "insert into member(Account,Password,Name) values(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getAccount());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.setString(3,user.getName());
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

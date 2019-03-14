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
//检测账号是否重复
@WebServlet(name = "CheckRepeat")
public class CheckRepeat extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String account = request.getParameter("Account");
        OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream());
        try {
            Connection connection = ConnectSQL.getConnection();
            String sql = "select * FROM member WHERE Account = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                out.write("true");
            } else {
                out.write("false");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
//加好友，存数据库
@WebServlet(name = "AddFriend")
public class AddFriend extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String name1 = request.getParameter("Name1");
        String name2 = request.getParameter("Name2");
        OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream());
        try {
            Connection connection = ConnectSQL.getConnection();
            String SQL = "insert into friend values(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,name1);
            preparedStatement.setString(2,name2);
            preparedStatement.executeUpdate();
            out.write("true");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.write("false");
        }
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
//向数据库中储存收藏的动态
@WebServlet(name = "Collection")
public class Collection extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String name = request.getParameter("Name");
        String Id = request.getParameter("Id");
        OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream());
        try {
            Connection connection = ConnectSQL.getConnection();
            String SQL = "insert into collection values (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,name);
                preparedStatement.setInt(2,Integer.parseInt(Id));
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

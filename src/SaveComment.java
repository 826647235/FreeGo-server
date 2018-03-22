import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet(name = "SaveComment")
public class SaveComment extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;cha  rset=utf-8");
        String Id = request.getParameter("Id");
        String name = request.getParameter("Name");
        String date = request.getParameter("Date");
        String content = request.getParameter("Content");
        try {
            Connection connection = ConnectSQL.getConnection();
            String SQL = "insert into comment(CommunityId, Name, Date, Content, LikeNum) values(?, ?, ?, ?, 0)";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1,Integer.parseInt(Id));
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,date);
            preparedStatement.setString(4,content);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

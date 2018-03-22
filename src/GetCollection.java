import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetCollection")
public class GetCollection extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String state = request.getParameter("State");
        String position = request.getParameter("Position");
        String name = request.getParameter("Name");
        PrintWriter out = response.getWriter();
        String SQL = null;
        try {
            Connection connection = ConnectSQL.getConnection();
            if (state.equals("myOwn")) {
                SQL = "select * from community where ID < ? order by ID DESC ";
            } else if (state.equals("myCollection")) {
                SQL = "select * from community " +
                        "where ID < ? and ID in " +
                        "(SELECT msgId from collection where Name = ?)" +
                        "  order by ID DESC";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1,Integer.parseInt(position));
            preparedStatement.setString(2,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> messageList = new ArrayList<>();
            int count = 1;
            while (resultSet.next()) {
                String message;
                message = resultSet.getInt("ID") + "##,##" +
                        resultSet.getString("Name") + "##,##" +
                        resultSet.getString("Date") + "##,##" +
                        resultSet.getString("Content") + "##,##" +
                        resultSet.getInt("LikeNum") + "##,##" +
                        resultSet.getInt("PictureNum") + "@@@";
                messageList.add(message);
                if (count == 8) {
                    break;
                }
                count++;
            }
            out.println(messageList);
            out.flush();
            out.close();
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

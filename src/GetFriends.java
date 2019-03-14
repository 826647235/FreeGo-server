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
//得到好友
@WebServlet(name = "GetFriends")
public class GetFriends extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String message = request.getParameter("Name");
        List<String> messageList = new ArrayList<>();
        PrintWriter out = response.getWriter();
        try {
            Connection collection = ConnectSQL.getConnection();
            String SQL = "select * from member where Name in" +
                    "(select Name2 from friend where Name1 = ?)";
            PreparedStatement preparedStatement = collection.prepareStatement(SQL);
            preparedStatement.setString(1, message);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String responseText;
                responseText = resultSet.getString("Account") + "##,##" +
                        resultSet.getString("Name") + "##,##" +
                        resultSet.getInt("Age") + "##,##" +
                        resultSet.getString("Sex") + "##,##" +
                        resultSet.getString("Motto") + "@@@";
                messageList.add(responseText);
            }
            out.println(messageList);
            collection.close();
            out.flush();
            out.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

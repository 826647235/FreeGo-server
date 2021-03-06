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
//得到动态，一时间顺序降序排列
@WebServlet(name = "GetCommunity")
public class GetCommunity extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String state = request.getParameter("State");
        String position = request.getParameter("Position");
        String userName = request.getParameter("Name");
        PrintWriter out = response.getWriter();
        String SQL = null;
        try {
            Connection connection = ConnectSQL.getConnection();
            if (state.equals("new")) {
                SQL = "select * from community where ID > ? order by ID DESC ";
            } else if (state.equals("old")) {
                SQL = "select * from community where ID < ? order by ID DESC";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1,Integer.parseInt(position));
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
                        resultSet.getInt("PictureNum");
                Connection smallConnection = ConnectSQL.getConnection();
                String smallSQL = "select * from collection where Name = ? and msgId = ?";
                PreparedStatement smallPreparedStatement = smallConnection.prepareStatement(smallSQL);
                smallPreparedStatement.setString(1,userName);
                smallPreparedStatement.setInt(2,resultSet.getInt("ID"));
                ResultSet smallResultSet = smallPreparedStatement.executeQuery();
                if(smallResultSet.next()) {
                    message += "##,##true";
                } else {
                    message += "##,##false";
                }
                smallSQL = "select * from islike where Name = ? and msgId = ?";
                smallPreparedStatement = smallConnection.prepareStatement(smallSQL);
                smallPreparedStatement.setString(1,userName);
                smallPreparedStatement.setInt(2,resultSet.getInt("ID"));
                smallResultSet = smallPreparedStatement.executeQuery();
                if(smallResultSet.next()) {
                    message += "##,##true@@@";
                } else {
                    message += "##,##false@@@";
                }
                messageList.add(message);
                if (count == 5) {
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

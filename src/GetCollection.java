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
//得到收藏的动态，以时间顺序降序排列
@WebServlet(name = "GetCollection")
public class GetCollection extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String state = request.getParameter("State");
        String position = request.getParameter("Position");
        String name = request.getParameter("Name");
        PrintWriter out = response.getWriter();
        String SQL;
        try {
            Connection connection = ConnectSQL.getConnection();
            PreparedStatement preparedStatement = null;
            if (state.equals("myOwn")) {
                if(Integer.parseInt(position) != 0) {
                    SQL = "select * from community where ID < ? and Name = ? order by ID DESC ";
                    preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setInt(1,Integer.parseInt(position));
                    preparedStatement.setString(2,name);
                } else {
                    SQL = "select * from community where Name = ? order by ID DESC ";
                    preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setString(1,name);
                }
            } else if (state.equals("myCollection")) {
                if(Integer.parseInt(position) != 0){
                    SQL = "select * from community " +
                            "where ID < ? and ID in " +
                            "(SELECT msgId from collection where Name = ?)" +
                            "  order by ID DESC";
                    preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setInt(1,Integer.parseInt(position));
                    preparedStatement.setString(2,name);
                }  else {
                    SQL = "select * from community " +
                            "where ID in " +
                            "(SELECT msgId from collection where Name = ?)" +
                            "  order by ID DESC";
                    preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setString(1,name);
                }
            }
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
                smallPreparedStatement.setString(1,name);
                smallPreparedStatement.setInt(2,resultSet.getInt("ID"));
                ResultSet smallResultSet = smallPreparedStatement.executeQuery();
                if(smallResultSet.next()) {
                    message += "##,##true";
                } else {
                    message += "##,##false";
                }
                smallSQL = "select * from islike where Name = ? and msgId = ?";
                smallPreparedStatement = smallConnection.prepareStatement(smallSQL);
                smallPreparedStatement.setString(1,name);
                smallPreparedStatement.setInt(2,resultSet.getInt("ID"));
                smallResultSet = smallPreparedStatement.executeQuery();
                if(smallResultSet.next()) {
                    message += "##,##true@@@";
                } else {
                    message += "##,##false@@@";
                }
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
//得到动态的评论，以时间顺序排序
public class GetComment extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String id = request.getParameter("Id");
        String state = request.getParameter("State");
        String position = request.getParameter("Position");
        PrintWriter out = response.getWriter();
        try {
            Connection connection = ConnectSQL.getConnection();
            String SQL;
            if(state.equals("new")) {
                SQL = "select * from comment where CommunityId = ? and CommentId > ? order by CommentId DESC";
            } else {
                SQL = "select * from comment where CommunityId = ? and CommentId < ? order by CommentId DESC";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1,Integer.parseInt(id));
            preparedStatement.setInt(2,Integer.parseInt(position));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> messageList = new ArrayList<>();
            int Count = 1;
            while (resultSet.next()) {
                String message;
                message = resultSet.getInt("CommentId") + "##,##" +
                        resultSet.getString("Name") + "##,##" +
                        resultSet.getString("Date") + "##,##" +
                        resultSet.getString("Content") + "##,##" +
                        resultSet.getInt("LikeNum") + "@@@";
                messageList.add(message);
                if(Count == 10){
                    break;
                }
            }
            out.println(messageList);
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

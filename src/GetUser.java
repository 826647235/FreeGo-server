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
//得到用户信息
@WebServlet(name = "GetUser")
public class GetUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String userName = request.getParameter("UserName");
        String name = request.getParameter("Name");
        PrintWriter out = response.getWriter();
        try {
            Connection collection = ConnectSQL.getConnection();
            String SQL = "select * from member where Name = ?";
            PreparedStatement preparedStatement = collection.prepareStatement(SQL);
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            String message = null;
            if(resultSet.next()) {
                message = resultSet.getInt("Age") + "##,##" +
                        resultSet.getString("Sex") + "##,##" +
                        resultSet.getString("Motto") + "##,##" +
                        resultSet.getInt("PhotoNum") + "##,##";
            }
            SQL = "select * from friend where name1 = ? and name2 = ?";
            preparedStatement = collection.prepareStatement(SQL);
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,name);
            ResultSet resultSet1 = preparedStatement.executeQuery();
            if(resultSet1.next()) {
                message += "false";
            } else {
                message += "true";
            }
            out.write(message);
            collection.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

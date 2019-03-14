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
//改变Accompany状态，在near和Accompany中改变
@WebServlet(name = "ChangeAccompany")
public class ChangeAccompany extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String name = request.getParameter("Name");
        PrintWriter out = response.getWriter();
        try {
            Connection connection = ConnectSQL.getConnection();
            String SQL = "select state from Location where name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                if(resultSet.getString("state").equals("ACCOMPANY")) {
                    String newSQL = "update Location set state = \"NEAR\" where name = ?";
                    preparedStatement = connection.prepareStatement(newSQL);
                    preparedStatement.setString(1,name);
                    preparedStatement.executeUpdate();
                    out.write("close");
                } else if(resultSet.getString("state").equals("NEAR")){
                    String newSQL = "update Location set state = \"ACCOMPANY\" where name = ?";
                    preparedStatement = connection.prepareStatement(newSQL);
                    preparedStatement.setString(1,name);
                    preparedStatement.executeUpdate();
                    out.write("open");
                }
            }
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

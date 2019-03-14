import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
//保存用户经纬度位置
@WebServlet(name = "SaveLocation")
public class SaveLocation extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String name = request.getParameter("Name");
        String longitude = request.getParameter("Longitude");
        String latitude = request.getParameter("Latitude");
        try {
            Connection connection = ConnectSQL.getConnection();
            String SQL = "Update Location set Longitude = ?, Latitude = ? where Name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setDouble(1,Double.parseDouble(longitude));
            preparedStatement.setDouble(2,Double.parseDouble(latitude));
            preparedStatement.setString(3,name);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

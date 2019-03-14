import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
//保存电话号码
@WebServlet(name = "SavePhoneNum")
public class SavePhoneNum extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String phoneNum = request.getParameter("PhoneNum");
        String name = request.getParameter("Name");
        PrintWriter out = response.getWriter();
        try {
            Connection connection = ConnectSQL.getConnection();
            String SQL = "Update Location set phone = ? where name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,phoneNum);
            preparedStatement.setString(2,name);
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

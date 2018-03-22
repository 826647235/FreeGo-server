import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "GetPortrait")
public class GetPortrait extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String name = request.getParameter("name");
        String portraitPath;
        try {
            Connection connection = ConnectSQL.getConnection();
            String SQL = "SELECT Portrait from member where Name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                portraitPath = resultSet.getString("Portrait");
            } else {
                portraitPath = "C:\\picture\\portrait\\default_portrait.jpg";
            }
            FileInputStream fileInputStream = new FileInputStream(portraitPath);
            int size =fileInputStream.available();
            byte data[]=new byte[size];
            fileInputStream.read(data);
            fileInputStream.close();
            response.setConten tType("image/jpg");
            OutputStream os = response.getOutputStream();
            os.write(data);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

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

@WebServlet(name = "GetPicture")
public class GetPicture extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String id = request.getParameter("Id");
        String count = request.getParameter("Count");
        String path = null;
        try {
            Connection connection = ConnectSQL.getConnection();
            String SQL = "select Img from picture where ID = ? and Count = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1,Integer.parseInt(id));
            preparedStatement.setInt(2,Integer.parseInt(count));
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                path = resultSet.getString("Img");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileInputStream fileInputStream = new FileInputStream(path);
        int size =fileInputStream.available();
        byte data[]=new byte[size];
        fileInputStream.read(data);
        fileInputStream.close();
        response.setContentType("image/jpg");
        OutputStream os = response.getOutputStream();
        os.write(data);
        os.flush();
        os.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("11111");
        FileInputStream fileInputStream = new FileInputStream("C:\\test\\IMG_20180303_124201.jpg");
        int size =fileInputStream.available();
        byte data[]=new byte[size];
        fileInputStream.read(data);
        fileInputStream.close();
        response.setContentType("image/jpg");
        OutputStream os = response.getOutputStream();
        System.out.println("已发送");
        os.write(data);
        os.flush();
        os.close();
    }
}

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//注册
@WebServlet(name = "Register")
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String account = request.getParameter("Account");
        String password = request.getParameter("Password");
        String name = request.getParameter("Name");
        String age = request.getParameter("Age");
        String sex = request.getParameter("Sex");
        String motto = request.getParameter("Motto");
        OutputStreamWriter out=new OutputStreamWriter(response.getOutputStream());
        if(!checkRepeat(name)){
            out.write("repeat");
        } else {
            try {
                Connection connection = ConnectSQL.getConnection();
                String sql = "insert into member(Account,Password,Portrait,Name,Age,Sex,Motto,PhotoNum) values(?,?,?,?,?,?,?,0)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,account);
                preparedStatement.setString(2,password);
                if(sex.equals("男")) {
                    preparedStatement.setString(3,"C:\\picture\\portrait\\default_portrait_man.png");
                } else {
                    preparedStatement.setString(3,"C:\\picture\\portrait\\default_portrait_woman.png");
                }
                preparedStatement.setString(4,name);
                preparedStatement.setInt(5,Integer.parseInt(age));
                preparedStatement.setString(6,sex);
                preparedStatement.setString(7,motto);
                preparedStatement.executeUpdate();

                sql = "insert into Location(Name) values(?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,name);
                preparedStatement.executeUpdate();
                connection.close();
                out.write("true");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        out.flush();
        out.close();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public boolean checkRepeat(String name) {
        try {
            Connection connection = ConnectSQL.getConnection();
            String sql = "select * FROM member WHERE NAME = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                connection.close();
                return true;
            } else {
                 connection.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

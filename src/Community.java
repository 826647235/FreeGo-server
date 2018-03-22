import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "Community")
public class Community extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String name = request.getParameter("Name");
        String content = request.getParameter("Content");
        String date = request.getParameter("Date");
        String pictureNum = request.getParameter("PictureNum");
        PrintWriter out = response.getWriter();
        try {
            Connection connection = ConnectSQL.getConnection();
            String SQL = "insert into community(Name,Date,Content,Likenum,PictureNum) VALUES (?,?,?,0,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,content);
            preparedStatement.setString(3,date);
            preparedStatement.setInt(4,Integer.parseInt(pictureNum));
            preparedStatement.executeUpdate();
            SQL = "select ID form community where Name = ? and Date = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,date);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                out.print(resultSet.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}

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
//得到附近的人信息
@WebServlet(name = "GetNearPeople")
public class GetNearPeople extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String name = request.getParameter("Name");
        String state = request.getParameter("State");
        double longitude = 0;
        double latitude = 0;
        List<String> messageList = new ArrayList<>();
        PrintWriter out = response.getWriter();
        try {
            Connection connection = ConnectSQL.getConnection();
            String SQL = "SELECT Longitude,Latitude from Location where name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                longitude = resultSet.getDouble(1);
                latitude = resultSet.getDouble(2);
            }
            if(state.equals("NEAR")) {
                SQL = "Select name,Longitude,Latitude,Phone from Location where Longitude > ? and Longitude < ? and Latitude > ? and Latitude < ? and name != ?";
            } else {
                SQL = "Select name,Longitude,Latitude,Phone from Location where Longitude > ? and Longitude < ? and Latitude > ? and Latitude < ? and state = \"ACCOMPANY\" and name != ?";
            }
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setDouble(1,longitude - 0.05);
            preparedStatement.setDouble(2,longitude + 0.05);
            preparedStatement.setDouble(3,latitude - 0.05);
            preparedStatement.setDouble(4,latitude + 0.05);
            preparedStatement.setString(5,name);
            ResultSet peopleName = preparedStatement.executeQuery();
            while(peopleName.next()) {
                double lon = peopleName.getDouble("Longitude");
                double lat = peopleName.getDouble("Latitude");
                String phoneNum = peopleName.getString("Phone");
                double distance = getDistance(latitude,longitude,lat,lon);
                Connection smallConnection = ConnectSQL.getConnection();
                String smallSQL = "Select * from member where name = ?";
                PreparedStatement smallStatement = smallConnection.prepareStatement(smallSQL);
                smallStatement.setString(1,peopleName.getString(1));
                ResultSet smallSet = smallStatement.executeQuery();
                String message;
                if(smallSet.next()) {
                    message =  smallSet.getString("Account") + "##,##" +
                            smallSet.getString("Name") + "##,##" +
                            smallSet.getInt("Age") + "##,##" +
                            smallSet.getString("Sex") + "##,##" +
                            smallSet.getString("Motto") + "##,##" +
                            phoneNum + "##,##" + distance + "@@@";
                    messageList.add(message);
                }
            }
            out.println(messageList);
            connection.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    public double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = getRadian(lat1);
        double radLat2 = getRadian(lat2);
        double a = radLat1 - radLat2;// 两点纬度差
        double b = getRadian(lng1) - getRadian(lng2);// 两点的经度差
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137;
        return s * 1000;
    }

    private double getRadian(double degree) {
        return degree * Math.PI / 180.0;
    }
}

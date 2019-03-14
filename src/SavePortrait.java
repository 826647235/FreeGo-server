import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
//保存头像
@WebServlet(name = "SavePortrait")
public class SavePortrait extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream());
        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            try {
                Connection connection = ConnectSQL.getConnection();
                PreparedStatement preparedStatement;
                String SQL;
                String name;
                String userName;
                List<FileItem> items = upload.parseRequest(request);
                for (FileItem fileItem : items) {
                    int position = 0;
                    name = fileItem.getName();
                    String[] message = name.split("_");
                    userName = message[0];
                    File file = new File("C:\\picture\\portrait", name);
                    fileItem.write(file);
                    SQL = "update member set Portrait = ? where name = ?";
                    preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setString(1,file.getPath());
                    preparedStatement.setString(2,userName);
                    preparedStatement.executeUpdate();
                }
                connection.close();
                out.write("true");
            } catch (Exception e) {
                e.printStackTrace();
                out.write("false");
            }
            out.flush();
            out.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

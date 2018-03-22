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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

@WebServlet(name = "SavePicture")
public class SavePicture extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                try {
                    Connection connection = ConnectSQL.getConnection();
                    PreparedStatement preparedStatement = null;
                    String SQL;
                    String name;
                    String Id;
                    String Count;
                    List<FileItem> items = upload.parseRequest(request);
                    for (FileItem fileItem : items) {
                        name = fileItem.getName();
                        String[] message = name.split("_");
                        Id = message[0];
                        Count = message[1];
                        File file = new File("C:\\picture\\photo", name);
                        fileItem.write(file);
                        SQL = "insert into Picture values (?, ?, ?)";
                        preparedStatement = connection.prepareStatement(SQL);
                        preparedStatement.setInt(1,Integer.parseInt(Id));
                        preparedStatement.setInt(2,Integer.parseInt(Count));
                        preparedStatement.setString(3,file.getPath());
                        System.out.println("       上传的文件名是:" + file.getName());
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

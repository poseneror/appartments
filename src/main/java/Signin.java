import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
/**
 * Created by Or on 03/08/2017.
 */
public class Signin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DataConnection dc = new DataConnection();
        String json = "";
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            int user_id = dc.authenticateSeller(email,password);
            if(user_id != -1){
                json = "{auth: true, token: " + user_id + "}";
            } else {
                json = "{auth: false}";
            }
        }
        response.setContentType("application/json");
        response.getWriter().print(json);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

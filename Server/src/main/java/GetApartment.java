import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Or on 30/07/2017.
 */
public class GetApartment extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Apartment ap = null;
        DataConnection dc = new DataConnection();
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost){
            // get apartments closest to location:
            int id = Integer.parseInt(request.getParameter("id"));
            ap = new Apartment(id);
        }
        response.setContentType("application/json");
        String json = new Gson().toJson(ap);
        response.getWriter().print(json);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }
}

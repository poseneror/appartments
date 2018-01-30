import com.google.gson.Gson;
import com.newrelic.agent.deps.com.google.gson.JsonArray;
import com.newrelic.agent.deps.org.json.simple.JSONArray;
import com.newrelic.agent.deps.org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Or on 30/07/2017.
 */
public class GetNearby extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List apartments = new ArrayList();
        DataConnection dc = new DataConnection();
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost){
            // get apartments closest to location in radius:
            double radius = 100;
            double x = Double.parseDouble(request.getParameter("x"));
            double y = Double.parseDouble(request.getParameter("y"));
            apartments = dc.getNearestAppartments(x, y, radius);
        } else {
            apartments = dc.getAllAppartments();
        }
        response.setContentType("application/json");
        String json = new Gson().toJson(apartments);
        response.getWriter().print(json);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }
}

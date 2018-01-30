import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostApartment extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    DataConnection dc = new DataConnection();
    List apartments = new ArrayList();
    boolean isPost = "POST".equals(request.getMethod());
    if(isPost) {
      String appTitle = request.getParameter("title");
      double appX = Double.parseDouble(request.getParameter("x"));
      double appY = Double.parseDouble(request.getParameter("y"));
      int appPrice = Integer.parseInt(request.getParameter("price"));
      String appAddress = request.getParameter("address");
      String appDescription = request.getParameter("description");
      int appSeller = Integer.parseInt(request.getParameter("seller"));
      Apartment newAp = new Apartment(appTitle, appX, appY, appPrice, appAddress, appDescription, appSeller);
      dc.insert(newAp);
    }
    response.setContentType("text/html");
    String html = "Congratulations!";
    response.getWriter().print(html);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doGet(request,response);
  }
}

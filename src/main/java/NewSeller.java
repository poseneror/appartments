import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewSeller extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    DataConnection dc = new DataConnection();
    boolean isPost = "POST".equals(request.getMethod());
    if(isPost) {
      String name = request.getParameter("name");
      int contact = Integer.parseInt(request.getParameter("contact"));
      String email = request.getParameter("email");
      String password = request.getParameter("password");
      Seller newSeller = new Seller(name, contact, email, password);
      dc.insert(newSeller);
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

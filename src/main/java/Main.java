import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    DataConnection dc = new DataConnection();
    List apartments = new ArrayList();
    apartments = dc.getAllAppartments();
    response.setContentType("text/html");
    String html = "<html><body>" +
            "<p><h3>New Seller:</h3><form action='NewSeller' method='POST'>" +
            "name: <input type='text' name='name'>" +
            "contact: <input type='text' name='contact'>" +
            "email: <input type='text' name='email'>" +
            "password: <input type='password' name='password'>" +
            "<input type='submit' value='submit' />" +
            "</form></p>" +
            "<p><h3>Login Seller:</h3><form action='Signin' method='POST'>" +
            "email: <input type='text' name='email'>" +
            "password: <input type='password' name='password'>" +
            "<input type='submit' value='submit' />" +
            "<p><h3>Add new apartment:</h3><form action='PostApartment' method='POST'>" +
            "title: <input type='text' name='title'>" +
            "location: <input type='text' name='x'>,<input type='text' name='y'>" +
            "price: <input type='text' name='price'>" +
            "address: <input type='text' name='address'>" +
            "description: <input type='text' name='description'>" +
            "seller id: <input type='text' name='seller'>" +
            "<input type='submit' value='submit' />" +
            "</form></p>" +
            "<p><h3>Get apartments near point:</h3><form action='GetNearby' method='POST'>" +
            "location:<input type='text' name='x'>,<input type='text' name='y'>" +
            "<input type='submit' value='submit' />" +
            "</form></p>" +
            "<p><h3>Get Apartment By ID:</h3><form action='GetApartment' method='POST'>" +
            "id:<input type='text' name='id'>" +
            "<input type='submit' value='submit' />" +
            "</form></p><table>";
    for(Object ob : apartments){
      Apartment ap = (Apartment) ob;
      html += "<tr><td>" + ap.getTitle() + "</td><td>" + ap.getX() + "</td><td>" +
              ap.getY() + "</td><td>" + ap.getPrice() + "</td></tr>";
    }
    html += "</table></body></html>";
    response.getWriter().print(html);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doGet(request,response);
  }
}

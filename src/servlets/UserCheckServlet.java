package servlets;

import com.google.gson.Gson;
import dto.UserDTO;
import entites.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class UserCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        try (PrintWriter pw = resp.getWriter()) {
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("html/text");

            if (user == null) {
                pw.write("User is not authorized");
            } else {
                pw.write("User is authorized");
            }

            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

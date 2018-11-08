package servlets;

import com.google.gson.Gson;
import dao.UserDAO;
import dao.impl.UserDAOImpl;
import entites.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Base64;


public class UserRegister extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String avatar = req.getParameter("avatar");
        resp.setContentType("html/text");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter pw = resp.getWriter()) {
            UserDAO userDAO = UserDAOImpl.getInstance();
            User user = userDAO.get(email);

            if (user == null) {
                User user1 = userDAO.save(new User(name, email, avatar, password));
                pw.write("User has been created");
            } else {
                pw.write("Email already exists");
            }
            pw.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

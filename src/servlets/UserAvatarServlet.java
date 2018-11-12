package servlets;

import dao.UserDAO;
import dao.impl.UserDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "User Avatar Servlet", urlPatterns = "/api/user/avatar")
public class UserAvatarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userID = Long.parseLong(req.getParameter("userID"));
        resp.setContentType("html/text");
        resp.setCharacterEncoding("UTF-8");
        UserDAO userDAO = UserDAOImpl.getInstance();

        try (PrintWriter pw = resp.getWriter()) {
            String avatar = userDAO.getUserAvatar(userID);
            pw.write(avatar);
            pw.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

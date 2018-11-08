package servlets;

import dao.PostDAO;
import dao.impl.PostDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class PostLengthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PostDAO postDAO = PostDAOImpl.getInstance();
        resp.setContentType("html/text");

        try (PrintWriter pw = resp.getWriter()) {
            long numberOfPosts = postDAO.getNumberOfAllPosts();
            pw.write(String.valueOf(numberOfPosts));
            pw.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

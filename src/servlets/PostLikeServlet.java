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

public class PostLikeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userID = Long.parseLong(req.getParameter("userID"));
        long postID = Long.parseLong(req.getParameter("postID"));

        resp.setContentType("html/text");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter pw = resp.getWriter()) {
            PostDAO postDAO = PostDAOImpl.getInstance();
            postDAO.likePostByUser(postID, userID);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
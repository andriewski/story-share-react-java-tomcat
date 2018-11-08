package servlets;

import dao.CommentDAO;
import dao.impl.CommentDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class PostCommentsLengthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CommentDAO commentDAO = CommentDAOImpl.getInstance();
        long postID = Long.parseLong(req.getParameter("postID"));
        resp.setContentType("html/text");

        try (PrintWriter pw = resp.getWriter()) {
            long numberOfCommentsInThePost = commentDAO.getNumberOfCommentsInThePost(postID);
            pw.write(String.valueOf(numberOfCommentsInThePost));
            pw.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

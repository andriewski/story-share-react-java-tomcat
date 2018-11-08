package servlets;

import com.google.gson.Gson;
import dao.CommentDAO;
import dao.impl.CommentDAOImpl;
import dto.CommentDTO;
import entites.Comment;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class PostLeaveCommentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userID = Long.parseLong(req.getParameter("userID"));
        long postID = Long.parseLong(req.getParameter("postID"));
        String text = req.getParameter("text");
        CommentDAO commentDAO = CommentDAOImpl.getInstance();

        try (PrintWriter pw = resp.getWriter()){
            commentDAO.save(
                    new Comment(userID, postID, text, new Timestamp(new Date().getTime())));
            pw.write("Success");
            pw.flush();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

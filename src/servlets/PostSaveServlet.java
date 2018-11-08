package servlets;

import dao.PostDAO;
import dao.impl.PostDAOImpl;
import entites.Post;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class PostSaveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userID = Long.parseLong(req.getParameter("userID"));
        String picture = req.getParameter("picture");
        String text = req.getParameter("text");
        PostDAO postDAO = PostDAOImpl.getInstance();

        try (PrintWriter pw = resp.getWriter()) {
            postDAO.save(new Post(text, new Timestamp(new Date().getTime()),
                    userID, picture));
            pw.write("Success");
            pw.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

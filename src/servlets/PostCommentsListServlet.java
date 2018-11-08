package servlets;

import com.google.gson.Gson;
import dao.CommentDAO;
import dao.impl.CommentDAOImpl;
import dto.CommentDTO;
import entites.Pagination;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class PostCommentsListServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long postID = Long.parseLong(req.getParameter("postID"));
        long offset = Long.parseLong(req.getParameter("offset"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        Pagination pagination = new Pagination(offset, limit);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");


        try (PrintWriter pw = resp.getWriter()) {
            CommentDAO commentDAO = CommentDAOImpl.getInstance();
            List<CommentDTO> listOfComments = commentDAO.getCommentsInThePostWithOffsetAndLimit(postID, pagination);
            Gson gson = new Gson();
            String json = gson.toJson(listOfComments);
            pw.write(json);
            pw.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

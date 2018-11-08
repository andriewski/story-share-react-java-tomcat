package servlets;

import com.google.gson.Gson;
import dao.PostDAO;
import dao.impl.PostDAOImpl;
import dto.PostDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class PostSingleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userID = Long.parseLong(req.getParameter("userID"));
        long postID = Long.parseLong(req.getParameter("postID"));
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter pw = resp.getWriter()) {
            PostDAO postDAO = PostDAOImpl.getInstance();
            PostDTO postDTO = postDAO.getSinglePostDTO(userID, postID);
            Gson gson = new Gson();
            String json = gson.toJson(postDTO);
            pw.write(json);
            pw.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

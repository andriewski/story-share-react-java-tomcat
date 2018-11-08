package servlets;

import com.google.gson.Gson;
import dao.PostDAO;
import dao.impl.PostDAOImpl;
import dto.PostDTO;
import entites.Pagination;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class PostListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long offset = Long.parseLong(req.getParameter("offset"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        long userID = Long.parseLong(req.getParameter("userID"));

//Надо бы переделать, чтобы был отдельный запрос для незарегистрированного пользователя
        Pagination pagination = new Pagination(offset, limit);
        PostDAO postDAO = PostDAOImpl.getInstance();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter pw = resp.getWriter()) {
            List<PostDTO> listOfPosts = postDAO.getPostsWithPagination(pagination, userID);
            Gson gson = new Gson();
            String json = gson.toJson(listOfPosts);
            pw.write(json);
            pw.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

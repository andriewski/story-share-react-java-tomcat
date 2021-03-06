package web.command.impl;

import com.google.gson.Gson;
import dto.PostDTO;
import entites.Pagination;
import org.apache.log4j.Logger;
import services.PostService;
import services.ServiceException;
import services.impl.PostServiceImpl;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PostListController implements Controller {
    private PostService postService = PostServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(PostListController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long offset = Long.parseLong(req.getParameter("offset"));
            int limit = Integer.parseInt(req.getParameter("limit"));
            long userID = Long.parseLong(req.getParameter("userID"));

            if ("User is not authorized".equals(req.getHeader("authorization"))) {
                userID = -1;
            }

//Надо бы переделать, чтобы был отдельный запрос для незарегистрированного пользователя
            Pagination pagination = new Pagination(offset, limit);
            resp.setContentType("application/json");

            try (PrintWriter pw = resp.getWriter()) {
                List<PostDTO> listOfPosts = postService.getPostsWithPagination(pagination, userID);
                Gson gson = new Gson();
                String json = gson.toJson(listOfPosts);
                pw.write(json);
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

package web.command.impl;

import com.google.gson.Gson;
import dto.PostDTO;
import org.apache.log4j.Logger;
import services.PostService;
import services.ServiceException;
import services.impl.PostServiceImpl;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PostSingleController implements Controller {
    private PostService postService = PostServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(PostSingleController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long userID = Long.parseLong(req.getParameter("userID"));
            long postID = Long.parseLong(req.getParameter("postID"));
            resp.setContentType("application/json");

            try (PrintWriter pw = resp.getWriter()) {
                PostDTO postDTO = postService.getSinglePostDTO(userID, postID);
                Gson gson = new Gson();
                String json = gson.toJson(postDTO);
                pw.write(json);
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

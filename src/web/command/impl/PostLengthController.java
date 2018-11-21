package web.command.impl;

import org.apache.log4j.Logger;
import services.PostService;
import services.ServiceException;
import services.impl.PostServiceImpl;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PostLengthController implements Controller {
    private PostService postService = PostServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(PostLengthController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("html/text");

            try (PrintWriter pw = resp.getWriter()) {
                long numberOfPosts = postService.getNumberOfAllPosts();
                pw.write(String.valueOf(numberOfPosts));
            }
        } catch (IOException | ServiceException e) {
            logger.error(e);
        }
    }
}

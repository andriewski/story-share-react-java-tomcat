package web.command.impl;

import entites.Post;
import org.apache.log4j.Logger;
import services.PostService;
import services.ServiceException;
import services.impl.PostServiceImpl;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

public class CreatePostController implements Controller {
    private PostService postService = PostServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(CreatePostController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long userID = Long.parseLong(req.getParameter("userID"));
            String picture = req.getParameter("picture");
            String text = req.getParameter("text");
            resp.setContentType("html/text");

            try (PrintWriter pw = resp.getWriter()) {
                postService.save(new Post(text, new Timestamp(new Date().getTime()), userID, picture));
                pw.write("Success");
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

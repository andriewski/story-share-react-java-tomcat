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

public class DeletePostController implements Controller {
    private PostService postService = PostServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(DeletePostController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long postID = Long.parseLong(req.getParameter("postID"));
            resp.setContentType("html/text");

            try (PrintWriter pw = resp.getWriter()) {
                postService.delete(postID);
                pw.write("Success");
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

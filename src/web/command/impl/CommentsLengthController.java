package web.command.impl;

import org.apache.log4j.Logger;
import services.CommentService;
import services.ServiceException;
import services.impl.CommentServiceImpl;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CommentsLengthController implements Controller {
    private CommentService commentService = CommentServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(CommentsLengthController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long postID = Long.parseLong(req.getParameter("postID"));
            resp.setContentType("html/text");

            try (PrintWriter pw = resp.getWriter()) {
                long numberOfCommentsInThePost = commentService.getNumberOfCommentsInThePost(postID);
                pw.write(String.valueOf(numberOfCommentsInThePost));
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

package web.command.impl;

import entites.Comment;
import org.apache.log4j.Logger;
import services.CommentService;
import services.ServiceException;
import services.impl.CommentServiceImpl;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

public class CreateCommentController implements Controller {
    private CommentService commentService = CommentServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(CreateCommentController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long userID = Long.parseLong(req.getParameter("userID"));
            long postID = Long.parseLong(req.getParameter("postID"));
            String text = req.getParameter("text");
            resp.setContentType("html/text");

            commentService.save(new Comment(userID, postID, text, new Timestamp(new Date().getTime())));

            try (PrintWriter pw = resp.getWriter()) {
                pw.write("Success");
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

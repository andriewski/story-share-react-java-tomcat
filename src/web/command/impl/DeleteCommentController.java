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

public class DeleteCommentController implements Controller {
    private CommentService commentService = CommentServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(DeleteCommentController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long commentID = Long.parseLong(req.getParameter("commentID"));
            resp.setContentType("html/text");

            commentService.delete(commentID);

            try (PrintWriter pw = resp.getWriter()) {
                pw.write("Success");
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

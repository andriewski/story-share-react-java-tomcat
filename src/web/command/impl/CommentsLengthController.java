package web.command.impl;

import services.CommentService;
import services.impl.CommentServiceImpl;
import web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CommentsLengthController implements Controller {
    private CommentService commentService = CommentServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        long postID = Long.parseLong(req.getParameter("postID"));
        resp.setContentType("html/text");

        try (PrintWriter pw = resp.getWriter()) {
            long numberOfCommentsInThePost = commentService.getNumberOfCommentsInThePost(postID);
            pw.write(String.valueOf(numberOfCommentsInThePost));
        }
    }
}

package web.command.impl;

import entites.Comment;
import services.CommentService;
import services.impl.CommentServiceImpl;
import web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

public class CreateCommentController implements Controller {
    private CommentService commentService = CommentServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        long userID = Long.parseLong(req.getParameter("userID"));
        long postID = Long.parseLong(req.getParameter("postID"));
        String text = req.getParameter("text");
        resp.setContentType("html/text");

        commentService.save(new Comment(userID, postID, text, new Timestamp(new Date().getTime())));

        try (PrintWriter pw = resp.getWriter()) {
            pw.write("Success");
        }
    }
}

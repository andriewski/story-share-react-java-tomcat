package web.command.impl;

import entites.Post;
import services.PostService;
import services.impl.PostServiceImpl;
import web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

public class CreatePostController implements Controller {
    private PostService postService = PostServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        long userID = Long.parseLong(req.getParameter("userID"));
        String picture = req.getParameter("picture");
        String text = req.getParameter("text");
        resp.setContentType("html/text");

        try (PrintWriter pw = resp.getWriter()) {
            postService.save(new Post(text, new Timestamp(new Date().getTime()), userID, picture));
            pw.write("Success");
        }
    }
}

package web.command.impl;

import services.PostService;
import services.impl.PostServiceImpl;
import web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LikeUnlikePostController implements Controller {
    private PostService postService = PostServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        long userID = Long.parseLong(req.getParameter("userID"));
        long postID = Long.parseLong(req.getParameter("postID"));
        String like = req.getParameter("like");
        resp.setContentType("html/text");

        try (PrintWriter pw = resp.getWriter()) {
            if ("like".equals(like)) {
                postService.likePostByUser(postID, userID);
            } else {
                postService.unlikePostByUser(postID, userID);
            }

            pw.write("Success");
        }
    }
}

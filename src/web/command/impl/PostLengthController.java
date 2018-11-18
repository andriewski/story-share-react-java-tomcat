package web.command.impl;

import services.PostService;
import services.impl.PostServiceImpl;
import web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PostLengthController implements Controller {
    private PostService postService = PostServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("html/text");

        try (PrintWriter pw = resp.getWriter()) {
            long numberOfPosts = postService.getNumberOfAllPosts();
            pw.write(String.valueOf(numberOfPosts));
        }
    }
}

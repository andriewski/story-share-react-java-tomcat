package web.command.impl;

import com.google.gson.Gson;
import dto.CommentDTO;
import entites.Pagination;
import org.apache.log4j.Logger;
import services.CommentService;
import services.ServiceException;
import services.impl.CommentServiceImpl;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CommentsListController implements Controller {
    private CommentService commentService = CommentServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(CommentsListController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long postID = Long.parseLong(req.getParameter("postID"));
            long offset = Long.parseLong(req.getParameter("offset"));
            int limit = Integer.parseInt(req.getParameter("limit"));
            Pagination pagination = new Pagination(offset, limit);
            resp.setContentType("application/json");

            try (PrintWriter pw = resp.getWriter()) {
                List<CommentDTO> listOfComments = commentService.getCommentsInThePostWithOffsetAndLimit(postID, pagination);
                Gson gson = new Gson();
                String json = gson.toJson(listOfComments);
                pw.write(json);
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

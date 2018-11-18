package web.command.impl;

import com.google.gson.Gson;
import dto.MessageDTO;
import entites.Pagination;
import services.MessageService;
import services.impl.MessageServiceImpl;
import web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ChatMessagesController implements Controller {
    private MessageService messageService = MessageServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        long userID = Long.parseLong(req.getParameter("userID"));
        long anotherUserID = Long.parseLong(req.getParameter("anotherUserID"));
        long offset = Long.parseLong(req.getParameter("offset"));
        int limit = Integer.parseInt(req.getParameter("limit"));

        List<MessageDTO> messageList = messageService.getUserMessagesWithOtherUserWithOffset(userID,
                anotherUserID, new Pagination(offset, limit));
        resp.setContentType("application/json");
        Gson gson = new Gson();

        try (PrintWriter pw = resp.getWriter()) {
            pw.write(gson.toJson(messageList));
        }
    }
}

package web.command.impl;

import com.google.gson.Gson;
import dto.MessageDTO;
import entites.Pagination;
import services.MessageService;
import services.ServiceException;
import services.impl.MessageServiceImpl;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.log4j.Logger;

public class ChatMessagesController implements Controller {
    private MessageService messageService = MessageServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(ChatMessagesController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
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
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

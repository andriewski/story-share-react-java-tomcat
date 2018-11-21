package web.command.impl;

import com.google.gson.Gson;
import dto.MessageDTO;
import org.apache.log4j.Logger;
import services.MessageService;
import services.ServiceException;
import services.impl.MessageServiceImpl;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MessageListController implements Controller {
    private MessageService messageService = MessageServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(MessageListController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long userID = Long.parseLong(req.getParameter("userID"));
            Gson gson = new Gson();
            resp.setContentType("application/json");

            List<MessageDTO> messageList = messageService.getLastMessagesInUsersDialogs(userID);

            try (PrintWriter pw = resp.getWriter()) {
                pw.write(gson.toJson(messageList));
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

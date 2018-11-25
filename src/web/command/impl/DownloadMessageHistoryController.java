package web.command.impl;

import dto.MessageDTO;
import entites.User;
import org.apache.log4j.Logger;
import services.MessageService;
import services.ServiceException;
import services.impl.MessageServiceImpl;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

public class DownloadMessageHistoryController implements Controller {
    private MessageService messageService = MessageServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(DownloadMessageHistoryController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long userID = Long.parseLong(req.getParameter("userID"));
            long anotherUserID = Long.parseLong(req.getParameter("anotherUserID"));
            resp.setContentType("html/text");
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");
            File file = new File("history_message.txt");

            if (user != null) {
                try (BufferedWriter writer = new BufferedWriter(new PrintWriter(file, "UTF-8"));
                     BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                     BufferedOutputStream bos = new BufferedOutputStream(resp.getOutputStream())) {
                    List<MessageDTO> messageList = messageService.getAllUserMessagesWithOtherUser(userID, anotherUserID);
                    StringBuilder sb = new StringBuilder();

                    for (MessageDTO message : messageList) {
                        sb.append(user.getId() == message.getSenderID() ? "Me: \t" : message.getSenderName() + ": \t")
                                .append(message.getText())
                                .append("\t")
                                .append(message.getDate())
                                .append("\n");
                    }

                    writer.write(sb.toString());
                    writer.flush();

                    int count;
                    while ((count = bis.read()) != -1) {
                        bos.write(count);
                    }
                }
            } else {
                try (PrintWriter pw = resp.getWriter()) {
                    pw.write("User is not authorized");
                }
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

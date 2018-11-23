package web.command.impl;

import org.apache.log4j.Logger;
import services.ServiceException;
import services.UserService;
import services.impl.UserServiceImpl;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ChangeUserStatusController implements Controller {
    private UserService userService = UserServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(ChangeUserStatusController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long userID = Long.parseLong(req.getParameter("userID"));
            String status = req.getParameter("status");
            resp.setContentType("html/text");

            try (PrintWriter pw = resp.getWriter()) {
                if ("active".equals(status)) {
                    userService.banUser(userID);
                    pw.write("The User has been banned");
                } else {
                    userService.unbanUser(userID);
                    pw.write("The User has been unbanned");
                }
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

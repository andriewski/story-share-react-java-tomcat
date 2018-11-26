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

public class UserAvatarController implements Controller {
    private UserService userService = UserServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(UserAvatarController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("html/text");
        try (PrintWriter pw = resp.getWriter()) {
            String sUserID = req.getParameter("userID");

            if (sUserID != null) {
                long userID = Long.parseLong(sUserID);
                String avatar = userService.getUserAvatar(userID);
                pw.write(avatar);
            } else {
                pw.write("Error");
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

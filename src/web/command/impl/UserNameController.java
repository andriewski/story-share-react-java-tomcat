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

public class UserNameController implements Controller {
    private UserService userService = UserServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(UserNameController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long userID = Long.parseLong(req.getParameter("userID"));
            resp.setContentType("html/text");

            try (PrintWriter pw = resp.getWriter()) {
                String name = userService.getUserName(userID);
                pw.write(name);
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}
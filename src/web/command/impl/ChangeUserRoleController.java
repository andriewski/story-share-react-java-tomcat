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

public class ChangeUserRoleController implements Controller {
    private UserService userService = UserServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(ChangeUserRoleController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long userID = Long.parseLong(req.getParameter("userID"));
            String status = req.getParameter("role");
            resp.setContentType("html/text");

            try (PrintWriter pw = resp.getWriter()) {
                if ("user".equals(status)) {
                    userService.assignAdmin(userID);
                    pw.write("Role has been changed to Admin");
                } else {
                    userService.assignUser(userID);
                    pw.write("Role has been changed to User");
                }
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}
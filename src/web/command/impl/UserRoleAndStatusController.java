package web.command.impl;

import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import services.ServiceException;
import services.UserService;
import services.impl.UserServiceImpl;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UserRoleAndStatusController implements Controller {
    private UserService userService = UserServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(UserRoleAndStatusController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long userID = Long.parseLong(req.getParameter("userID"));
            resp.setContentType("html/text");

            try (PrintWriter pw = resp.getWriter()) {
                JsonObject role = userService.getUserRoleAndStatus(userID);
                pw.write(role.toString());
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

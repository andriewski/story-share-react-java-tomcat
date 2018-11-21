package web.command.impl;

import entites.User;
import org.apache.log4j.Logger;
import services.ServiceException;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class CheckLocalStorageController implements Controller {
    final private static Logger logger = Logger.getLogger(CheckLocalStorageController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String userID = req.getParameter("userID");
            String userName = req.getParameter("userName");
            String userAvatar = req.getParameter("userAvatar");
            String userRole = req.getParameter("userRole");
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            try (PrintWriter pw = resp.getWriter()) {
                if ((user == null) && (userID != null || userName != null || userAvatar != null || userRole != null)) {
                    pw.write("User is hacker");
                } else if ((user != null) && (!String.valueOf(user.getId()).equals(userID)
                        || !user.getRole().equals(userRole) || !user.getName().equals(userName)
                        || !user.getAvatar().equals(userAvatar))) {
                    pw.write("User is hacker");
                } else {
                    pw.write("Success");
                }
            }
        } catch (IOException | NumberFormatException | ServiceException e) {
            logger.error(e);
        }
    }
}

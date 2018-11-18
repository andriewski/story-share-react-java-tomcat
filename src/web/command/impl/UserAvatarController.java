package web.command.impl;

import services.UserService;
import services.impl.UserServiceImpl;
import web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UserAvatarController implements Controller {
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        long userID = Long.parseLong(req.getParameter("userID"));
        resp.setContentType("html/text");

        try (PrintWriter pw = resp.getWriter()) {
            String avatar = userService.getUserAvatar(userID);
            pw.write(avatar);
        }
    }
}

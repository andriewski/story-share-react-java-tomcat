package web.command.impl;

import entites.User;
import org.apache.log4j.Logger;
import services.ServiceException;
import services.UserService;
import services.impl.UserServiceImpl;
import web.auth.Encoder;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateUserController implements Controller {
    private UserService userService = UserServiceImpl.getInstance();
    final private static Logger logger = Logger.getLogger(CreateUserController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String avatar = req.getParameter("avatar");
            resp.setContentType("html/text");

            try (PrintWriter pw = resp.getWriter()) {
                User user = userService.get(email);

                if (user == null) {
                    User savedUser = userService.save(new User(name, email, avatar,
                            Encoder.encode(password), "user"));
                    pw.write("User has been created");
                } else {
                    pw.write("Email already exists");
                }
            }
        } catch (IOException | ServiceException e) {
            logger.error(e);
        }
    }
}

package web.command.impl;

import entites.User;
import services.UserService;
import services.impl.UserServiceImpl;
import web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateUserController implements Controller {
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String avatar = req.getParameter("avatar");
        resp.setContentType("html/text");

        try (PrintWriter pw = resp.getWriter()) {
            User user = userService.get(email);

            if (user == null) {
                //МБ ПРОВЕРКИ ТРЕБУЮТСЯ
                User savedUser = userService.save(new User(name, email, avatar, password));
                pw.write("User has been created");
            } else {
                pw.write("Email already exists");
            }
        }
    }
}

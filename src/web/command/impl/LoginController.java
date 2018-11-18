package web.command.impl;

import com.google.gson.Gson;
import dto.UserDTO;
import entites.User;
import services.UserService;
import services.impl.UserServiceImpl;
import web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginController implements Controller {
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try(PrintWriter pw = resp.getWriter()) {
            User user = userService.get(email);

            if (user == null) {
                resp.setContentType("html/text");
                pw.write("Error login");
            } else {
                if (!user.getPassword().equals(password)) {
                    resp.setContentType("html/text");
                    pw.write("Error login");
                } else {
                    resp.setContentType("application/json");
                    HttpSession session = req.getSession();
                    session.setAttribute("user", user);
                    UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getAvatar());
                    pw.write(new Gson().toJson(userDTO));
                }
            }
        }
    }
}

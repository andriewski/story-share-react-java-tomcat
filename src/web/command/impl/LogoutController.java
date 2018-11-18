package web.command.impl;

import web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LogoutController implements Controller {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getSession().invalidate();

        try (PrintWriter pw = resp.getWriter()) {
            pw.write("Success");
        }
    }
}

package web.command.impl;

import org.apache.log4j.Logger;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LogoutController implements Controller {
    final private static Logger logger = Logger.getLogger(LogoutController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getSession().invalidate();

            try (PrintWriter pw = resp.getWriter()) {
                pw.write("Success");
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }
}

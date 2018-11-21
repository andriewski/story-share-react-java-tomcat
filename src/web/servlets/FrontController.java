package web.servlets;

import org.apache.log4j.Logger;
import web.command.enums.ControllerType;
import web.command.impl.MessageListController;
import web.handlers.RequestHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Front Controller", urlPatterns = "/frontController")
public class FrontController extends HttpServlet {
    final private static Logger logger = Logger.getLogger(FrontController.class);
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ControllerType controllerType = RequestHandler.getCommand(req);
            controllerType.getController().execute(req, resp);
        } catch (Exception e) {
            logger.fatal(e);
        }
    }
}

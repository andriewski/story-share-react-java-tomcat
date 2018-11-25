package web.handlers;

import web.command.enums.ControllerType;

import javax.servlet.http.HttpServletRequest;

public class RequestHandler {
    public static ControllerType getCommand(HttpServletRequest req) {
        String param = req.getParameter("command");

        return ControllerType.getByPageName(param);
    }
}

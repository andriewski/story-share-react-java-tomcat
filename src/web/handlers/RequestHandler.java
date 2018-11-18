package web.handlers;

import web.command.enums.ControllerType;

import javax.servlet.http.HttpServletRequest;

public class RequestHandler {
    public static ControllerType getCommand(HttpServletRequest req) {
        String param = req.getParameter("command");

//        ЧЕТ МНЕ КАЖЕТСЯ, ЧТО ТУТ ИЛИ НАДО
        if (param == null || "".equals(param)) {
            param = "home.title";
        }

        //   И НАДО РАЗОБРАТЬСЯ С HOME PAGE

        ControllerType type = ControllerType.getByPageName(param);
        return type;
    }
}

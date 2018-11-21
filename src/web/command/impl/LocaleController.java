package web.command.impl;

import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import web.command.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class LocaleController implements Controller {
    final private static Logger logger = Logger.getLogger(LocaleController.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String locale = req.getParameter("locale");
            ResourceBundle rb;
            JsonObject jsonObject = new JsonObject();
            resp.setContentType("application/json");

            if ("ru".equals(locale)) {
                rb = ResourceBundle.getBundle("localization_ru");
            } else if ("en".equals(locale)) {
                rb = ResourceBundle.getBundle("localization_en");
            } else {
                rb = ResourceBundle.getBundle("localization");
            }

            Enumeration<String> listOfKeys = rb.getKeys();
            String nextElement;
            while (listOfKeys.hasMoreElements()) {
                nextElement = listOfKeys.nextElement();
                jsonObject.addProperty(nextElement, rb.getString(nextElement));
            }

            try (PrintWriter pw = resp.getWriter()) {
                pw.write(jsonObject.toString());
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }
}

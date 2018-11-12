package servlets;

import com.google.gson.Gson;
import dao.MessageDAO;
import dao.impl.MessageDAOImpl;
import dto.MessageDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Message List Servlet", urlPatterns = "/api/message/listOfMessages")
public class MessageListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userID = Long.parseLong(req.getParameter("userID"));
        MessageDAO messageDAO = MessageDAOImpl.getInstance();

        try {
            List<MessageDTO> messageList = messageDAO.getLastMessagesInUsersDialogs(userID);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            Gson gson = new Gson();

            try (PrintWriter pw = resp.getWriter()) {
                pw.write(gson.toJson(messageList));
                pw.flush();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

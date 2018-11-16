package servlets;

import com.google.gson.Gson;
import dao.MessageDAO;
import dao.impl.MessageDAOImpl;
import dto.MessageDTO;
import entites.Pagination;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
//http://localhost:8080/api/message/chat?userID=1&anotherUserID=2&offset=0&limit=100
@WebServlet(name = "Chat Messages Servlet", urlPatterns = "/api/message/chat")
public class ChatMessagesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userID = Long.parseLong(req.getParameter("userID"));
        long anotherUserID = Long.parseLong(req.getParameter("anotherUserID"));
        long offset = Long.parseLong(req.getParameter("offset"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        MessageDAO messageDAO = MessageDAOImpl.getInstance();

        try {
            List<MessageDTO> messageList = messageDAO.getUserMessagesWithOtherUserWithOffset(userID,
                    anotherUserID, new Pagination(offset, limit));
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

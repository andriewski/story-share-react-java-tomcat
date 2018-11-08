package servlets;

import com.google.gson.Gson;
import dao.UserDAO;
import dao.impl.UserDAOImpl;
import dto.UserDTO;
import entites.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class UserLogin extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        UserDAO userDAO = UserDAOImpl.getInstance();

        try(PrintWriter pw = resp.getWriter()) {
            resp.setCharacterEncoding("UTF-8");

            try {
                User user = userDAO.get(email);

                if (user == null) {
                    resp.setContentType("html/text");
                    pw.write("User not found");
                } else {
                    if (!user.getPassword().equals(password)) {
                        resp.setContentType("html/text");
                        pw.write("Password incorrect");
                    } else {
                        HttpSession session = req.getSession();
                        session.setAttribute("user", user);
                        resp.setContentType("application/json");
                        UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getAvatar());
                        pw.write(new Gson().toJson(userDTO));
                    }
                }

                pw.flush();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

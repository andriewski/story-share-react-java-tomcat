package web.filters;

import entites.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String command = req.getParameter("command");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            if ("chat_messages".equals(command) || "message_list".equals(command) ||
                    "create_post".equals(command) || "create_comment".equals(command) ||
                    "like_unlike_post".equals(command)) {
                try (PrintWriter pw = resp.getWriter()) {
                    pw.write("User is not authorized");
                }

                return;
            } else {
                resp.setHeader("authorization", "User is not authorized");
            }
        } else {
            resp.setHeader("authorization", "User is authorized");
        }

        filterChain.doFilter(req, resp);
    }
}

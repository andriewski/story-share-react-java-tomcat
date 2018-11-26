package web.filters;

import entites.User;
import org.apache.log4j.Logger;
import services.UserService;
import services.impl.UserServiceImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    final private static Logger logger = Logger.getLogger(AuthFilter.class);
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpServletResponse resp = (HttpServletResponse) servletResponse;

            String command = req.getParameter("command");
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            if ("user_avatar".equals(command) || "post_length".equals(command) || "comments_length".equals(command)
                    || "get_locale".equals(command)) {
                filterChain.doFilter(req, resp);
            } else if (user == null) {
                if ("chat_messages".equals(command) || "message_list".equals(command)
                        || "create_post".equals(command) || "create_comment".equals(command)
                        || "like_unlike_post".equals(command) || "change_status".equals(command)
                        || "delete_post".equals(command) || "user_role_and_status".equals(command)
                        || "change_role".equals(command) || "download_history_message".equals(command)) {
                    try (PrintWriter pw = resp.getWriter()) {
                        pw.write("User is not authorized");
                    }

                    return;
                } else {
                    resp.setHeader("authorization", "User is not authorized");
                }
            } else {
                User checkedUser = userService.get(user.getEmail());

                if (checkedUser.isDeleted()) {
                    try (PrintWriter pw = resp.getWriter()) {
                        pw.write("User is banned");
                        req.getSession().invalidate();

                        return;
                    }
                } else {
                    resp.setHeader("authorization", "User is authorized");
                }
            }

            filterChain.doFilter(req, resp);
        } catch (IOException | ServletException e) {
            logger.error(e);
        }
    }
}

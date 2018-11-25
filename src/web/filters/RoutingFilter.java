package web.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class RoutingFilter implements Filter {
    final private static Logger logger = Logger.getLogger(RoutingFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpServletResponse resp = (HttpServletResponse) servletResponse;

            String requestURL = req.getRequestURL().toString();

            if (requestURL.contains("/home") || requestURL.contains("/messages")
                    || requestURL.contains("/chat/") || requestURL.contains("/story/")
                    || requestURL.contains("/login")) {
                resp.sendRedirect("/index.html");
            } else {
                filterChain.doFilter(req, resp);
            }
        } catch (IOException | ServletException e) {
            logger.error(e);
        }
    }
}

package web.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "encodingFilter", urlPatterns = "/*",
        initParams = {@WebInitParam(name = "encoding", value = "UTF-8")})
public class EncodingFilter implements Filter {
    final private static Logger logger = Logger.getLogger(EncodingFilter.class);
    private String encoding;

    public void init(FilterConfig filterConfig) {
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null && !"".equals(encodingParam)) {
            encoding = encodingParam;
        } else {
            encoding = "UTF-8";
        }
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) {
        try {
            req.setCharacterEncoding(encoding);
            //response.setContentType("text/html; charset=" + encoding); НЕ согласен
            resp.setCharacterEncoding(encoding);
            ((HttpServletResponse) resp).setHeader("Content-Language", "UTF-8");
            filterChain.doFilter(req, resp);
        } catch (IOException | ServletException e) {
            logger.error(e);
        }
    }
}

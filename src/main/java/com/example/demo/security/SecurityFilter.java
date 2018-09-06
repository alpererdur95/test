package com.example.demo.security;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class SecurityFilter implements Filter {

    public SecurityFilter() {
    }

    public void init(FilterConfig conf) {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();
        if (url.equals("login") || url.equals("/login") || url.equals("/auth/check")||isResource(url))
            chain.doFilter(servletRequest, servletResponse);
        else {
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("/login");
                response.setContentType("text/css");
                response.setContentType("text/javascript");
            } else {
                chain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    private boolean isResource(String url){

        return url.endsWith(".js") || url.endsWith(".css") || url.startsWith("/images")|| url.startsWith("/icons");
    }
}

package com.bookly.backend.filters;

import com.bookly.backend.services.UserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityFilter implements Filter {

    private final UserService userService;

    public SecurityFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getRequestURL().toString().contains("/users") && httpRequest.getMethod().equals("POST")) {
            chain.doFilter(request, response);
        }

        String securityToken = httpRequest.getHeader("Security-Token");

        if (securityToken != null && userService.tryGetUserByToken(securityToken).isPresent()) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}

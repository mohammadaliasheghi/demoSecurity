package com.google.demosecurity.config;

import com.google.demosecurity.enums.Authority;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication.getAuthorities().contains(Authority.OP_ACCESS_ADMIN))
            redirectStrategy.sendRedirect(request, response, "/admin");
        else if (authentication.getAuthorities().contains(Authority.OP_ACCESS_USER))
            redirectStrategy.sendRedirect(request, response, "/user");
        else
            redirectStrategy.sendRedirect(request, response, "/");
    }
}

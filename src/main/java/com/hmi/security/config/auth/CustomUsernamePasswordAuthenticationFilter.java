package com.hmi.security.config.auth;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private String usernameParameter = "username";
    private String passwordParameter = "password";

    public CustomUsernamePasswordAuthenticationFilter() {
        setFilterProcessesUrl("/auth/token");
    }

    @Override
    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        return request.getHeader(passwordParameter);
    }

    @Override
    @Nullable
    protected String obtainUsername(HttpServletRequest request) {
        return request.getHeader(usernameParameter);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}

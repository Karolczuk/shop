package com.app.security.filters;

import com.app.security.tokens.TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final TokenService tokenService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
        super(authenticationManager);
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("----------------------------------- 1 ----------------------------------------");
        System.out.println(accessToken);

        if (Objects.nonNull(accessToken)) {
            UsernamePasswordAuthenticationToken auth = tokenService.parseAccessToken(accessToken);
            System.out.println("----------------------------------- 2 ----------------------------------------");
            System.out.println(auth);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }
}

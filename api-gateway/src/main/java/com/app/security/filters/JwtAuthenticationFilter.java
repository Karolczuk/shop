package com.app.security.filters;

import com.app.dto.AuthenticationUser;
import com.app.exception.ApiGatewayException;
import com.app.security.tokens.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        try {
            AuthenticationUser user = new ObjectMapper().readValue(request.getInputStream(), AuthenticationUser.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.emptyList()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiGatewayException("attempt authentication exception");
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        var tokens = tokenService.generateTokens(authResult);
        var json = new ObjectMapper().writeValueAsString(tokens);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(json);
        response.getWriter().flush();
        response.getWriter().close();
    }
}

package com.app.security;

import com.app.security.filters.JwtAuthenticationFilter;
import com.app.security.filters.JwtAuthorizationFilter;
import com.app.security.tokens.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    public SecurityConfiguration(
            @Qualifier("userDetailsService") UserDetailsService userDetailsService,
            TokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // do konfiguracji security
        http
                .cors()

                .and()
                .csrf()
                .disable()

                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())

                .and()
                .authorizeRequests()
                .antMatchers("/api/users/security/**").permitAll() // dostepne dla wszytkich
                .antMatchers("/api/service/users/**").hasRole("USER")
                .antMatchers("/api/service/admins/**").hasRole("ADMIN")
                .anyRequest().authenticated() // inne zadanie trzeba byc przynajneij zalogownym

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), tokenService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), tokenService));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (httpServletRequest, httpServletResponse, e) -> {
            var json = new ObjectMapper().writeValueAsString(e.getMessage());

            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE); // dne beda wsyalne w postaci jsona
            httpServletResponse.getWriter().write(json); // pobieramyy strumien i zapisujemy tego jsona i info
            httpServletResponse.getWriter().flush();  //
            httpServletResponse.getWriter().close();
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            var json = new ObjectMapper().writeValueAsString(e.getMessage());

            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.getWriter().write(json);
            httpServletResponse.getWriter().flush();
            httpServletResponse.getWriter().close();
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

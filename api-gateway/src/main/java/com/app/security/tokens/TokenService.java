package com.app.security.tokens;

import com.app.dto.RefreshTokenData;
import com.app.exception.ApiGatewayException;
import com.app.proxy.FindUserProxy;
import com.app.security.dto.Tokens;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${token.access.expiration-time-ms}")
    private Long accessTokenExpirationTimeMs;

    @Value("${token.refresh.expiration-time-ms}")
    private Long refreshTokenExpirationTimeMs;

    @Value("${token.refresh.property}")
    private String refreshTokenProperty;

    @Value("${token.prefix}")
    private String tokenPrefix;

    private final FindUserProxy findUserProxy;
    private final SecretKey secretKey;

    // ---------------------------------------------------------------------------------------
    // GENERATE TOKENS
    // ---------------------------------------------------------------------------------------
    public Tokens generateTokens(Authentication authentication) {

        if (Objects.isNull(authentication)) {
            throw new ApiGatewayException("authentication is null");
        }

        var user = findUserProxy.findByUsername(authentication.getName());

        Date currentDate = new Date();

        long accessTokenExpirationDateMs = System.currentTimeMillis() + accessTokenExpirationTimeMs;
        Date accessTokenExpirationDate = new Date(accessTokenExpirationDateMs);

        long refreshTokenExpirationDateMs = System.currentTimeMillis() + refreshTokenExpirationTimeMs;
        Date refreshTokenExpirationDate = new Date(refreshTokenExpirationDateMs);

        var accessToken = Jwts
                .builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(currentDate)
                .setExpiration(accessTokenExpirationDate)
                .signWith(secretKey)
                .compact();

        var refreshToken = Jwts
                .builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(currentDate)
                .setExpiration(refreshTokenExpirationDate)
                .claim(refreshTokenProperty, accessTokenExpirationDateMs)
                .signWith(secretKey)
                .compact();

        return Tokens
                .builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }

    public Tokens generateTokens(RefreshTokenData refreshTokenData) {

        String refreshToken = refreshTokenData.getRefreshToken();

        if (Objects.isNull(refreshToken)) {
            throw new ApiGatewayException("authentication is null");
        }

        // musimy sprawdzic czy moge jeszcze wygenerowac refresh token
        var accessTokenExpirationDateMsFromToken = Long.parseLong(getClaims(refreshToken).get(refreshTokenProperty).toString());
        if (accessTokenExpirationDateMsFromToken < System.currentTimeMillis()) {
            throw new ApiGatewayException("cannot generate refresh token");
        }

        var userId = getId(refreshToken);
        var user = findUserProxy
                .findById(userId);

        Date currentDate = new Date();

        long accessTokenExpirationDateMs = System.currentTimeMillis() + accessTokenExpirationTimeMs;
        Date accessTokenExpirationDate = new Date(accessTokenExpirationDateMs);

        Date refreshTokenExpirationDate = getExpirationDate(refreshToken);

        var accessToken = Jwts
                .builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(currentDate)
                .setExpiration(accessTokenExpirationDate)
                .signWith(secretKey)
                .compact();

        var newRefreshToken = Jwts
                .builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(currentDate)
                .setExpiration(refreshTokenExpirationDate)
                .claim(refreshTokenProperty, accessTokenExpirationDateMs)
                .signWith(secretKey)
                .compact();

        return Tokens
                .builder()
                .refreshToken(newRefreshToken)
                .accessToken(accessToken)
                .build();

    }

    // ---------------------------------------------------------------------------------------
    // PARSE TOKENS
    // ---------------------------------------------------------------------------------------
    public UsernamePasswordAuthenticationToken parseAccessToken(String token) {
        if (Objects.isNull(token)) {
            throw new ApiGatewayException("token is null");
        }

        if (!token.startsWith(tokenPrefix)) {
            throw new ApiGatewayException("token is not correct");
        }

        if (!isTokenValid(token)) {
            throw new ApiGatewayException("token has been expired");
        }

        Long userId = getId(token);
        var user = findUserProxy.findById(userId);
        return new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority(user.getRole().toString())));

    }

    private Claims getClaims(String token) {
        if (Objects.isNull(token)) {
            throw new ApiGatewayException("token is null");
        }

        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token.replace(tokenPrefix, ""))
                .getBody();
    }

    public Long getId(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    public boolean isTokenValid(String token) {
        var expirationDate = getExpirationDate(token);
        return expirationDate.after(new Date());
    }
}

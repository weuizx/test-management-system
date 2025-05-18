package org.evilincorporated.pineapple.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.security.exception.ExpiredTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final UserDetailsService userDetailsService;

    @Value("${app.security.jwt.secret}")
    private String secretKey;
    @Value("${app.security.jwt.lifetime.access}")
    private Long accessLifetime;
    @Value("${app.security.jwt.lifetime.refresh}")
    private Long refreshLifetime;

    public JwtPair generateTokenPair(final UserDetails userDetails) {
        String token = generateAccessToken(userDetails);
        String refreshToken = generateRefreshToken(userDetails);
        return new JwtPair(token, refreshToken);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateJwt(userDetails, accessLifetime);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateJwt(userDetails, refreshLifetime);
    }

    private String generateJwt(UserDetails userDetails, long tokenLifetime) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + tokenLifetime))
                .signWith(getSigningKey())
                .compact();
    }

    public UserDetails parseJwtToken(final String accessToken) {
        UserDetails userDetails = null;
        if (StringUtils.hasText(accessToken) && validateToken(accessToken)) {
            String username = extractUsername(accessToken);
            userDetails = userDetailsService.loadUserByUsername(username);
        }
        return userDetails;
    }

    public boolean validateToken(final String authToken) {
        try {
            extractAllClaims(authToken);///хз тут ваще исключения могут вылететь или нет как будто всегда тру
            return true;
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException ex) {
            logger.debug("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (SignatureException | ExpiredJwtException expiredEx) {
            logger.debug("JWT Token is expired", expiredEx);
            throw new ExpiredTokenException(authToken, "JWT Token expired", expiredEx);
        }
    }

    public String getTokenFromRequest(final HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }
        if (header.length() < BEARER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }
        return header.substring(BEARER_PREFIX.length());
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {
        JwtParserBuilder parser = Jwts.parser();
        parser.verifyWith(getSigningKey());

        return parser.build()
                .parseSignedClaims(token)//signed claims ???
                .getPayload();
    }

}


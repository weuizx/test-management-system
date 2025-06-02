package org.evilincorporated.pineapple.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.PathItem;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.security.service.Token;
import org.evilincorporated.pineapple.security.service.TokensDto;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
public class RequestJwtTokensFilter extends OncePerRequestFilter {

    private static final String BASIC_PREFIX = "Basic ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/v1/jwt/tokens", PathItem.HttpMethod.POST.name());
    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private final Function<Authentication, Token> refreshTokenFactory;
    private final Function<Token, Token> accessTokenFactory;
    private final Function<Token, String> refreshTokenSerializer;
    private final Function<Token, String> accessTokenSerializer;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!this.requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContext context = resolveSecurityContextFromBasicAuth(request);
        if (context == null || context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User must be authenticated");
            return;
        }
        Token refreshToken = refreshTokenFactory.apply(context.getAuthentication());
        Token accessToken = accessTokenFactory.apply(refreshToken);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), new TokensDto(
                accessTokenSerializer.apply(accessToken), accessToken.getExpiredAt().toString(),
                refreshTokenSerializer.apply(refreshToken), refreshToken.getExpiredAt().toString()
        ));
    }

    private SecurityContext resolveSecurityContextFromBasicAuth(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header == null || !header.startsWith(BASIC_PREFIX)) {
            return null;
        }

        try {
            String base64Credentials = header.substring(BASIC_PREFIX.length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), UTF_8);
            String[] parts = credentials.split(":", 2);
            if (parts.length != 2) return null;

            String username = parts[0];
            String password = parts[1];

            Authentication authRequest = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authResult = authenticationManager.authenticate(authRequest);

            SecurityContext context = org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            return context;

        } catch (Exception e) {
            return null;
        }
    }


}

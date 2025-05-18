package org.evilincorporated.pineapple.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.PathItem;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.security.service.Token;
import org.evilincorporated.pineapple.security.service.TokenUser;
import org.evilincorporated.pineapple.security.service.TokensDto;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.function.Function;

@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/jwt/refresh", PathItem.HttpMethod.POST.name());
    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Function<Token, Token> accessTokenFactory;
    private final Function<Token, String> accessTokenStringSerializer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!this.requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (securityContextRepository.containsContext(request)) {
            SecurityContext context = securityContextRepository.loadDeferredContext(request).get();
            if (context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken token &&
                    token.getPrincipal() instanceof TokenUser user &&
                    token.getAuthorities().contains(new SimpleGrantedAuthority("JWT_REFRESH"))) {
                Token accessToken = accessTokenFactory.apply(user.getToken());

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(response.getWriter(), new TokensDto(
                        accessTokenStringSerializer.apply(accessToken), accessToken.getExpiredAt().toString(),
                        null, null));
                return;
            }

            throw new AccessDeniedException("User must be authenticated with JWT");
        }
    }
}

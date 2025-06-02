package org.evilincorporated.pineapple.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.PathItem;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.ApiError;
import org.evilincorporated.pineapple.domain.entity.DeactivatedToken;
import org.evilincorporated.pineapple.domain.repository.DeactivatedTokenRepository;
import org.evilincorporated.pineapple.security.service.TokenUser;
import org.springframework.http.HttpStatus;
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

@RequiredArgsConstructor
public class JwtLogoutFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/v1/jwt/logout", PathItem.HttpMethod.POST.name());
    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final DeactivatedTokenRepository deactivatedTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (securityContextRepository.containsContext(request)) {
            SecurityContext context = securityContextRepository.loadDeferredContext(request).get();
            if (context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken token &&
                    token.getPrincipal() instanceof TokenUser user &&
                    token.getAuthorities().contains(new SimpleGrantedAuthority("JWT_LOGOUT"))) {

                deactivatedTokenRepository.save(new DeactivatedToken(user.getToken().getId(), user.getToken().getExpiredAt()));
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), new ApiError(
                    String.valueOf(HttpStatus.FORBIDDEN.value()),
                    HttpStatus.FORBIDDEN.getReasonPhrase(),
                    "User must be authenticated with JWT refresh token"
            ));
        }
    }
}

package org.evilincorporated.pineapple.security.filter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.domain.repository.DeactivatedTokenRepository;
import org.evilincorporated.pineapple.security.service.Token;
import org.evilincorporated.pineapple.security.service.TokenAuthenticationUserDetailsService;
import org.evilincorporated.pineapple.security.service.jwt.DefaultAccessTokenFactory;
import org.evilincorporated.pineapple.security.service.jwt.DefaultRefreshTokenFactory;
import org.evilincorporated.pineapple.security.service.jwt.JwtAuthenticationConverter;
import org.evilincorporated.pineapple.security.service.jwt.JwtConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.function.Function;

@RequiredArgsConstructor
public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private final JwtConfiguration jwtConfiguration;
    private final Function<Token, String> refreshTokenJweStringSerializer;
    private final Function<Token, String> accessTokenJwsStringSerializer;
    private final Function<String, Token> refreshTokenStringDeserializer;
    private final Function<String, Token> accessTokenStringDeserializer;


    private final DeactivatedTokenRepository deactivatedTokenRepository;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        CsrfConfigurer csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        if (csrfConfigurer != null) {
            csrfConfigurer.ignoringRequestMatchers(new AntPathRequestMatcher("/jwt/tokens", "POST"));
        }
        super.init(builder);
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        Function<Authentication, Token> refreshTokenFactory = new DefaultRefreshTokenFactory(jwtConfiguration.getRefreshTokenTtl());
        Function<Token, Token> accessTokenFactory = new DefaultAccessTokenFactory(jwtConfiguration.getAccessTokenTtl());
        RequestJwtTokensFilter requestJwtTokensFilter = new RequestJwtTokensFilter(
                refreshTokenFactory, accessTokenFactory,
                refreshTokenJweStringSerializer, accessTokenJwsStringSerializer);

        AuthenticationConverter converter = new JwtAuthenticationConverter(refreshTokenStringDeserializer, accessTokenStringDeserializer);
        AuthenticationFilter jwtAuthenticationFilter = new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class), converter);
        jwtAuthenticationFilter.setSuccessHandler(((request, response, authentication) -> CsrfFilter.skipRequest(request)));
        jwtAuthenticationFilter.setFailureHandler((request, response, exception) -> response.sendError(HttpServletResponse.SC_FORBIDDEN));

        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(new TokenAuthenticationUserDetailsService(deactivatedTokenRepository));

        RefreshTokenFilter refreshTokenFilter = new RefreshTokenFilter(accessTokenFactory, accessTokenJwsStringSerializer);

        JwtLogoutFilter jwtLogoutFilter = new JwtLogoutFilter(deactivatedTokenRepository);

        builder
                .addFilterBefore(requestJwtTokensFilter, AnonymousAuthenticationFilter.class)
                .addFilterBefore(refreshTokenFilter, AnonymousAuthenticationFilter.class)
                .addFilterBefore(jwtLogoutFilter, AnonymousAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class)
                .authenticationProvider(authenticationProvider);
    }
}

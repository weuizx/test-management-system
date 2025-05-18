package org.evilincorporated.pineapple.security.configuration;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.security.jwt.JwtTokenAuthenticationFilter;
import org.evilincorporated.pineapple.security.jwt.JwtTokenProvider;
import org.evilincorporated.pineapple.security.jwt.SkipPathRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.List;

import static org.evilincorporated.pineapple.security.configuration.SecurityConfiguration.*;

@RequiredArgsConstructor
public class JwtTokenAuthenticationFilterConfigurer extends AbstractHttpConfigurer<JwtTokenAuthenticationFilterConfigurer, HttpSecurity> {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
    }

    @Override
    public void init(HttpSecurity builder) throws Exception {
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
        List<String> pathsToSkip = List.of(SIGNIN_ENTRY_POINT, SIGNUP_ENTRY_POINT, SWAGGER_ENTRY_POINT, API_DOCS_ENTRY_POINT, TOKEN_REFRESH_ENTRY_POINT);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip);
        JwtTokenAuthenticationFilter filter = new JwtTokenAuthenticationFilter(jwtTokenProvider, matcher);
        filter.setAuthenticationManager(authenticationManager);
    }
}

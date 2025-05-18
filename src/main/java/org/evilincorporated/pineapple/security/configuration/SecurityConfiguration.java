package org.evilincorporated.pineapple.security.configuration;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.security.jwt.JwtTokenProvider;
import org.evilincorporated.pineapple.security.jwt.SkipPathRequestMatcher;
import org.evilincorporated.pineapple.security.jwt.JwtTokenAuthenticationFilter;
import org.evilincorporated.pineapple.security.jwt.JwtTokenAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

import static org.evilincorporated.pineapple.repository.entity.UserRole.ADMIN;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    public static final String SIGNIN_ENTRY_POINT = "/api/v1/pineapple/auth/signin";
    public static final String SIGNUP_ENTRY_POINT = "/api/v1/pineapple/auth/signup";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/v1/pineapple/auth/refresh-token";
    public static final String SWAGGER_ENTRY_POINT = "/swagger-ui/**";
    public static final String API_DOCS_ENTRY_POINT = "/v3/api-docs/**";
    public static final String ADMIN_ENTRY_POINT = "/api/v1/pineapple/test/admin/**";

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(configurer -> configurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SIGNIN_ENTRY_POINT).permitAll()
                        .requestMatchers(SIGNUP_ENTRY_POINT).permitAll()
                        .requestMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll()
                        .requestMatchers(SWAGGER_ENTRY_POINT).permitAll()
                        .requestMatchers(API_DOCS_ENTRY_POINT).permitAll()
                        .requestMatchers(ADMIN_ENTRY_POINT).hasRole(ADMIN.getAuthority())
                        .anyRequest().authenticated())
                .addFilterBefore(buildJwtTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(jwtTokenAuthenticationProvider);
    }

    protected JwtTokenAuthenticationFilter buildJwtTokenAuthenticationFilter() {
        List<String> pathsToSkip = List.of(SIGNIN_ENTRY_POINT, SIGNUP_ENTRY_POINT, SWAGGER_ENTRY_POINT, API_DOCS_ENTRY_POINT, TOKEN_REFRESH_ENTRY_POINT);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip);
        JwtTokenAuthenticationFilter filter = new JwtTokenAuthenticationFilter(jwtTokenProvider, matcher);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

}

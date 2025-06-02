package org.evilincorporated.pineapple.security;

import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.domain.repository.DeactivatedTokenRepository;
import org.evilincorporated.pineapple.security.filter.JwtAuthenticationConfigurer;
import org.evilincorporated.pineapple.security.service.jwt.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConfigurer jwtAuthenticationConfigurer) throws Exception {

        http.apply(jwtAuthenticationConfigurer);

        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/manager.html").hasRole("MANAGER")
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui/index.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(JwtProperties jwtProperties,
                                                                   DeactivatedTokenRepository deactivatedTokenRepository,
                                                                   AuthenticationManager authenticationManager
    ) throws Exception {
        return new JwtAuthenticationConfigurer(
                jwtProperties,
                new RefreshTokenJweStringSerializer(new DirectEncrypter(OctetSequenceKey.parse(jwtProperties.getRefreshTokenKey()))),
                new AccessTokenJwsStringSerializer(new MACSigner(OctetSequenceKey.parse(jwtProperties.getAccessTokenKey()))),
                new RefreshTokenJweStringDeserializer(new DirectDecrypter(OctetSequenceKey.parse(jwtProperties.getRefreshTokenKey()))),
                new AccessTokenJwsStringDeserializer(new MACVerifier(OctetSequenceKey.parse(jwtProperties.getAccessTokenKey()))),
                deactivatedTokenRepository, authenticationManager);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // разрешаем любые источники
        configuration.setAllowedMethods(List.of("*"));        // любые методы
        configuration.setAllowedHeaders(List.of("*"));        // любые заголовки
        configuration.setAllowCredentials(true);              // куки и сессии (если нужны)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

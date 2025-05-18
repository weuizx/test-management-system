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
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConfigurer jwtAuthenticationConfigurer) throws Exception {

        http.apply(jwtAuthenticationConfigurer);

        return http
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/manager.html").hasRole("MANAGER")
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(JwtConfiguration jwtConfiguration,
                                                                   DeactivatedTokenRepository deactivatedTokenRepository
    ) throws Exception {
        return new JwtAuthenticationConfigurer(
                jwtConfiguration,
                new RefreshTokenJweStringSerializer(new DirectEncrypter(OctetSequenceKey.parse(jwtConfiguration.getRefreshTokenKey()))),
                new AccessTokenJwsStringSerializer(new MACSigner(OctetSequenceKey.parse(jwtConfiguration.getAccessTokenKey()))),
                new RefreshTokenJweStringDeserializer(new DirectDecrypter(OctetSequenceKey.parse(jwtConfiguration.getRefreshTokenKey()))),
                new AccessTokenJwsStringDeserializer(new MACVerifier(OctetSequenceKey.parse(jwtConfiguration.getAccessTokenKey()))),
                deactivatedTokenRepository);
    }

}

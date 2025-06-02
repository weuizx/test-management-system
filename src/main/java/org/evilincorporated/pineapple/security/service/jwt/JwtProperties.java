package org.evilincorporated.pineapple.security.service.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@AllArgsConstructor
@ConfigurationProperties(prefix = "app.security.jwt", ignoreUnknownFields = false)
public class JwtProperties {

    private final String accessTokenKey;
    private final String refreshTokenKey;
    private final Duration accessTokenTtl;
    private final Duration refreshTokenTtl;
}
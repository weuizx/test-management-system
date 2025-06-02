package org.evilincorporated.pineapple.security.service.jwt;


import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.security.service.Token;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
public class DefaultRefreshTokenFactory implements Function<Authentication, Token> {

    private static final String GRANT_PREFIX = "GRANT_";

    private final Duration refreshTokenTtl;

    @Override
    public Token apply(Authentication authentication) {
        List<String> authorities = new LinkedList<>();
        authorities.add("JWT_REFRESH");
        authorities.add("JWT_LOGOUT");
        authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> GRANT_PREFIX + authority)
                .forEach(authorities::add);
        Instant now = Instant.now();
        return new Token(UUID.randomUUID(), authentication.getName(), authorities, now, now.plus(refreshTokenTtl));
    }
}

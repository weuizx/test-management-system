package org.evilincorporated.pineapple.security.service.jwt;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.security.service.Token;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class DefaultAccessTokenFactory implements Function<Token, Token> {

    private static final String GRANT_PREFIX = "GRANT_";

    private final Duration accessTokenTtl;

    @Override
    public Token apply(Token token) {
        List<String> authorities =
                token.getAuthorities().stream()
                        .filter(authority -> authority.startsWith(GRANT_PREFIX))
                        .map(authority -> authority.substring(GRANT_PREFIX.length()))
                        .toList();
        Instant now = Instant.now();
        return new Token(token.getId(), token.getSubject(), token.getUserId(), authorities, now, now.plus(accessTokenTtl));
    }
}
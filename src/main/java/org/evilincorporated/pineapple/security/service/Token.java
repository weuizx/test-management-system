package org.evilincorporated.pineapple.security.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Token {
    private final UUID id;
    private final String subject;
    private final Long userId;
    private final List<String> authorities;
    private final Instant createdAt;
    private final Instant expiredAt;

}

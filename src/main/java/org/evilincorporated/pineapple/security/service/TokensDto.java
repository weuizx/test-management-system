package org.evilincorporated.pineapple.security.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokensDto {
    private final String accessToken;
    private final String accessTokenExpiry;
    private final String refreshToken;
    private final String refreshTokenExpiry;

}

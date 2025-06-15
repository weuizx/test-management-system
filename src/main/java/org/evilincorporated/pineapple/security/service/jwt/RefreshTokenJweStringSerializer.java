package org.evilincorporated.pineapple.security.service.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.evilincorporated.pineapple.security.service.Token;

import java.util.Date;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class RefreshTokenJweStringSerializer implements Function<Token, String> {

    private static final String CLAIM_AUTHORITIES = "authorities";
    private static final String CLAIM_USER_ID = "userId";

    private final JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;
    private final EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;
    private final JWEEncrypter jweEncrypter;


    @Override
    public String apply(Token token) {
        JWEHeader jweHeader = new JWEHeader.Builder(jweAlgorithm, encryptionMethod).keyID(token.getId().toString()).build();
        JWTClaimsSet claimSet = new JWTClaimsSet.Builder()
                .jwtID(token.getId().toString())
                .subject(token.getSubject())
                .issueTime(Date.from(token.getCreatedAt()))
                .expirationTime(Date.from(token.getExpiredAt()))
                .claim(CLAIM_AUTHORITIES, token.getAuthorities())
                .claim(CLAIM_USER_ID, token.getUserId())
                .build();
        try {
            EncryptedJWT encryptedJWT = new EncryptedJWT(jweHeader, claimSet);
            encryptedJWT.encrypt(jweEncrypter);

            return encryptedJWT.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

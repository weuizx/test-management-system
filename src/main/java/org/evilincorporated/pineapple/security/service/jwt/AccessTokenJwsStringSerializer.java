package org.evilincorporated.pineapple.security.service.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.evilincorporated.pineapple.security.service.Token;

import java.util.Date;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenJwsStringSerializer implements Function<Token, String> {

    private static final String CLAIM_AUTHORITIES = "authorities";

    private final JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;
    private final JWSSigner jwsSigner;


    @Override
    public String apply(Token token) {
        JWSHeader jwsHeader = new JWSHeader.Builder(this.jwsAlgorithm).keyID(token.getId().toString()).build();
        JWTClaimsSet claimSet = new JWTClaimsSet.Builder()
                .jwtID(token.getId().toString())
                .subject(token.getSubject())
                .issueTime(Date.from(token.getCreatedAt()))
                .expirationTime(Date.from(token.getExpiredAt()))
                .claim(CLAIM_AUTHORITIES, token.getAuthorities())
                .build();
        try {
            SignedJWT signedJwt = new SignedJWT(jwsHeader, claimSet);
            signedJwt.sign(jwsSigner);

            return signedJwt.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

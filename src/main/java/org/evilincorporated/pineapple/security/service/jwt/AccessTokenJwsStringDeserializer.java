package org.evilincorporated.pineapple.security.service.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.evilincorporated.pineapple.security.service.Token;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenJwsStringDeserializer implements Function<String, Token> {

    private static final String CLAIM_AUTHORITIES = "authorities";

    private final JWSVerifier jwsVerifier;

    @Override
    public Token apply(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (signedJWT.verify(jwsVerifier)) {
                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                return new Token(
                        UUID.fromString(claimsSet.getJWTID()),
                        claimsSet.getSubject(),
                        claimsSet.getStringListClaim(CLAIM_AUTHORITIES),
                        claimsSet.getIssueTime().toInstant(),
                        claimsSet.getExpirationTime().toInstant());
            }
        } catch (ParseException | JOSEException e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }
}

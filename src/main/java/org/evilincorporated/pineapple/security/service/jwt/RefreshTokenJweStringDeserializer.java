package org.evilincorporated.pineapple.security.service.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.evilincorporated.pineapple.security.service.Token;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class RefreshTokenJweStringDeserializer implements Function<String, Token> {

    private static final String CLAIM_AUTHORITIES = "authorities";
    private static final String CLAIM_USER_ID = "userId";

    private final JWEDecrypter jweDecrypter;

    @Override
    public Token apply(String token) {
        try {
            EncryptedJWT encryptedJWT = EncryptedJWT.parse(token);
            encryptedJWT.decrypt(jweDecrypter);
            JWTClaimsSet claimsSet = encryptedJWT.getJWTClaimsSet();
            return new Token(
                    UUID.fromString(claimsSet.getJWTID()),
                    claimsSet.getSubject(),
                    claimsSet.getLongClaim(CLAIM_USER_ID),
                    claimsSet.getStringListClaim(CLAIM_AUTHORITIES),
                    claimsSet.getIssueTime().toInstant(),
                    claimsSet.getExpirationTime().toInstant());
        } catch (ParseException | JOSEException e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }
}

package org.evilincorporated.pineapple.service;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.SignInDtoIn;
import org.evilincorporated.pineapple.repository.ClientRepository;
import org.evilincorporated.pineapple.repository.entity.Client;
import org.evilincorporated.pineapple.security.jwt.JwtPair;
import org.evilincorporated.pineapple.security.jwt.JwtTokenProvider;
import org.evilincorporated.pineapple.security.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtPair signIn(SignInDtoIn signInDtoIn) {

        UserDetails user = authenticateByUsernameAndPassword(signInDtoIn.getUsername(), signInDtoIn.getPassword());

        String access = jwtTokenProvider.generateAccessToken(user);
        String refresh = jwtTokenProvider.generateRefreshToken(user);

        return new JwtPair(access, refresh);
    }

    private UserDetails authenticateByUsernameAndPassword(final String username, final String password) {
        Client client = clientRepository.findByUsername(username).orElseThrow(() ->
                new RuntimeException("User with this username is not registered"));

        if (!passwordEncoder.matches(password, client.getPassword())) {
            throw new RuntimeException("Bad credentials");
        }

        return UserDetailsImpl.build(client);
    }

    public JwtPair refreshAccessToken(String refreshToken) {
        String username = jwtTokenProvider.extractUsername(refreshToken);
        Client client = clientRepository.findByUsername(username).orElseThrow(() ->
                new RuntimeException("User with this username is absent"));
        String newAccessToken = jwtTokenProvider.generateAccessToken(UserDetailsImpl.build(client));
        return new JwtPair(newAccessToken, refreshToken);
    }
}

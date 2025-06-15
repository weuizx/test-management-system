package org.evilincorporated.pineapple.security.service;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.domain.entity.User;
import org.evilincorporated.pineapple.domain.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    //TODO тут вообще все переделать
    public User getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof TokenUser tokenUser) {
            String username = tokenUser.getUsername();
            return userRepository.findByUsername(username).orElseThrow(() ->
                    new UsernameNotFoundException("User was not found by username"));
        }
        throw new BadCredentialsException("Invalid access token");
    }

}

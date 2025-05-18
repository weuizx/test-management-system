package org.evilincorporated.pineapple.security;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.repository.ClientRepository;
import org.evilincorporated.pineapple.repository.entity.Client;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with username '%s' not found", username)));

        return UserDetailsImpl.build(client);
    }

}

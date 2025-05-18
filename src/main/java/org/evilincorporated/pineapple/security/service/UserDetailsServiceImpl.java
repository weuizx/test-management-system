package org.evilincorporated.pineapple.security.service;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.domain.repository.ClientRepository;
import org.evilincorporated.pineapple.domain.entity.Client;
import org.evilincorporated.pineapple.domain.entity.ClientAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
                new UsernameNotFoundException("User was not found by username"));
        return User.builder()
                .username(client.getUsername())
                .password(client.getPassword())
                .authorities(client.getAuthorities().stream().map(ClientAuthority::getRole).map(UserRole::name).map(SimpleGrantedAuthority::new).toList())
                .build();
    }
}

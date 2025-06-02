package org.evilincorporated.pineapple.security.service;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.domain.entity.User;
import org.evilincorporated.pineapple.domain.entity.UserAuthority;
import org.evilincorporated.pineapple.domain.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User was not found by username"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities().stream().map(UserAuthority::getRole).map(UserRole::name).map(SimpleGrantedAuthority::new).toList())
                .build();
    }
}

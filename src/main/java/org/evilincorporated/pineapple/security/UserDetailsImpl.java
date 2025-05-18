package org.evilincorporated.pineapple.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.evilincorporated.pineapple.repository.entity.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String patronymic;

    public static UserDetailsImpl build(Client client){
        return new UserDetailsImpl(client.getId(),
                client.getUsername(),
                client.getPassword(),
                client.getName(),
                client.getSurname(),
                client.getPatronymic());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }


}

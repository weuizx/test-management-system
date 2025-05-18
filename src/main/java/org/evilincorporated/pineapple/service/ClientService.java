package org.evilincorporated.pineapple.service;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.SignUpDtoIn;
import org.evilincorporated.pineapple.repository.ClientRepository;
import org.evilincorporated.pineapple.repository.entity.Client;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public String registerUser(SignUpDtoIn signUpDtoIn){
        if(clientRepository.existsByUsername(signUpDtoIn.getUsername())){
            throw new RuntimeException("This username is already taken");
        }

        String hashedPassword = passwordEncoder.encode(signUpDtoIn.getPassword());
        Client client = new Client();
        client.setUsername(signUpDtoIn.getUsername());
        client.setPassword(hashedPassword);
        clientRepository.save(client);

        return client.getUsername();
    }
}

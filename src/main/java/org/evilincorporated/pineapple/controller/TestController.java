package org.evilincorporated.pineapple.controller;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.repository.ClientRepository;
import org.evilincorporated.pineapple.repository.entity.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pineapple/test")
public class TestController {

    private final ClientRepository clientRepository;

    @PostMapping("/create-client")
    public ResponseEntity<String> createClient(@RequestBody String username){
        Client client = new Client();
        client.setUsername(username);
        client.setPassword("");
        clientRepository.save(client);
        return ResponseEntity.ok("");
    }

    @GetMapping("/check-authentication")
    public ResponseEntity<String> checkAuthentication(){
        return ResponseEntity.ok("well well well");
    }

    @GetMapping("/admin/get-phrase")
    public ResponseEntity<String> getPhrase(Principal principal){
        return ResponseEntity.ok("Echkere!");
    }
}

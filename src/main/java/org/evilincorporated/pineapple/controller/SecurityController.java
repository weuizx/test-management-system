package org.evilincorporated.pineapple.controller;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.SignInDtoIn;
import org.evilincorporated.pineapple.controller.dto.SignUpDtoIn;
import org.evilincorporated.pineapple.security.jwt.JwtPair;
import org.evilincorporated.pineapple.service.AuthenticationService;
import org.evilincorporated.pineapple.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pineapple/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final ClientService clientService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDtoIn signUpDtoIn){
        return ResponseEntity.ok("Success. " + clientService.registerUser(signUpDtoIn));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtPair> signIn(@RequestBody SignInDtoIn signInDtoIn){
        return ResponseEntity.ok(authenticationService.signIn(signInDtoIn));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtPair> refreshAccessToken(@RequestParam String refreshToken){
        return ResponseEntity.ok(authenticationService.refreshAccessToken(refreshToken));
    }
}

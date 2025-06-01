package org.evilincorporated.pineapple.controller;

import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.UserDto;
import org.evilincorporated.pineapple.controller.dto.UserDtoIn;
import org.evilincorporated.pineapple.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @PermitAll
    public ResponseEntity<UserDto> createUser(@RequestBody UserDtoIn userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, UserDtoIn userDtoIn) {
        userDtoIn.setId(id);
        return ResponseEntity.ok(userService.updateUser(userDtoIn));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}

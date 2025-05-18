package org.evilincorporated.pineapple.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignUpDtoIn {

    private String username;
    private String email;
    private String password;
}

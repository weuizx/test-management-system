package org.evilincorporated.pineapple.controller.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDtoIn extends UserDto {

    private String password;
}

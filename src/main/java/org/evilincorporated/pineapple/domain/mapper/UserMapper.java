package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.UserDto;
import org.evilincorporated.pineapple.controller.dto.UserDtoIn;
import org.evilincorporated.pineapple.domain.entity.User;
import org.evilincorporated.pineapple.domain.entity.UserAuthority;
import org.evilincorporated.pineapple.security.service.UserRole;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    public abstract User userDtoToUser(UserDto userDto);

    @Mapping(target = "role", source = "authorities", qualifiedByName = "mapRole")
    public abstract UserDto userToUserDto(User user);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    public abstract User userDtoInToUser(UserDtoIn userDtoIn);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    public abstract User updateUserFromDto(UserDtoIn source, @MappingTarget User target);

    @Named("encodePassword")
    protected String encodePassword(String password) {
        if (password.isBlank()) {
            return null;
        }
        return "{noop}" + password;
    }

    @Named("mapRole")
    protected UserRole mapRole(Set<UserAuthority> authorities) {
        if (authorities != null && !authorities.isEmpty()) {
            return authorities.stream().findFirst().map(UserAuthority::getRole).orElse(null);
        }
        return null;
    }

    @Named("mapRoleDescription")
    protected String mapRoleDescription(Set<UserAuthority> authorities) {
        if (authorities != null && !authorities.isEmpty()) {
            return authorities.stream().findFirst().map(UserAuthority::getRole).map(UserRole::getDescription).orElse(null);
        }
        return null;
    }

}

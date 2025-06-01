package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.UserDto;
import org.evilincorporated.pineapple.controller.dto.UserDtoIn;
import org.evilincorporated.pineapple.domain.entity.User;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    public abstract User userDtoToUser(UserDto userDto);

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
}

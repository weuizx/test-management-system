package org.evilincorporated.pineapple.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.UserDto;
import org.evilincorporated.pineapple.controller.dto.UserDtoIn;
import org.evilincorporated.pineapple.domain.entity.User;
import org.evilincorporated.pineapple.domain.mapper.UserMapper;
import org.evilincorporated.pineapple.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

//TODO сделать чтобы пользователь мог процессить ток себя
@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found by id = %s";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto createUser(UserDtoIn userDtoIn) {
        //TODO добавить проверку пароля (хотя бы не null)
        //TODO имя проверять на существующее
        User user = userRepository.save(userMapper.userDtoInToUser(userDtoIn));
        return userMapper.userToUserDto(user);
    }

    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE.formatted(id)));
        return userMapper.userToUserDto(user);
    }

    public UserDto updateUser(UserDtoIn userDtoIn) {
        //TODO имя проверять на существующее
        User user = userRepository.findById(userDtoIn.getId()).orElseThrow(
                () -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE.formatted(userDtoIn.getId())));
        user = userMapper.updateUserFromDto(userDtoIn, user);
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE.formatted(id)));
        userRepository.delete(user);
    }
}

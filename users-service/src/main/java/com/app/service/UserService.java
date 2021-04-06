package com.app.service;

import com.app.dto.CreateUserDto;
import com.app.dto.GetUserDto;
import com.app.exception.UsersServiceException;
import com.app.mapper.Mappers;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Long add(CreateUserDto createUserDto) {
        if (createUserDto == null) {
            throw new UsersServiceException("create user dto is null");
        }

        if (userRepository.findByUsername(createUserDto.getUsername()).isPresent()) {
            throw new UsersServiceException("username already exists");
        }

        var user = Mappers.fromCreateUserDtoToUser(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

        return userRepository.save(user).getId();
    }

    public GetUserDto findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .map(Mappers::fromUserToGetUserDto)
                .orElseThrow(() -> new UsersServiceException("cannot get user with username"));
    }

    public GetUserDto findById(Long id) {
        return userRepository
                .findById(id)
                .map(Mappers::fromUserToGetUserDto)
                .orElseThrow(() -> new UsersServiceException("cannot get user with username"));
    }
}

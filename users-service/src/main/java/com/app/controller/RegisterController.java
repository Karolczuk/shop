package com.app.controller;

import com.app.dto.CreateUserDto;
import com.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @PostMapping
    public Long register(@RequestBody CreateUserDto createUserDto) {
        return userService.add(createUserDto);
    }

}

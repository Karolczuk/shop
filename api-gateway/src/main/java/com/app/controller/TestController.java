package com.app.controller;

import com.app.dto.GetUserDto;
import com.app.proxy.FindUserProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final FindUserProxy findUserProxy;

    @GetMapping("/username/{username}")
    public GetUserDto findByUsername(@PathVariable String username) {
        return findUserProxy.findByUsername(username);
    }

    @GetMapping("/id/{id}")
    public GetUserDto findById(@PathVariable Long id) {
        return findUserProxy.findById(id);
    }
}

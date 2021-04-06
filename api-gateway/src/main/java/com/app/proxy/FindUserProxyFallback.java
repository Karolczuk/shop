package com.app.proxy;

import com.app.dto.GetUserDto;
import org.springframework.stereotype.Component;

@Component
public class FindUserProxyFallback implements FindUserProxy {
    @Override
    public GetUserDto findByUsername(String username) {
        return GetUserDto.builder().build();
    }

    @Override
    public GetUserDto findById(Long id) {
        return GetUserDto.builder().build();
    }
}

package com.app.proxy;

import com.app.dto.GetUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-service", fallback = FindUserProxyFallback.class)
public interface FindUserProxy {
    @GetMapping("/find/username/{username}")
    GetUserDto findByUsername(@PathVariable String username);

    @GetMapping("/find/id/{id}")
    GetUserDto findById(@PathVariable Long id);
}

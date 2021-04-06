package com.app.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tokens {
    private String accessToken;
    private String refreshToken;
    private Set<String> roles;
}

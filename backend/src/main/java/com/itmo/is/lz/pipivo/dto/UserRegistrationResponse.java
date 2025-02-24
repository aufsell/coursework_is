package com.itmo.is.lz.pipivo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegistrationResponse {
    private Long id;
    private String username;
    private String role;
    private String token;
}
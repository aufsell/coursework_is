package com.itmo.is.lz.pipivo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequestDTO {
    private String username;
    private String password;
    private String recaptcha;
}

package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.SignInRequestDTO;
import com.itmo.is.lz.pipivo.dto.SignUpRequestDTO;
import com.itmo.is.lz.pipivo.dto.UserRegistrationResponse;
import com.itmo.is.lz.pipivo.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public UserRegistrationResponse register(@RequestBody SignUpRequestDTO request) {
        return authService.signUp(request);
    }

    @PostMapping("/signin")
    public UserRegistrationResponse login(@RequestBody SignInRequestDTO request) {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserRegistrationResponse response = authService.signIn(request);
        return new ResponseEntity<>(response, HttpStatus.OK).getBody();
    }
}
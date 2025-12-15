package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.SignInRequestDTO;
import com.itmo.is.lz.pipivo.dto.SignUpRequestDTO;
import com.itmo.is.lz.pipivo.dto.UserRegistrationResponse;
import com.itmo.is.lz.pipivo.service.AuthService;
import com.itmo.is.lz.pipivo.service.ReCaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private ReCaptchaService reCaptchaService;

    @PostMapping("/signup")
    public ResponseEntity<UserRegistrationResponse> register(@RequestBody SignUpRequestDTO request) {
            log.info("Signup attempt username={}", request.getUsername());
            UserRegistrationResponse response = authService.signUp(request);
            log.info("Signup successful username={}", response.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserRegistrationResponse> login(@RequestBody SignInRequestDTO request,
                                                          HttpServletRequest httpRequest) {
        log.info("Login attempt username={}", request.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        UserRegistrationResponse response = authService.signIn(request, httpRequest);
        log.info("Login successful username={}", request.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("You have successfully logout the system");
    }
}

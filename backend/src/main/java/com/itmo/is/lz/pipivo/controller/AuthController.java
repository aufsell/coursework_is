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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private ReCaptchaService reCaptchaService;

    @PostMapping("/signup")
    public ResponseEntity<UserRegistrationResponse> register(@RequestBody SignUpRequestDTO request) {

        System.out.print("user" + request.getUsername() +"pwd"+ request.getPassword() + "req" + request.getRecaptcha());
//        if (reCaptchaService.verifyRecaptcha(request.getRecaptcha())) {
            UserRegistrationResponse response = authService.signUp(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        }
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<UserRegistrationResponse> login(@RequestBody SignInRequestDTO request,
                                                          HttpServletRequest httpRequest) {

//        if (reCaptchaService.verifyRecaptcha(request.getRecaptcha())) {


            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            UserRegistrationResponse response = authService.signIn(request, httpRequest);
            return ResponseEntity.ok(response);
//        }
//        else{
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
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

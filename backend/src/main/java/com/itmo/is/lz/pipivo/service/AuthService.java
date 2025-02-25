package com.itmo.is.lz.pipivo.service;

import com.itmo.is.lz.pipivo.dto.SignInRequestDTO;
import com.itmo.is.lz.pipivo.dto.SignUpRequestDTO;
import com.itmo.is.lz.pipivo.dto.UserRegistrationResponse;
import com.itmo.is.lz.pipivo.model.User;
import com.itmo.is.lz.pipivo.repository.RoleRepository;
import com.itmo.is.lz.pipivo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserService userService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    public UserRegistrationResponse signUp(SignUpRequestDTO request) {
        if (userRepository.findByName(request.getUsername()).isPresent()) {
            throw new RuntimeException("User with this name already exists");
        }
        User user = new User();
        Long roleId = 1L;
        user.setName(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found")));
        userService.create(user);

        return new UserRegistrationResponse(user.getId(), user.getName(), user.getRole().getName(), null);
    }


    public UserRegistrationResponse signIn(SignInRequestDTO request, HttpServletRequest httpRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        User user = userRepository.findByName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserRegistrationResponse(user.getId(), user.getName(), user.getRole().getName(), null);
    }
}
package com.itmo.is.lz.pipivo.service;

import com.itmo.is.lz.pipivo.dto.SignInRequestDTO;
import com.itmo.is.lz.pipivo.dto.SignUpRequestDTO;
import com.itmo.is.lz.pipivo.dto.UserRegistrationResponse;
import com.itmo.is.lz.pipivo.model.User;
import com.itmo.is.lz.pipivo.repository.RoleRepository;
import com.itmo.is.lz.pipivo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RoleRepository roleRepository, JwtService jwtService, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
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

    public UserRegistrationResponse signIn(SignInRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtService.generateJwtToken(request.getUsername());
        User user = userRepository.findByName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("User: " + user.getName() + " Role: " + user.getRole().getName());

        return new UserRegistrationResponse(user.getId(), user.getName(), user.getRole().getName(), jwtToken);
    }
}
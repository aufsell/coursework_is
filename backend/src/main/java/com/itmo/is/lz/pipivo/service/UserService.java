package com.itmo.is.lz.pipivo.service;

import com.itmo.is.lz.pipivo.model.User;
import com.itmo.is.lz.pipivo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDetailsService userDetailsService() {
        return this::loadUserByUsername;
    }

    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), new ArrayList<>());
    }

    public User getByUsername(String username) {
        return userRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }

    public User create(User user) {
        if (userRepository.existsByName((user.getName()))) {
            throw new ResourceNotFoundException("User with this name already exists");
        }
        return save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}

package com.itmo.is.lz.pipivo.service;

import com.itmo.is.lz.pipivo.model.User;
import com.itmo.is.lz.pipivo.repository.FavouriteBeerRepository;
import com.itmo.is.lz.pipivo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FavouriteBeerRepository favouriteBeerRepository;

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

    @Transactional
    public void addBeerToFavourite(Long beerId) {
        String username = getCurrentUsername();
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (favouriteBeerRepository.exists(user.getId(), beerId)) {
            System.out.println("Beer "+ beerId+ " is already in favourites for user "+ user.getId());
            return;
        }

        favouriteBeerRepository.addBeerToFavourites(user.getId(), beerId);
        System.out.println("Beer "+ beerId+ " added in favourites for user "+ user.getId());
    }

    @Transactional
    public void removeBeerFromFavourite(Long beerId) {
        String username = getCurrentUsername();
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!favouriteBeerRepository.exists(user.getId(), beerId)) {
            System.out.println("Beer "+ beerId+ " is already isn't in favourites for user "+ user.getId());
            return;
        }

        favouriteBeerRepository.removeBeerFromFavourites(user.getId(), beerId);
        System.out.println("Beer "+ beerId+ " deleted from favourites for user "+ user.getId());
    }

    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }


    public boolean checkCurrentUser(Long userId) {
        String userName = getCurrentUsername();
        System.out.println("Current user: "+ userName);
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId().equals(userId);
    }
}

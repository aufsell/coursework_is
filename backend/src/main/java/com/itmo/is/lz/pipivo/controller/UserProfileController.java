package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.dto.ProfileDTO;
import com.itmo.is.lz.pipivo.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class UserProfileController {

    private final ProfileService profileService;

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long userId) {
        ProfileDTO profileDTO = profileService.getProfile(userId);
        return ResponseEntity.status(HttpStatus.OK).body(profileDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateProfile(@PathVariable Long userId, @RequestBody ProfileDTO profileDTO) {
        profileService.updateProfile(userId, profileDTO);
        return ResponseEntity.ok().build();
    }

//    На кого подписан User
    @GetMapping("/subscribers/{userId}")
    public ResponseEntity<List<ProfileDTO>> getSubscribers(@PathVariable Long userId) {
        List<ProfileDTO> subscribers = profileService.getSubscribers(userId);
        return ResponseEntity.ok(subscribers);
    }

    @GetMapping("/subscribers/{userId}/count")
    public ResponseEntity<Long> getSubscribersCount(@PathVariable Long userId) {
        Long count = profileService.getSubscribersCount(userId);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/subscribers/{SubscribedUserId}")
    public ResponseEntity<Void> subscribe(@PathVariable Long SubscribedUserId) {
        profileService.subscribe(SubscribedUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/subscribers/{SubscribedUserId}")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long SubscribedUserId) {
        profileService.unsubscribe(SubscribedUserId);
        return ResponseEntity.ok().build();
    }
}

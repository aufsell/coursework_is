package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.dto.ProfileDTO;
import com.itmo.is.lz.pipivo.service.ProfileService;
import com.itmo.is.lz.pipivo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class UserProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long userId) {
        log.info("Get user profile. userId={}", userId);
        ProfileDTO profileDTO = profileService.getProfile(userId);
        log.debug("User profile fetched. userId={}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(profileDTO);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateProfile(@PathVariable Long userId,
                                              @RequestBody ProfileDTO profileDTO) {
        log.info("Update user profile attempt. userId={}", userId);
        if (!userService.checkCurrentUser(userId)) {
            log.warn("Forbidden profile update attempt. userId={}", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        profileService.updateProfile(userId, profileDTO);
        log.info("User profile updated successfully. userId={}", userId);
        return ResponseEntity.ok().build();
    }


    // На кого подписан User
    @GetMapping("/subscribers/{userId}")
    public ResponseEntity<List<ProfileDTO>> getSubscribers(@PathVariable Long userId) {
        log.info("Get subscribers. userId={}", userId);
        List<ProfileDTO> subscribers = profileService.getSubscribers(userId);
        log.debug("Subscribers fetched. userId={}, count={}", userId, subscribers.size());
        return ResponseEntity.ok(subscribers);
    }


    @GetMapping("/subscribers/{userId}/count")
    public ResponseEntity<Long> getSubscribersCount(@PathVariable Long userId) {
        log.info("Get subscribers count. userId={}", userId);
        Long count = profileService.getSubscribersCount(userId);
        log.debug("Subscribers count fetched. userId={}, count={}", userId, count);
        return ResponseEntity.ok(count);
    }


    @PostMapping("/subscribers/{SubscribedUserId}")
    public ResponseEntity<Void> subscribe(@PathVariable Long SubscribedUserId) {
        log.info("Subscribe to user. subscribedUserId={}", SubscribedUserId);
        profileService.subscribe(SubscribedUserId);
        log.info("Subscription successful. subscribedUserId={}", SubscribedUserId);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/subscribers/{SubscribedUserId}")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long SubscribedUserId) {
        log.info("Unsubscribe from user. subscribedUserId={}", SubscribedUserId);
        profileService.unsubscribe(SubscribedUserId);
        log.info("Unsubscription successful. subscribedUserId={}", SubscribedUserId);
        return ResponseEntity.ok().build();
    }


    // Кто подписан на User
    @GetMapping("/subscribed/{userId}")
    public ResponseEntity<List<ProfileDTO>> getSubscribed(@PathVariable Long userId) {
        log.info("Get subscribed users. userId={}", userId);
        List<ProfileDTO> subscribed = profileService.getSubscribed(userId);
        log.debug("Subscribed users fetched. userId={}, count={}", userId, subscribed.size());
        return ResponseEntity.ok(subscribed);
    }


    @GetMapping("/subscribed/{userId}/count")
    public ResponseEntity<Long> getSubscribedCount(@PathVariable Long userId) {
        log.info("Get subscribed count. userId={}", userId);
        Long count = profileService.getSubscribedCount(userId);
        log.debug("Subscribed count fetched. userId={}, count={}", userId, count);
        return ResponseEntity.ok(count);
    }


    @GetMapping("/subscribed/{userId}/isSubscribed")
    public ResponseEntity<Boolean> isSubscribed(@PathVariable Long userId) {
        log.debug("Check subscription status. targetUserId={}", userId);
        Boolean isSubscribed = profileService.isSubscribed(userId);
        return ResponseEntity.ok(isSubscribed);
    }


    @PostMapping("/{userId}/avatar")
    public ResponseEntity<Void> updateAvatar(@PathVariable Long userId,
                                             @RequestParam("file") MultipartFile avatar) {
        log.info("Update avatar attempt. userId={}, fileName={}, fileSize={}",
                userId, avatar.getOriginalFilename(), avatar.getSize());
        if (!userService.checkCurrentUser(userId)) {
            log.warn("Forbidden avatar update attempt. userId={}", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        profileService.updateAvatar(userId, avatar);
        log.info("Avatar updated successfully. userId={}", userId);
        return ResponseEntity.ok().build();
    }

}

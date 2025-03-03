package com.itmo.is.lz.pipivo.service;

import com.itmo.is.lz.pipivo.dto.ProfileDTO;
import com.itmo.is.lz.pipivo.model.User;
import com.itmo.is.lz.pipivo.repository.SubscribedUsersRepository;
import com.itmo.is.lz.pipivo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final SubscribedUsersRepository subscribedUsersRepository;

    public ProfileDTO getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    private ProfileDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setProfileId(user.getId());
        profileDTO.setName(user.getName());
        profileDTO.setFirstName(user.getFirstName());
        profileDTO.setLastName(user.getLastName());
        profileDTO.setCountry(user.getCountry());
        profileDTO.setAvatarPath(user.getAvatarPath());
        return profileDTO;
    }

    public ProfileDTO updateProfile(Long userId, ProfileDTO profileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(profileDTO.getFirstName());
        user.setLastName(profileDTO.getLastName());
        user.setCountry(profileDTO.getCountry());
        user.setEmail(profileDTO.getEmail());
        user.setPrefLanguage(profileDTO.getPrefLanguage());
        userRepository.save(user);
        return convertToDTO(user);
    }

    public List<ProfileDTO> getSubscribers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Long> subscribersId = subscribedUsersRepository.getFollowedUserIdsByUserId(userId);
        List<User> subscribers = userRepository.findAllById(subscribersId);
        return subscribers.stream().map(this::convertToDTO).collect(Collectors.toList());

    }
}

package com.itmo.is.lz.pipivo.service;

import com.itmo.is.lz.pipivo.dto.ProfileDTO;
import com.itmo.is.lz.pipivo.model.User;
import com.itmo.is.lz.pipivo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserService userService;
    private final UserRepository userRepository;

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
}

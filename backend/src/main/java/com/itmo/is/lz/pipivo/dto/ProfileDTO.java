package com.itmo.is.lz.pipivo.dto;

import com.itmo.is.lz.pipivo.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProfileDTO {
    private Long profileId;

    @NotNull(message = "Name cannot be null")
    private String name;

    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String prefLanguage;
    private String avatarPath;

    public ProfileDTO(String name,String firstName, String lastName, String email,String prefLanguage, String country, String avatarPath) {
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.prefLanguage = prefLanguage;
        this.country = country;
        this.avatarPath = avatarPath;
    }

    public ProfileDTO(User user) {
        this.profileId = user.getId();
        this.name = user.getName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.prefLanguage = user.getPrefLanguage();
        this.country = user.getCountry();
        this.avatarPath = user.getAvatarPath();

    }
}

package com.itmo.is.lz.pipivo.dto;

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
}

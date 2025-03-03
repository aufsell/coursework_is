package com.itmo.is.lz.pipivo.dto;

import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long reviewId;

    private Float rating;

    private String comment;

    private Long beerId;

    private String beerImagePath;

    private LocalDateTime created_at;

    private ProfileDTO profile;
}

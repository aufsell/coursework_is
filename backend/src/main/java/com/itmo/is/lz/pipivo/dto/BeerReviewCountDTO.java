package com.itmo.is.lz.pipivo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BeerReviewCountDTO {
    private Integer beerId;
    private String beerName;
    private String imagePath;
    private Double price;
    private Double averageRating;
    private String country;
    private Integer reviewCount;
}

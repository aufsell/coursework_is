package com.itmo.is.lz.pipivo.dto;
import com.itmo.is.lz.pipivo.model.FermentationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BeerDTO {

    private Long beerId;

    @NotNull(message = " beer name not be null")
    private String name;

    @NotNull (message = "Beer's price cannot be bull")
    private Double price;

    @NotNull (message = "Beer's value cannot be bull")
    private Double volume;

    @NotNull (message = "Beer's fermentation type cannot be bull")
    private FermentationType fermentationType;

    private Double averageRating;

    @NotNull (message = "Beer's srm cannot be bull")
    private Long srm;

    @NotNull (message = "Beer's ibu cannot be bull")
    private Long ibu;

    @NotNull (message = "Beer's abv cannot be bull")
    private Long abv;

    @NotNull (message = "Beer's og cannot be bull")
    private Long og;

    private String country;

    private String imagePath;

    private Double averageRating;

    public BeerDTO(String name, Double price, Double volume, FermentationType fermentationType, Long srm, Long ibu, Long abv, Long og, String country, String imagePath) {
        this.name = name;
        this.price = price;
        this.volume = volume;
        this.fermentationType = fermentationType;
        this.srm = srm;
        this.ibu = ibu;
        this.abv = abv;
        this.og = og;
        this.country = country;
        this.imagePath = imagePath;
    }

}

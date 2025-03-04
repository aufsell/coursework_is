package com.itmo.is.lz.pipivo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name="beers")
public class Beer {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "name", nullable = false)
    @NotNull (message = "Beer's name cannot be bull")
    @NotBlank (message = "Beer's name cannot be blank")
    private String name;

    @Column (name = "price", nullable = false)
    @NotNull (message = "Beer's price cannot be bull")
    @NotBlank (message = "Beer's price cannot be blank")
    @Positive (message = "Beer's price should be > 0")
    private Double price;

    @Column (name = "volume", nullable = false)
    @NotNull (message = "Beer's value cannot be bull")
    @NotBlank (message = "Beer's value cannot be blank")
    @Positive (message = "Beer's value should be > 0")
    private Double volume;

    @ManyToMany(mappedBy = "favouriteBeers")
    private Set<User> usersFavourite;

    @ManyToMany(mappedBy = "recommendationBeers")
    private Set<User> usersRecommendation;

    @ManyToOne
    @JoinColumn(name="fermentation_type", nullable = false)
    @NotNull (message = "Beer's fermentation type cannot be bull")
    private FermentationType fermentationType;

    @Column (name = "srm", nullable = false)
    @NotNull (message = "Beer's srm cannot be bull")
    @NotBlank (message = "Beer's srm cannot be blank")
    @Positive (message = "Beer's srm should be > 0")
    private Long srm;

    @Column (name = "ibu", nullable = false)
    @NotNull (message = "Beer's ibu cannot be bull")
    @NotBlank (message = "Beer's ibu cannot be blank")
    @Positive (message = "Beer's ibu should be > 0")
    private Long ibu;

    @Column (name = "abv", nullable = false)
    @NotNull (message = "Beer's abv cannot be bull")
    @NotBlank (message = "Beer's abv cannot be blank")
    @Positive (message = "Beer's abv should be > 0")
    private Long abv;

    @Column (name = "og", nullable = false)
    @NotNull (message = "Beer's og cannot be bull")
    @NotBlank (message = "Beer's og cannot be blank")
    @Positive (message = "Beer's og should be > 0")
    private Long og;

    @Column (name = "country")
    private String country;

    @Column (name = "image_path")
    private String imagePath;

    @Column (name="last_updated", nullable = false, updatable = false, insertable = false)
    private java.sql.Timestamp lastUpdated;

}

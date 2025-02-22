package com.itmo.is.lz.pipivo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="tasteprofiles")
@AllArgsConstructor
@NoArgsConstructor
public class TasteProfile {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private User user;

    @Positive(message = "IBU preference must be positive")
    @Max(100)
    @Column(name="ibu_pref")
    private Long ibuPreference;

    @Positive(message = "ABV preference must be positive")
    @Max(100)
    @Column(name="abv_pref")
    private Long abvPreference;

    @Positive(message = "SRM preference must be positive")
    @Max(100)
    @Column(name="srm_pref")
    private Long srmPreference;

    @Positive(message = "OG preference must be positive")
    @Max(100)
    @Column(name="og_pref")
    private Long ogPreference;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fermentation_type", referencedColumnName = "id")
    private FermentationType fermentationType;

    @Positive
    @Column (name = "price")
    private Long pricePreference;
}

package com.itmo.is.lz.pipivo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table (name= "fermentation_types")
public class FermentationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotNull(message = "Fermentation type name cannot be null")
    @NotBlank(message = "Fermentation type name cannot be blank")
    private String name;
}

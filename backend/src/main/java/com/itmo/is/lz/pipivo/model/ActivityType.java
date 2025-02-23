package com.itmo.is.lz.pipivo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name="activity_types")
public class ActivityType {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    @NotNull( message = "Activity type name cannot be null")
    @NotBlank( message = "Activity type name cannot be blank")
    private String name;
}

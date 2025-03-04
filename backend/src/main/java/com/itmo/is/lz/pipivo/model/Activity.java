package com.itmo.is.lz.pipivo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name="activities")
public class Activity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    @NotNull(message = "User cannot be null")
    private User user;


    @ManyToOne
    @JoinColumn(name="type_id")
    @NotNull(message = "Activity type cannot be null")
    private ActivityType activityType;

    @OneToOne
    @JoinColumn(name="review_id")
    @NotNull(message = "Review cannot be null")
    private Review review;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @PrePersist
    protected void onCreate() {
        created_at = new Date();
    }
}

package com.itmo.is.lz.pipivo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull( message = "User name cannot be null")
    @NotBlank( message = "User name cannot be blank")
    @Column(name="username", nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "notification_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id")
    )
    private Set<Notification> sharedNotifications;

    @ManyToMany
    @JoinTable(
            name = "review_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id")
    )
    private Set<Review> reviews;

    @ManyToMany
    @JoinTable(
            name = "subscribed_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_user")
    )
    private Set<User> subscriptions;

    @ManyToMany(mappedBy = "subscriptions")
    private Set<User> subscribers;

    @ManyToMany
    @JoinTable(
            name="favourite_beer",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "beer_id")
    )
    private Set<Beer> favouriteBeers;

    @ManyToMany
    @JoinTable(
            name="recommendation",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="beer_id")
    )
    private Set<Beer> recommendationBeers;


//    @NotBlank( message = "User email cannot be blank")
    @Column(name="email")
    private String email;

    @NotNull( message = "User password cannot be null")
    @NotBlank( message = "User password cannot be blank")
    @Column(name="password", nullable = false)
    private String password;

//    @NotBlank( message = "User first name cannot be blank")
    @Column(name="first_name")
    private String firstName;

//    @NotBlank( message = "User last name cannot be blank")
    @Column(name="last_name")
    private String lastName;

    @Column(name="country")
    private String Country;

    @Column(name="preferred_language")
    private String prefLanguage;

    @NotNull(message = "User role cannot be null")
    @ManyToOne
    @JoinColumn(name = "roles_id", referencedColumnName = "id")
    private Role role;

    @Column(name = "avatar_path")
    private String avatarPath;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @PrePersist
    protected void onCreate() {
        created_at = new Date();
    }
}

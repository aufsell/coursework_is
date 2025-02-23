package com.itmo.is.lz.pipivo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    @NotNull(message = "User cannot be null")
    private User user;

    @ManyToMany(mappedBy = "sharedNotifications")
    private Set<User> users;

    @OneToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;


    @NotNull(message = "message cannot be null")
    @NotBlank(message = "message cannot be blank")
    @Column(name="message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @PrePersist
    protected void onCreate() {
        status = "unread";
    }
}

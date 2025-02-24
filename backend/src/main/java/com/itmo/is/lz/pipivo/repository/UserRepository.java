package com.itmo.is.lz.pipivo.repository;

import com.itmo.is.lz.pipivo.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String username);

    boolean existsByName(@NotNull(message = "User name cannot be null") @NotBlank(message = "User name cannot be blank") String attr0);
}

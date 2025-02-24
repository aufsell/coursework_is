package com.itmo.is.lz.pipivo.repository;

import com.itmo.is.lz.pipivo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String roleUser);
}

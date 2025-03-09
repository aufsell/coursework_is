package com.itmo.is.lz.pipivo.repository;

import com.itmo.is.lz.pipivo.model.TasteProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TasteProfileRepository extends JpaRepository<TasteProfile, Long> {
    TasteProfile findByUserId(Long id);
}

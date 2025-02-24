package com.itmo.is.lz.pipivo.repository;


import com.itmo.is.lz.pipivo.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BeerRepository extends JpaRepository<Beer, Long>, JpaSpecificationExecutor<Beer> {
}

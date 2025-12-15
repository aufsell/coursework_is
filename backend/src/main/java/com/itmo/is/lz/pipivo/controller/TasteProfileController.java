package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.service.TasteProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/me/recommendations")
public class TasteProfileController {

    private final TasteProfileService tasteProfileService;

    public TasteProfileController(TasteProfileService tasteProfileService) {
        this.tasteProfileService = tasteProfileService;
    }

    @GetMapping
    public ResponseEntity<List<BeerDTO>> getRecomendatedBeers() {
        log.info("Get recommended beers for current user");
        List<BeerDTO> beers = tasteProfileService.getRecomendatedBeers();
        log.debug("Recommended beers fetched. count={}", beers.size());
        return ResponseEntity.ok(beers);
    }
}

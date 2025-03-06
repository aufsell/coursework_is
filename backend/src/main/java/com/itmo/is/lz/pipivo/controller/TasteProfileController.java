package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.service.TasteProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasteProfile")
public class TasteProfileController {

    private final TasteProfileService tasteProfileService;

    public TasteProfileController(TasteProfileService tasteProfileService) {
        this.tasteProfileService = tasteProfileService;
    }

    @GetMapping
    public ResponseEntity<List<BeerDTO>> getRecomendatedBeers() {
        List<BeerDTO> beers = tasteProfileService.getRecomendatedBeers();
        return ResponseEntity.ok(beers);
    }
}

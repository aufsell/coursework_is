package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.BeerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasteProfile")
public class TasteProfileController {

    @GetMapping
    public ResponseEntity<List<BeerDTO>> getRecomendatedBeers() {
        List<BeerDTO> beers = null;
        return ResponseEntity.ok(beers);
    }
}

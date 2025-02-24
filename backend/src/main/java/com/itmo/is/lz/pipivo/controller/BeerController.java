package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.service.BeerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final  BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }


    @GetMapping ("/{beerId}")
    public ResponseEntity<BeerDTO> grantBeerId(@PathVariable Long beerId) {
        BeerDTO beerDTO = beerService.getBeerById(beerId);
        return ResponseEntity.status(HttpStatus.OK).body(beerDTO);
    }

    @GetMapping
    public ResponseEntity<Page<BeerDTO>> getBeers(Pageable pageable, @RequestParam Map<String, String> filters) {
        Page<BeerDTO> beers = beerService.getBeers(pageable, filters);
        return ResponseEntity.ok(beers);
    }
}

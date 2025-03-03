package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.service.BeerService;
import com.itmo.is.lz.pipivo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favourite")
public class FavouriteController {

    private final UserService userService;
    private final BeerService beerService;

    public FavouriteController(UserService userService, BeerService beerService) {
        this.userService = userService;
        this.beerService = beerService;
    }
    @PostMapping("/add/{beerId}")
    public ResponseEntity<Void> addBeerToFavourite(@PathVariable Long beerId) {
        userService.addBeerToFavourite(beerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("remove/{beerId}")
    public ResponseEntity<Void> removeBeerFromFavourite(@PathVariable Long beerId) {
        userService.removeBeerFromFavourite(beerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<BeerDTO>> getFavouriteByUserId(@PathVariable Long userId) {
        List<BeerDTO> beers = beerService.getFavouriteByUserId(userId);
        return ResponseEntity.ok(beers);
    }

}

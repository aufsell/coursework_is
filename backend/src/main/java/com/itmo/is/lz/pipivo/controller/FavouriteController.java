package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.model.User;
import com.itmo.is.lz.pipivo.service.BeerService;
import com.itmo.is.lz.pipivo.service.TasteProfileService;
import com.itmo.is.lz.pipivo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/me/favourite")
public class FavouriteController {

    private final UserService userService;
    private final BeerService beerService;
    private final TasteProfileService tasteProfileService;

    public FavouriteController(UserService userService, BeerService beerService, TasteProfileService tasteProfileService) {
        this.userService = userService;
        this.beerService = beerService;
        this.tasteProfileService = tasteProfileService;
    }
    @PostMapping("/add/{beerId}")
    public ResponseEntity<Void> addBeerToFavourite(@PathVariable Long beerId) {
        log.info("Adding beer to favourites");
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
        log.info("Get favourites by userId={}", userId);
        List<BeerDTO> beers = beerService.getFavouriteByUserId(userId);
        return ResponseEntity.ok(beers);
    }

    @GetMapping("/{beerId}/isFavourite")
    public ResponseEntity<Boolean> isFavourite(@PathVariable Long beerId) {
        boolean isFavourite = userService.isFavourite(beerId);
        return ResponseEntity.ok(isFavourite);
    }

}

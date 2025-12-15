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
        String username = userService.getCurrentUsername();
        User user = userService.getByUsername(username);
        log.info("Add beer to favourites. userId={}, beerId={}", user.getId(), beerId);
        userService.addBeerToFavourite(beerId);

        tasteProfileService.updateTasteProfileByFavourite(user.getId(), beerId);
        System.out.println("Taste profile updated for user "+ user.getId());

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
        String username = userService.getCurrentUsername();
        User user = userService.getByUsername(username);
        log.debug("Check if beer is favourite. userId={}, beerId={}", user.getId(), beerId);
        boolean isFavourite = userService.isFavourite(user.getId(), beerId);
        return ResponseEntity.ok(isFavourite);
    }

}

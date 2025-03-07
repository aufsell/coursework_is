package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.model.User;
import com.itmo.is.lz.pipivo.service.BeerService;
import com.itmo.is.lz.pipivo.service.TasteProfileService;
import com.itmo.is.lz.pipivo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favourite")
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
        List<BeerDTO> beers = beerService.getFavouriteByUserId(userId);
        return ResponseEntity.ok(beers);
    }

}

package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/favourite")
public class FavouriteController {

    private final UserService userService;

    public FavouriteController(UserService userService) {
        this.userService = userService;
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

}

package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.config.JwtService;


import com.example.demo.entity.dto.GameDTO;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

@Operation(summary = "Add game to list", description = "Add game to user's list")
@PostMapping("/add")
public ResponseEntity<?> addGameToList(@RequestParam Long gameId, 
        @CookieValue(name = "jwt", required = false) String token) {

    if (token == null || token.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided");
    }

    UUID userUuidFromToken = jwtService.extractUuid(token);
    if (userUuidFromToken == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }

    userService.addGame(userUuidFromToken, gameId); 
    return ResponseEntity.status(HttpStatus.OK).body(null);
}

@GetMapping("/mes-jeux")
public ResponseEntity<List<GameDTO>> getListGames(@CookieValue(name = "jwt", required = false) String token) {

    if (token == null || token.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
    }

    UUID userUuidFromToken = jwtService.extractUuid(token);
    if (userUuidFromToken == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
    }

    List<GameDTO> games = userService.getList(userUuidFromToken);
    if (games == null || games.isEmpty()) {

        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
    }


    return new ResponseEntity<>(games, HttpStatus.OK);
}



@Operation(summary = "Delete game from list", description = "Delete a game from the user's list")
@DeleteMapping()
public ResponseEntity<List<GameDTO>> deleteGame(@RequestParam Long gameId, 
        @CookieValue(name = "jwt", required = false) String token) {


    if (token == null || token.isEmpty()) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
    }


    UUID userUuidFromToken = jwtService.extractUuid(token);
    if (userUuidFromToken == null) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
    }


    List<GameDTO> updatedGames = userService.deleteGame(userUuidFromToken, gameId);
    return ResponseEntity.ok(updatedGames); 
}

}

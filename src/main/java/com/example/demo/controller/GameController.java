package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.dto.GameDTO;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addGameToList(@RequestParam Long gameId,
            @RequestHeader("Authorization") String token) {
        Long userId = userService.getIdInToken(token);
        userService.addGame(userId, gameId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("")
    public ResponseEntity<List<GameDTO>> getListGames(@RequestParam Long user_id,
            @RequestHeader("Authorization") String token) {
        Long userIdToken = userService.getIdInToken(token);
        if (!user_id.equals(userIdToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<GameDTO> games = userService.getList(user_id);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<List<GameDTO>> deleteGame(Long gameId,
            @RequestHeader("Authorization") String token) {
        Long userIdToken = userService.getIdInToken(token);

        List<GameDTO> updatedGames = userService.deleteGame(userIdToken, gameId);
        return ResponseEntity.ok(updatedGames);

    }
}

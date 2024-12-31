package com.example.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.JwtService;
import com.example.demo.entity.Game;
import com.example.demo.entity.User;
import com.example.demo.entity.dto.GameDTO;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.UserRepository;
import com.github.slugify.Slugify;

import java.util.stream.Collectors;

@Service
public class UserService {

    private Slugify slugify = Slugify.builder().build();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private JwtService jwtService;


    private Game game = new Game();

    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    public User getOneByUuid(UUID uuid) {
        return this.userRepository.findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("User not found with UUID: " + uuid));
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByUuid(UUID uuid) {
        Optional<User> userOptional = userRepository.findByUuid(uuid);
        return userOptional.orElseThrow(() -> new RuntimeException("User not found with UUID: " + uuid));
    }

    public User getOneBySlug(String slug) {
        return this.userRepository.findBySlug(slug);
    }

    public User createUser(User user) {
        user.setSlug(slugify.slugify(user.getUsername()));
        return this.userRepository.save(user);
    }

    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User updateUser(UUID uuid, User user) {
        User retrievedUser = this.userRepository.findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("User not found with UUID: " + uuid));
        retrievedUser.setUsername(user.getUsername());
        retrievedUser.setSlug(slugify.slugify(user.getUsername()));
        retrievedUser.setEmail(user.getEmail());
        retrievedUser.setBiography(user.getBiography());
        if (user.getPassword() != null) {
            retrievedUser.setPassword(user.getPassword());
        }
        return this.userRepository.save(retrievedUser);
    }

    public User updateBanner(UUID uuid, User user) {
        User retrievedUser = this.userRepository.findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("User not found with UUID: " + uuid));
        retrievedUser.setBannerPicture(user.getBannerPicture());
        return this.userRepository.save(retrievedUser);
    }

    public User updateProfilePicture(UUID uuid, User user) {
        User retrievedUser = this.userRepository.findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("User not found with UUID: " + uuid));
        retrievedUser.setProfilePicture(user.getProfilePicture());
        return this.userRepository.save(retrievedUser);
    }

    public void deleteUser(UUID uuid) {
        this.userRepository.deleteById(uuid);
    }

    public UUID findUserUuidByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getUuid();
        } else {
            throw new IllegalArgumentException("User not found with username: " + username);
        }
    }

    public String findUserSlugByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getSlug();
        } else {
            throw new IllegalArgumentException("User slug not found with username: " + username);
        }
    }

    public void addGame(UUID userUuid, Long gameId) {
        Optional<User> user = userRepository.findByUuid(userUuid);
        if (user.isPresent()) {

            Optional<Game> retrievedGame = gameRepository.findById(gameId);

            if (retrievedGame.isPresent()) {
                this.game = retrievedGame.get();
            } else {
                game.setId(gameId);
                gameRepository.save(game);
            }
            List<Game> games = user.get().getGames();

            if (!games.contains(game)) {
                games.add(game);
                user.get().setGames(games);
                userRepository.save(user.get());
            } else {
                throw new IllegalArgumentException("Game is already registered in the user list");
            }
        } else {
            throw new IllegalArgumentException("User not found with UUID: " + userUuid);
        }
    }

    private GameDTO convertToDTO(Game game) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(game.getId());
        return gameDTO;
    }

    public List<GameDTO> getList(UUID userUuid) {
        // Correctly handle the Optional returned by findByUuid
        Optional<User> userOptional = userRepository.findByUuid(userUuid);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();  // Retrieve the User object
            return user.getGames().stream()  // Get the games associated with the user
                    .map(this::convertToDTO)  // Convert each game to GameDTO
                    .collect(Collectors.toList());  // Collect the result into a List
        } else {
            throw new IllegalArgumentException("User not found with UUID: " + userUuid);
        }
    }
    

    public List<GameDTO> deleteGame(UUID userUuid, Long gameId) {
        Optional<User> userOptional = userRepository.findByUuid(userUuid);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Optional<Game> gameInList = gameRepository.findById(gameId);
            if (gameInList.isPresent()) {
                this.game = gameInList.get();

                if (userOptional.get().getGames().contains(game)) {
                    user.getGames().remove(game);
                    userRepository.save(user);
                }
                return user.getGames().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            } else {
                throw new IllegalArgumentException("Game not found in the user's list: " + gameId);
            }
        } else {
            throw new IllegalArgumentException("User not found with UUID: " + userUuid);
        }
    }

    public UUID getUuidInToken(String token) {
        try {
            return jwtService.extractUuid(token);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Impossible d'extraire l'UUID du token : " + e.getMessage(), e);
        }
    }

    public String getSlugInToken(String token) {
        String username = jwtService.extractUsername(token);
        String userSlug = this.findUserSlugByUsername(username);
        return userSlug;
    }
}

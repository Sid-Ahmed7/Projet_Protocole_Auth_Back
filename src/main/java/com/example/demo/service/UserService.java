package com.example.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
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
        List<User> users = this.userRepository.findAll();
        return users;
    }

    public User getOneById(Long id) {
        User user = this.userRepository.findById(id).get();
        return user;
    }

    public User getOneBySlug(String slug) {
        User user = this.userRepository.findBySlug(slug);
        return user;
    }

    public User createUser(User user) {
        user.setSlug(slugify.slugify(user.getUsername()));
        return this.userRepository.save(user);
    }

    public User findByemail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User updateUser(Long id, User user) {
        User retrievedUser = this.userRepository.findById(id).get();
        retrievedUser.setUsername(user.getUsername());
        retrievedUser.setSlug(slugify.slugify(user.getUsername()));
        retrievedUser.setEmail(user.getEmail());
        retrievedUser.setBiography(user.getBiography());
        if (user.getPassword() != null) {
            retrievedUser.setPassword(user.getPassword());
        }
        
        return this.userRepository.save(retrievedUser);
    }

    public User updateBanner(Long id, User user) {
        User retrievedUser = this.userRepository.findById(id).get();
        retrievedUser.setBannerPicture(user.getBannerPicture());
        return this.userRepository.save(retrievedUser);
    }

    public User updateProfilePicture(Long id, User user) {
        User retrievedUser = this.userRepository.findById(id).get();
        retrievedUser.setProfilePicture(user.getProfilePicture());
        return this.userRepository.save(retrievedUser);
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public Long findUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getId();
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public String findUserSlugByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getSlug();
        } else {
            throw new IllegalArgumentException("User slug not found");
        }
    }

    public void addGame(Long userId, Long gameId) {
        Optional<User> user = userRepository.findById(userId);
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
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
    }

    private GameDTO convertToDTO(Game game) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(game.getId());
        return gameDTO;
    }

    public List<GameDTO> getList(Long userId) {

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getGames().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("User not found with id: " + userId);

        }

    }

    public List<GameDTO> deleteGame(Long userId, Long gameId) {

        Optional<User> userOptional = userRepository.findById(userId);
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
                throw new IllegalArgumentException("le jeux n'est pas dans la liste: " + gameId);
            }
        } else {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
    }

    public Long getIdInToken(String token) {
        String username = jwtService.extractUsername(token);
        Long userId = this.findUserIdByUsername(username);
        return userId;
    }

    public String getSlugInToken(String token) {
        String username = jwtService.extractUsername(token);
        String userSlug = this.findUserSlugByUsername(username);
        return userSlug;
    }
}
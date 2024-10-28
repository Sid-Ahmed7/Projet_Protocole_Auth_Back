package com.example.demo.entity.dto;

import java.util.*;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDTO {

    private Long id;

    @NotBlank(message = "Veuillez renseigner un nom d'utilisateur")
    @Size(min = 3, max = 15, message = "Le nom d'utilisateur doit faire entre 3 et 15 caractères de long")
    private String username;

    private String slug;

    @NotBlank(message = "Veuillez renseigner une adresse email")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "Veuillez renseigner une adresse email valide")
    private String email;

    @NotBlank(message = "Veuillez renseigner un mot de passe")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$")
    private String password;

    private boolean role;
    private String bannerPicture;
    private String profilePicture;

    private String biography;
    private boolean isOnline;

    private List<GameDTO> games;

    public UserDTO() {
    }

    public UserDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = false;
    }

    public UserDTO(String username, String email, String password, String biography) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.biography = biography;
    }

    public UserDTO(String username, String email, String password, String biography, String slug, boolean role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.biography = biography;
        this.slug = slug;
        this.role = role;
    }

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSlug() {
        return this.slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRole() {
        return this.role;
    }

    public boolean getRole() {
        return this.role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }

    public String getBannerPicture() {
        return this.bannerPicture;
    }

    public void setBannerPicture(String bannerPicture) {
        this.bannerPicture = bannerPicture;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBiography() {
        return this.biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public boolean isIsOnline() {
        return this.isOnline;
    }

    public boolean getIsOnline() {
        return this.isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public List<GameDTO> getGames() {
        return games;
    }

    public void setGames(List<GameDTO> games) {
        this.games = games;
    }
}

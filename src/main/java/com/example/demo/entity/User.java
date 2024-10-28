package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Veuillez renseigner un nom d'utilisateur")
    @Size(min = 3, max = 15, message = "Le nom d'utilisateur doit faire entre 3 et 15 caractères de long")
    private String username;

    private String slug;

    @Column(unique = true)
    @NotBlank(message = "Veuillez renseigner une adresse email")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "Veuillez renseigner une adresse email valide")
    private String email;

    @NotBlank(message = "Veuillez renseigner un mot de passe")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$")
    private String password;

    @Transient
    private String newPassword;

    private boolean role;
    private String bannerPicture;
    private String profilePicture;

    @Column(columnDefinition = "TEXT")
    private String biography;
    private boolean isOnline;

    @OneToMany(mappedBy = "user")
    private List<Commentary> commentaries;

    @ManyToMany
    @JoinTable(name = "user_game", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "game_id"))
    private List<Game> games = new ArrayList<>();

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = false;
    }

    public User(String username, String email, String newPassword, String biography) {
        this.username = username;
        this.email = email;
        this.newPassword = newPassword;
        this.biography = biography;
    }

    public User(String username, String email, String password, String biography, String slug, boolean role) {
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

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

}
package com.example.demo.dto;

public class UserUpdate {

    private String username;
    private String email;
    private String biography;
    private String password = "";

    public UserUpdate (String username, String email, String biography) {
        this.username = username;
        this.email = email;
        this.biography = biography;
    }    

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBiography() {
        return this.biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

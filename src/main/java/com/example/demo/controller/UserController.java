package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.demo.service.LoginAttemptService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository;

import com.example.demo.config.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private RoleRepository roleRepository;

    @Operation(summary = "Get all users", description = "Get all users")
    @GetMapping("")
    public List<User> getAll() {
        return this.userService.getAll();
    }

    @Operation(summary = "Get one user by slug", description = "Get one user by slug")
    @GetMapping("/profile/{uuid}")
    public User getOneByUUID(@PathVariable UUID uuid) {
        return this.userService.getOneByUuid(uuid);
    }

    @Operation(summary = "Create user", description = "Create user")
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'adresse email est déjà utilisée");
        }
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
            Role userRole = roleRepository.findByName("USER");
    if (userRole == null) {
        userRole = new Role("USER"); 
        roleRepository.save(userRole);
    }

    user.getRoles().add(userRole);  
        this.userService.createUser(user);
        return ResponseEntity.ok("Utilisateur créé avec succès");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User users) {
        if (loginAttemptService.isBlocked(users.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account locked. Please try again later.");
        }
    
        User user = userService.findByEmail(users.getEmail());
        if (user != null && passwordEncoder.matches(users.getPassword(), user.getPassword())) {
            loginAttemptService.registerLoginAttempt(users.getEmail(), true);
    
            String token = jwtService.generateToken(user.getUuid(), user.getSlug());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
    
        loginAttemptService.registerLoginAttempt(users.getEmail(), false);
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
    }
    @Operation(summary = "Update user", description = "Update user")
    @PutMapping("/edit/{uuid}")
    public User updateUser(@PathVariable UUID uuid, @RequestBody User user,
            @RequestParam(defaultValue = "none") String picture) {
        switch (picture) {
            case "banner":
                return this.userService.updateBanner(uuid, user);

            case "profile":
                return this.userService.updateProfilePicture(uuid, user);

            case "none":
                return this.userService.updateUser(uuid, user);

            default:
                return this.userService.updateUser(uuid, user);
        }
    }

    @Operation(summary = "Delete user", description = "Delete user")
    @DeleteMapping("/{uuid}")
    public void deleteUser(@PathVariable UUID uuid) {
        this.userService.deleteUser(uuid);
    }




}
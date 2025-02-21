package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
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
import com.example.demo.service.BruteForceProtectionService;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository;

import com.example.demo.config.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

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
    private BruteForceProtectionService bruteForceProtectionService;

    @Autowired
    private RoleRepository roleRepository;

    @Operation(summary = "Get all users", description = "Get all users")
    @GetMapping("")
    public List<User> getAll() {
        return this.userService.getAll();
    }

@GetMapping("/profile/{uuid}")
public ResponseEntity<?> getProfile(@CookieValue(name = "jwt", required = false) String token) {
    if (token == null || token.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided");
    }
    UUID uuid = jwtService.extractUuid(token);
    if (uuid == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }
    User user = userService.getOneByUuid(uuid);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    return ResponseEntity.ok(user);
}
    
    @Operation(summary = "Create user", description = "Create user")
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'adresse email est déjà utilisée");
        }
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
            Role userRole = roleRepository.findByName("User");
    if (userRole == null) {
        userRole = new Role("User"); 
        roleRepository.save(userRole);
    }

    user.getRoles().add(userRole);  
        this.userService.createUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User users, HttpServletResponse response) {
        if (bruteForceProtectionService.isBlocked(users.getEmail())) {
            long remainingTime = bruteForceProtectionService.getRemainingLockTime(users.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Account locked. Please try again after " + remainingTime + " seconds."));
        }
    
        User user = userService.findByEmail(users.getEmail());
    
        if (user != null && passwordEncoder.matches(users.getPassword(), user.getPassword())) {
            bruteForceProtectionService.loginSucceeded(users.getEmail());
    
            
            String token = jwtService.generateToken(user.getUuid());
    
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)  
                    .secure(false)   
                    .path("/")      
                    .maxAge(-1)      
                    .sameSite("Strict")
                    .build();
    
            response.addHeader("Set-Cookie", cookie.toString());
    
            return ResponseEntity.ok().build();
        }
    
        bruteForceProtectionService.loginFailed(users.getEmail());
    
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid credentials."));
    }
    
    
    
    @Operation(summary = "Update user", description = "Update user")
    @PutMapping("/edit/{uuid}")
    public ResponseEntity<?> updateUser(@CookieValue(name = "jwt", required = false) String token,
                                        @PathVariable UUID uuid, 
                                        @RequestBody User user,
                                        @RequestParam(defaultValue = "none") String picture) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided");
        }
        
        UUID tokenUuid = jwtService.extractUuid(token);
        if (tokenUuid == null || !tokenUuid.equals(uuid)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or user mismatch");
        }
        
        switch (picture) {
            case "banner":
                userService.updateBanner(uuid, user);
                break;
    
            case "profile":
                userService.updateProfilePicture(uuid, user);
                break;
    
            case "none":
            default:
                userService.updateUser(uuid, user);
                break;
        }
        return ResponseEntity.ok(user);
    }
    

    @Operation(summary = "Delete user", description = "Delete user")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteUser(@CookieValue(name = "jwt", required = false) String token, 
                                        @PathVariable UUID uuid) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided");
        }
        
        UUID tokenUuid = jwtService.extractUuid(token);
        if (tokenUuid == null || !tokenUuid.equals(uuid)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or user mismatch");
        }
        
        userService.deleteUser(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
 
    @PostMapping("/logout")
public ResponseEntity<?> logoutUser(HttpServletResponse response) {
    ResponseCookie cookie = ResponseCookie.from("jwt", "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0) 
            .sameSite("Strict")
            .build();

    response.addHeader("Set-Cookie", cookie.toString());

    return ResponseEntity.ok("Utilisateur déconnecté avec succès");
}




}
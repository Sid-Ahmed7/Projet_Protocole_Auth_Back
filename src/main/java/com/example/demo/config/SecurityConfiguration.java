package com.example.demo.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.config.filter.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final JwtRequestFilter jwtRequestFilter;

    
    public SecurityConfiguration(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  
            .authorizeHttpRequests(authz -> 
                authz
                        .requestMatchers("/games/**", "/commentaries/**", "/notes/**", "/users/edit/**").hasAnyRole("USER")
                        .requestMatchers("/users/delete/**").hasAnyRole("ADMIN")
                        .requestMatchers("/users/register", "/users/login").permitAll()
                        .anyRequest().authenticated()
            )
           .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

  
}
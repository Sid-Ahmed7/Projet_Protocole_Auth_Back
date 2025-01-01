package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    public User findBySlug(String slug);

    User findByEmail(String email);

    User findByUsername(String username);
    Optional<User> findByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);
}
package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findById(Long id);

}

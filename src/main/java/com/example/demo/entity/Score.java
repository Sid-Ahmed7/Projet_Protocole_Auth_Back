package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.*;
@Entity
@Table(name = "score")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long score;
    private UUID UserUuid;
    private Long GameId;

    public Score() {
    }

    public Score(Long id, Long score, UUID userUuid, Long gameId) {
        this.id = id;
        this.score = score;
        UserUuid = userUuid;
        GameId = gameId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public UUID getUserUuid() {
        return UserUuid;
    }

    public void setUserUuid(UUID userUuid) {
        UserUuid = userUuid;
    }

    public Long getGameId() {
        return GameId;
    }

    public void setGameId(Long gameId) {
        GameId = gameId;
    }
}

package com.example.demo.service;

import com.example.demo.entity.Score;
import com.example.demo.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreService {
    @Autowired
    private ScoreRepository scoreRepository;

    public List<Score> findAll(){
        return scoreRepository.findAll();
    }

    public Optional<Score> findById(Long id) {
        return scoreRepository.findById(id);
    }

    public Score save(Score score) {
        return scoreRepository.save(score);
    }

    public void deleteById(Long id) {
        scoreRepository.deleteById(id);
    }

    public Optional<Score> update(Long id, Score scoreDetails) {
        return scoreRepository.findById(id).map(score -> {
            score.setScore(scoreDetails.getScore());
            score.setUserUuid(scoreDetails.getUserUuid());
            score.setGameId(scoreDetails.getGameId());
            return scoreRepository.save(score);
        });
    }
}

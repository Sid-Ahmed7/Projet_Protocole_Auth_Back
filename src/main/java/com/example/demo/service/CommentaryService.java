package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Commentary;
import com.example.demo.repository.CommentaryRepository;



@Service
public class CommentaryService {

    @Autowired
    private CommentaryRepository commentaryRepository;

    public List<Commentary> getAll() {
        List<Commentary> commentaries = this.commentaryRepository.findAll();
        return commentaries;
    }

    public Commentary getOneById(Long id) {
        Commentary commentary = this.commentaryRepository.findById(id).get();
        return commentary;
    }

    public Commentary createCommentary(Commentary commentary) {
        Commentary createdCommentary = this.commentaryRepository.save(commentary);
        return createdCommentary;
    }

    public void deleteCommentary(Long id) {
        this.commentaryRepository.deleteById(id);
    }
}

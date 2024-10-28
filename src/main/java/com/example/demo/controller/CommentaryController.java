package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.example.demo.service.CommentaryService;

import io.swagger.v3.oas.annotations.Operation;
import com.example.demo.entity.Commentary;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/commentaries")
public class CommentaryController {
    
    @Autowired
    private CommentaryService commentaryService;

    @Operation(summary = "Get all commentaries", description = "Get all commentaries")
    @GetMapping("")
    public List<Commentary> getAll() {
        return this.commentaryService.getAll();
    }

    @Operation(summary = "Get commentary by id", description = "Get commentary by id")
    @GetMapping("/{id}")
    public Commentary getOneById(@PathVariable Long id) {
        return this.commentaryService.getOneById(id);
    }

    @Operation(summary = "Create commentary", description = "Create commentary")
    @PostMapping("")
    public Commentary createCommentary(@RequestBody Commentary commentary) {
        return this.commentaryService.createCommentary(commentary);
    }

    @Operation(summary = "Delete commentary", description = "Delete commentary")
    @DeleteMapping("/{id}")
    public void deleteCommentary(@PathVariable Long id) {
        this.commentaryService.deleteCommentary(id);
    }
}

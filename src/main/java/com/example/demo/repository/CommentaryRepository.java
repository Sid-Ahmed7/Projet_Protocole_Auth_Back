package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Commentary;


@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, Long>{
    
    
}

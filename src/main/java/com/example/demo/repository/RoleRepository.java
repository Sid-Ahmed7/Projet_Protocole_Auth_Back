package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Role;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

    Role findByName(String name);
}

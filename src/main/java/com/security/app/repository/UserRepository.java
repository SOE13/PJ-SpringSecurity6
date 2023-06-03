package com.security.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.app.models.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    Boolean existsByUsername(String username);
}

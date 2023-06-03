package com.security.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.app.models.Roles;

public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByName(String username);

    Boolean existsByName(String username);
}

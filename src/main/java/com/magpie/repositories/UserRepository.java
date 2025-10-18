package com.magpie.repositories;

import com.magpie.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPublicId(UUID publicId);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}

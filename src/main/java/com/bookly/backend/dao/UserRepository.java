package com.bookly.backend.dao;

import com.bookly.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getBySecurityToken(String securityToken);
    Optional<User> getByLogin(String login);
}

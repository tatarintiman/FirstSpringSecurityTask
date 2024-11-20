package ru.itmentor.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

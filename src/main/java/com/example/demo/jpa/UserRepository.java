package com.example.demo.jpa;

import com.example.demo.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findAllByBirthDateBetween(LocalDate start, LocalDate end);
    Optional<User> findById(@NonNull UUID id);
    boolean existsByEmail(String email);
}
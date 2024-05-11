package com.example.demo.service;

import com.example.demo.jpa.UserRepository;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Value("${user.minimum-age}")
    private int minimumAge;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(Optional<User> userOptional) {
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("User data is required."));
        checkAge(user);
        return userRepository.save(user);
    }

    void checkAge(User user) {
        LocalDate now = LocalDate.now();
        LocalDate birthDate = user.getBirthDate();
        long age = Period.between(birthDate, now).getYears();

        if (age < minimumAge) {
            throw new IllegalArgumentException("User must be at least " + minimumAge + " years old.");
        }
    }

    //200
    public User updateUser(UUID id, Optional<User> updatedUserOptional) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        updatedUserOptional.ifPresent(updatedUser -> {
                // Делать изменение почты не особо правильно, так как этим может воспользоваться злоумышленик
//                if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
                if (updatedUser.getFirstName() != null) existingUser.setFirstName(updatedUser.getFirstName());
                if (updatedUser.getLastName() != null) existingUser.setLastName(updatedUser.getLastName());
                if (updatedUser.getBirthDate() != null) existingUser.setBirthDate(updatedUser.getBirthDate());
                if (updatedUser.getAddress() != null) existingUser.setAddress(updatedUser.getAddress());
                if (updatedUser.getPhoneNumber() != null) existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        });
        checkAge(existingUser);

        return userRepository.save(existingUser);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public List<User> findUsersByBirthDateRange(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
        return userRepository.findAllByBirthDateBetween(start, end);
    }


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

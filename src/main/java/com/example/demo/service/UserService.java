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

    /**
     * Creates a new user.
     *
     * @param userOptional the optional user object to be created
     * @return the saved user object
     * @throws IllegalArgumentException if user data is required
     */
    public User createUser(Optional<User> userOptional) {
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("User data is required."));
        checkAge(user);
        return userRepository.save(user);
    }

    /**
     * Checks if the user's age is greater or equal to the minimum age specified in the application properties.
     *
     * @param user the user object to be checked
     * @throws IllegalArgumentException if the user is below the minimum age
     */
    private void checkAge(User user) {
        LocalDate now = LocalDate.now();
        LocalDate birthDate = user.getBirthDate();
        long age = Period.between(birthDate, now).getYears();

        if (age < minimumAge) {
            throw new IllegalArgumentException("User must be at least " + minimumAge + " years old.");
        }
    }

    /**
     * Updates an existing user.
     *
     * @param id the unique identifier of the user to be updated
     * @param updatedUserOptional the optional updated user object
     * @return the updated user object
     * @throws IllegalArgumentException if the user is not found
     */
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

    /**
     * Deletes a user by its unique identifier.
     *
     * @param id the unique identifier of the user to be deleted
     */
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    /**
     * Finds users by their birth dates within a specified range.
     *
     * @param start the start date of the range
     * @param end   the end date of the range
     * @return a list of users whose birth dates fall within the specified range
     * @throws IllegalArgumentException if the start date is after the end date
     */
    public List<User> findUsersByBirthDateRange(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
        return userRepository.findAllByBirthDateBetween(start, end);
    }

    /**
     * Checks if a user with the specified email already exists in the database.
     *
     * @param email the email of the user to be checked
     * @return true if a user with the specified email exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

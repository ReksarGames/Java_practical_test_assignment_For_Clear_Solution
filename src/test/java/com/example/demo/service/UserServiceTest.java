package com.example.demo.service;

import com.example.demo.jpa.UserRepository;
import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ValidUser_Success() {
        User user = createUserMock();
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.createUser(Optional.of(user));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_NullUser_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(Optional.empty()));
    }

    @Test
    void updateUser_ValidUser_Success() {
        UUID id = UUID.randomUUID();
        User existingUser = createUserMock();
        Optional<User> updatedUserOptional = Optional.of(createUserMock());

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        userService.updateUser(id, updatedUserOptional);

        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_UserNotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        Optional<User> updatedUserOptional = Optional.of(createUserMock());

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(id, updatedUserOptional));
    }

    @Test
    void deleteUser_ValidId_Success() {
        UUID id = UUID.randomUUID();

        userService.deleteUser(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void findUsersByBirthDateRange_StartDateAfterEndDate_ThrowsException() {
        LocalDate start = LocalDate.of(2024, 5, 1);
        LocalDate end = LocalDate.of(2024, 4, 30);

        assertThrows(IllegalArgumentException.class, () -> userService.findUsersByBirthDateRange(start, end));
    }

    private User createUserMock() {
        return User.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phoneNumber("123-456-7890")
                .build();
    }
}

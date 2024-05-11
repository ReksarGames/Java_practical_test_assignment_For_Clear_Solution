package com.example.demo.restControllers;

import com.example.demo.components.UserConverter;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserConverter userConverter;

    @Autowired
    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    //200
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDTO userDto) {
        // Проверяем, существует ли пользователь с таким email
        if (userService.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this email already exists");
        }

        User user = userService.createUser(userConverter.convertToEntity(userDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(userConverter.convertToDto(Optional.ofNullable(user)));
    }

    //200
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDto) {
        User user = userService.updateUser(id, userConverter.convertToEntity(userDto));
        return ResponseEntity.ok(user);
    }

    //200
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    //200
    @GetMapping("/search")
    public ResponseEntity<List<User>> findByDates(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        List<User> user = userService.findUsersByBirthDateRange(start, end);
        return ResponseEntity.ok().body(user);
    }
}

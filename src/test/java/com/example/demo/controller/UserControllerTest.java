package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import com.example.demo.components.UserConverter;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.restControllers.UserController;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@Slf4j
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setBirthDate(LocalDate.of(2021, 1, 1));

        testUserDTO = new UserDTO();
        testUserDTO.setFirstName("John");
        testUserDTO.setLastName("Doe");
        testUserDTO.setEmail("john.doe@example.com");
        testUserDTO.setBirthDate(LocalDate.of(2021, 1, 1));
    }

    @Test
    void testCreateUser() {
        when(userService.createUser(any())).thenReturn(testUser);
        when(userConverter.convertToEntity(any())).thenReturn(Optional.of(testUser));
        when(userConverter.convertToDto(any())).thenReturn(testUserDTO);

        ResponseEntity<?> response = userController.createUser(testUserDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testUserDTO, response.getBody());
        log.debug(response.toString());
    }
    @Test
    void testUpdateUser() {
        when(userService.updateUser(any(), any())).thenReturn(testUser);
        when(userConverter.convertToEntity(any())).thenReturn(Optional.of(testUser));
        when(userConverter.convertToDto(any())).thenReturn(testUserDTO);

        ResponseEntity<User> response = userController.updateUser(testUser.getId(), testUserDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    void testDeleteUser() {
        UUID userId = testUser.getId();
        doNothing().when(userService).deleteUser(userId); // Ничего не возвращает

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).deleteUser(userId); // Проверка вызова метода deleteUser с правильным идентификатором
    }


    @Test
    void testFindByDates() throws BadRequestException {
        when(userService.findUsersByBirthDateRange(any(), any())).thenReturn(Collections.singletonList(testUser));

        ResponseEntity<List<User>> response = userController.findByDates(LocalDate.of(1990, 1, 1), LocalDate.of(1990, 12, 1));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonList(testUser), response.getBody());
    }
}

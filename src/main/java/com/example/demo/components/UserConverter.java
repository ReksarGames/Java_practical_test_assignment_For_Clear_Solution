package com.example.demo.components;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * UserConverter class is responsible for converting UserDTO to User entity and vice versa.
 *
 *
 */
@Component
public class UserConverter {

    /**
     * Converts a UserDTO object to a User entity.
     *
     * @param userDto the UserDTO object to be converted
     * @return an Optional containing the converted User entity, or empty if the UserDTO is null
     */
    public Optional<User> convertToEntity(UserDTO userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBirthDate(userDto.getBirthDate());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return Optional.of(user);
    }

    /**
     * Converts a User entity to a UserDTO object.
     *
     * @param user the User entity to be converted
     * @return a UserDTO object containing the converted data, or throws an IllegalArgumentException if the User entity is null
     */
    public UserDTO convertToDto(Optional<User> user) {
        return user.map(userEntity -> {
            UserDTO userDto = new UserDTO();
            userDto.setEmail(userEntity.getEmail());
            userDto.setFirstName(userEntity.getFirstName());
            userDto.setLastName(userEntity.getLastName());
            userDto.setBirthDate(userEntity.getBirthDate());
            userDto.setAddress(userEntity.getAddress());
            userDto.setPhoneNumber(userEntity.getPhoneNumber());
            return userDto;
        }).orElseThrow(() -> new IllegalArgumentException("No user data available"));
    }
}
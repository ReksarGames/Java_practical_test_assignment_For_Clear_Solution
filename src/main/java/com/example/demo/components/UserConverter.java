package com.example.demo.components;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserConverter {

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

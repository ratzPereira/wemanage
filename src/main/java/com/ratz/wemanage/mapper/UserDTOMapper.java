package com.ratz.wemanage.mapper;

import com.ratz.wemanage.domain.User;
import com.ratz.wemanage.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {

    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhone(user.getPhone());
        userDTO.setTitle(user.getTitle());
        userDTO.setBio(user.getBio());
        userDTO.setImageUrl(user.getImageUrl());
        userDTO.setEnabled(user.isEnabled());
        userDTO.setNotLocked(user.isNotLocked());
        userDTO.setUsingMfa(user.isUsingMfa());
        userDTO.setCreatedAt(user.getCreatedAt());
        return userDTO;
    }

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        user.setPhone(userDTO.getPhone());
        user.setTitle(userDTO.getTitle());
        user.setBio(userDTO.getBio());
        user.setImageUrl(userDTO.getImageUrl());
        user.setEnabled(userDTO.isEnabled());
        user.setNotLocked(userDTO.isNotLocked());
        user.setUsingMfa(userDTO.isUsingMfa());
        user.setCreatedAt(userDTO.getCreatedAt());
        return user;
    }
}

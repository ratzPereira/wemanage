package com.ratz.wemanage.mapper;

import com.ratz.wemanage.domain.Role;
import com.ratz.wemanage.domain.User;
import com.ratz.wemanage.dto.UserDTO;
import org.springframework.beans.BeanUtils;

public class UserDTOMapper {

    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public static UserDTO fromUser(User user, Role role) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoleName(role.getName());
        userDTO.setPermission(role.getPermission());
        return userDTO;
    }

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }


}

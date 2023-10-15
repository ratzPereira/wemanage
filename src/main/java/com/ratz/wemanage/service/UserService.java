package com.ratz.wemanage.service;

import com.ratz.wemanage.domain.User;
import com.ratz.wemanage.dto.UserDTO;

public interface UserService {

    UserDTO createUser(User user);

    UserDTO getUserByEmail(String email);

    void sendVerificationCode(UserDTO userDTO);

    User getUser(String email);
}

package com.ratz.wemanage.service.impl;

import com.ratz.wemanage.domain.Role;
import com.ratz.wemanage.domain.User;
import com.ratz.wemanage.dto.UserDTO;
import com.ratz.wemanage.repository.RoleRepository;
import com.ratz.wemanage.repository.UserRepository;
import com.ratz.wemanage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ratz.wemanage.mapper.UserDTOMapper.fromUser;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;
    private final RoleRepository<Role> roleRepository;

    @Override
    public UserDTO createUser(User user) {
        return mapToUserDTO(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return mapToUserDTO(userRepository.getUserByEmail(email));
    }

    @Override
    public void sendVerificationCode(UserDTO userDTO) {
        userRepository.sendVerificationCode(userDTO);
    }

    @Override
    public UserDTO verifyCode(String email, String code) {
        return mapToUserDTO(userRepository.verifyCode(email, code));
    }

    private UserDTO mapToUserDTO(User user) {
        return fromUser(user, roleRepository.getRoleByUserId(user.getId()));
    }
}


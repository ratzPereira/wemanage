package com.ratz.wemanage.service.impl;

import com.ratz.wemanage.domain.User;
import com.ratz.wemanage.dto.UserDTO;
import com.ratz.wemanage.repository.UserRepository;
import com.ratz.wemanage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ratz.wemanage.mapper.UserDTOMapper.fromUser;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;

    @Override
    public UserDTO createUser(User user) {

        return fromUser(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return fromUser(userRepository.getUserByEmail(email));
    }

    @Override
    public void sendVerificationCode(UserDTO userDTO) {
        userRepository.sendVerificationCode(userDTO);
    }

    @Override
    public User getUser(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public UserDTO verifyCode(String email, String code) {
        return fromUser(userRepository.verifyCode(email, code));
    }
}


package com.ratz.wemanage.service.impl;

import com.ratz.wemanage.domain.User;
import com.ratz.wemanage.dto.UserDTO;
import com.ratz.wemanage.mapper.UserDTOMapper;
import com.ratz.wemanage.repository.UserRepository;
import com.ratz.wemanage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;

    @Override
    public UserDTO createUser(User user) {

        return UserDTOMapper.fromUser(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return UserDTOMapper.fromUser(userRepository.getUserByEmail(email));
    }
}


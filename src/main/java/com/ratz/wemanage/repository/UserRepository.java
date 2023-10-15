package com.ratz.wemanage.repository;

import com.ratz.wemanage.domain.User;
import com.ratz.wemanage.dto.UserDTO;

import java.util.Collection;

public interface UserRepository<T extends User> {

    /* Basic CRUD operations */
    T create(T data);

    Collection<T> getAll(int page, int pageSize);

    T get(Long id);

    T update(Long id);

    /* more complex CRUD operations */
    User getUserByEmail(String email);

    Boolean delete(Long id);

    void sendVerificationCode(UserDTO userDTO);

    User verifyCode(String email, String code);
}
package com.ratz.wemanage.repository;

import com.ratz.wemanage.domain.User;

import java.util.Collection;

public interface UserRepository <T extends User> {

    /* Basic CRUD operations */
    T create(T data);

    Collection<T> getAll(int page, int pageSize);

    T get(Long id);

    T update(Long id);

    Boolean delete(Long id);

    /* more complex CRUD operations */

}
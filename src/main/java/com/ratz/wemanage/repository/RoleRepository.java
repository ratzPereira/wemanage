package com.ratz.wemanage.repository;

import com.ratz.wemanage.domain.Role;

import java.util.Collection;

public interface RoleRepository<T extends Role> {

    T create(T data);

    Collection<T> getAll(int page, int pageSize);

    T get(Long id);

    T update(Long id);

    Boolean delete(Long id);



    /* more complex CRUD operations */
    void addRoleToUser(Long userId, String roleName);
}

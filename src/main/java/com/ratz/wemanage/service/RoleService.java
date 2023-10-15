package com.ratz.wemanage.service;

import com.ratz.wemanage.domain.Role;

public interface RoleService {

    Role getRoleByUserId(Long userId);
}

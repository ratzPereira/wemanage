package com.ratz.wemanage.service.impl;

import com.ratz.wemanage.domain.Role;
import com.ratz.wemanage.repository.RoleRepository;
import com.ratz.wemanage.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository<Role> roleRepository;

    @Override
    public Role getRoleByUserId(Long userId) {
        return roleRepository.getRoleByUserId(userId);
    }
}

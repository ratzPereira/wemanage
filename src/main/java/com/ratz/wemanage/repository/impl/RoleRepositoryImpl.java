package com.ratz.wemanage.repository.impl;

import com.ratz.wemanage.RowMapper.RoleRowMapper;
import com.ratz.wemanage.domain.Role;
import com.ratz.wemanage.exception.ApiException;
import com.ratz.wemanage.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static com.ratz.wemanage.enums.RoleType.ROLE_USER;
import static com.ratz.wemanage.query.RoleQuery.INSERT_ROLE_TO_USER_QUERY;
import static com.ratz.wemanage.query.RoleQuery.SELECT_ROLE_BY_NAME_QUERY;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role> {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Role create(Role data) {
        return null;
    }

    @Override
    public Collection<Role> getAll(int page, int pageSize) {
        return null;
    }

    @Override
    public Role get(Long id) {
        return null;
    }


    @Override
    public Role update(Long id) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {

        log.info("Adding role {} to user id: {}", roleName, userId);

        try {
            Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("roleName", roleName), new RoleRowMapper());
            jdbc.update(INSERT_ROLE_TO_USER_QUERY, Map.of("userId", userId, "roleId", Objects.requireNonNull(role).getId()));
        } catch (EmptyResultDataAccessException e) {
            throw new ApiException("No role found by name: " + ROLE_USER.name());
        } catch (Exception e) {
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public Role getRoleByUserId(Long userId) {
        return null;
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        return null;
    }


    @Override
    public void updateUserRole(Long userId, String roleName) {

    }


}

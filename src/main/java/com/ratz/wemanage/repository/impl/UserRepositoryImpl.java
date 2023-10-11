package com.ratz.wemanage.repository.impl;

import com.ratz.wemanage.domain.Role;
import com.ratz.wemanage.domain.User;
import com.ratz.wemanage.exception.ApiException;
import com.ratz.wemanage.repository.RoleRepository;
import com.ratz.wemanage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static com.ratz.wemanage.enums.RoleType.ROLE_USER;
import static com.ratz.wemanage.enums.VerificationType.ACCOUNT;
import static com.ratz.wemanage.query.UserQuery.*;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User> {


    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;


    @Override
    public User create(User user) {

        // Check the email is unique
        if(getEmailCount(user.getEmail().trim().toLowerCase()) > 0)
            throw new ApiException("Email already in use. Please use other email.");

        // Save new user
        try {

            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            user.setId(requireNonNull(holder.getKey()).longValue());

            // Add role to user
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());

            // Send verification URL
            String verificationUrl= getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());

            // Save URL in verification table
            jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, Map.of("userId", user.getId(), "url", verificationUrl));


            // Send email to user with verification URL
            //TODO: emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            user.setEnabled(false);
            user.setNotLocked(true);

            // Return the newly created user
            return user;


            // If errors, throw exception with proper message
        } catch (EmptyResultDataAccessException ex){
            throw new ApiException("No role found by name: " + ROLE_USER.name());

        } catch (Exception ex) {
            throw new ApiException("An error occurred. Please try again.");
        }
    }



    @Override
    public Collection<User> getAll(int page, int pageSize) {
        return null;
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public User update(Long id) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    private Integer  getEmailCount(String email) {
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", encoder.encode(user.getPassword()));
    }

    private String getVerificationUrl(String key, String type){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify" + type + "/" + key).toUriString();
    }
}

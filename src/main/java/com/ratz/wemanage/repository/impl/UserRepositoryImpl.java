package com.ratz.wemanage.repository.impl;

import com.ratz.wemanage.RowMapper.UserRowMapper;
import com.ratz.wemanage.domain.Role;
import com.ratz.wemanage.domain.User;
import com.ratz.wemanage.domain.UserPrincipal;
import com.ratz.wemanage.dto.UserDTO;
import com.ratz.wemanage.exception.ApiException;
import com.ratz.wemanage.repository.RoleRepository;
import com.ratz.wemanage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.ratz.wemanage.enums.RoleType.ROLE_USER;
import static com.ratz.wemanage.enums.VerificationType.ACCOUNT;
import static com.ratz.wemanage.query.UserQuery.*;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.time.DateUtils.addDays;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User>, UserDetailsService {

    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;


    @Override
    public User create(User user) {

        // Check the email is unique
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0)
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
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());


            // Save URL in verification table
            jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, Map.of("userId", user.getId(), "url", verificationUrl));


            // Send email to user with verification URL


            //TODO: emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            user.setEnabled(true);
            user.setNotLocked(true);

            // Return the newly created user
            return user;


            // If errors, throw exception with proper message
        } catch (Exception ex) {
            throw new ApiException("An error occurred. Please try again.");


        }
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("Getting user by email: {}", email);

        try {
            return jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, Map.of("email", email), new UserRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            log.error("Error occurred while getting user by email: {}", ex.getMessage());
            throw new ApiException("No user found with email: " + email);

        } catch (Exception ex) {
            log.error("Error occurred while getting user by email: {}", ex.getMessage());
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

    @Override
    public void sendVerificationCode(UserDTO userDTO) {

        String expirationDate = DateFormatUtils.format(addDays(new Date(), 1), DATE_FORMAT);
        String verificationCode = randomAlphabetic(8).toUpperCase();

        log.info("Sending verification code: {} to user: {}", verificationCode, userDTO.getEmail());

        try {
            jdbc.update(DELETE_VERIFICATION_CODE_BY_USER_ID, Map.of("id", userDTO.getId()));
            jdbc.update(INSERT_VERIFICATION_CODE_QUERY, Map.of("userId", userDTO.getId(), "code", verificationCode, "expirationDate", expirationDate));

            //sendSms(userDTO.getPhone(), "From: WeManage \nVerification code\n" + verificationCode);

        } catch (Exception ex) {
            log.error("Error occurred while sending verification code: {}", ex.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);

        log.info("User found by email: {}", email);

        if (user == null) throw new UsernameNotFoundException("User not found");
        else return new UserPrincipal(user, roleRepository.getRoleByUserId(user.getId()).getPermission());
    }


    private Integer getEmailCount(String email) {
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", encoder.encode(user.getPassword()));
    }

    private String getVerificationUrl(String key, String type) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify" + type + "/" + key).toUriString();
    }

}

package com.ratz.wemanage.Controller;

import com.ratz.wemanage.domain.User;
import com.ratz.wemanage.domain.response.HttpResponse;
import com.ratz.wemanage.dto.UserDTO;
import com.ratz.wemanage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<HttpResponse> createUser(@RequestBody User user) {

        UserDTO userDTO = userService.createUser(user);

        return ResponseEntity.created(getURI())
                .body(HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("user", userDTO))
                        .message("User created")
                        .statusCode(HttpStatus.CREATED.value())
                        .httpStatus(HttpStatus.CREATED)
                        .build());
    }

    private URI getURI() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/get/<userId>").toUriString());
    }
}

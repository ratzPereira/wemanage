package com.ratz.wemanage.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratz.wemanage.domain.response.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static java.time.LocalTime.now;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(now().toString())
                .reason("Access Denied")
                .statusCode(HttpStatus.FORBIDDEN.value())
                .httpStatus(HttpStatus.FORBIDDEN)
                .build();

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        OutputStream out = response.getOutputStream();

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse);

        out.flush();
    }
}

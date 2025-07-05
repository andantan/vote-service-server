package org.zerock.voteservice.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.CommonFailureResponseDto;

import java.io.IOException;

@Log4j2
@Component
public class UserAuthenticationErrorHandler {

    private final ObjectMapper objectMapper;

    public UserAuthenticationErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handleGenericError(
            HttpServletResponse response, HttpStatus httpStatus, String message
    ) throws IOException {
        int statusCode = httpStatus.value();
        String status = httpStatus.name();

        this.writeErrorAndFlush(response, statusCode, status, message);
    }

    private void writeErrorAndFlush(
            HttpServletResponse response,
            int httpStatusCode, String status, String message
    ) throws IOException {
        response.setStatus(httpStatusCode);
        response.setContentType("application/json;charset=UTF-8");

        CommonFailureResponseDto failureDto = CommonFailureResponseDto.builder()
                .success(false)
                .message(message)
                .status(status)
                .httpStatusCode(httpStatusCode)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(failureDto));
        response.getWriter().flush();
    }
}

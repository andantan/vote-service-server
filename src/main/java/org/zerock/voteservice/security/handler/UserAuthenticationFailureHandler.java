package org.zerock.voteservice.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.zerock.voteservice.adapter.in.web.dto.user.error.UserErrorResponseDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public UserAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=utf-8");

        UserErrorResponseDto failureDto = this.getErrorResponseDto(exception);

        response.getWriter().write(objectMapper.writeValueAsString(failureDto));
        response.getWriter().flush();
    }

    private UserErrorResponseDto getErrorResponseDto(AuthenticationException exception) {

        String errorMessage;
        String errorStatus;

        if (exception instanceof UsernameNotFoundException) {
            errorMessage = "사용자 이름 또는 비밀번호가 일치하지 않습니다.";
            errorStatus = "USER_NOT_FOUND";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "사용자 이름 또는 비밀번호가 일치하지 않습니다.";
            errorStatus = "USER_NOT_FOUND";
        } else if (exception instanceof AccountExpiredException) {
            errorMessage = "계정이 만료되었습니다.";
            errorStatus = "EXPIRED_USER";
        } else if (exception instanceof LockedException) {
            errorMessage = "계정이 잠겨있습니다.";
            errorStatus = "LOCKED_USER";
        } else if (exception instanceof DisabledException) {
            errorMessage = "계정이 비활성화되었습니다.";
            errorStatus = "UNAVAILABLE_USER";
        } else if (exception instanceof InsufficientAuthenticationException) {
            errorMessage = "인증 자격 증명이 부족합니다.";
            errorStatus = "INSUFFICIENT_AUTHENTICATION";
        } else {
            errorMessage = "알 수 없는 인증 오류가 발생했습니다: " + exception.getMessage();
            errorStatus = "UNKNOWN_ERROR";
        }

        return UserErrorResponseDto.builder()
                .success(false)
                .message(errorMessage)
                .status(errorStatus)
                .httpStatusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
    }
}
